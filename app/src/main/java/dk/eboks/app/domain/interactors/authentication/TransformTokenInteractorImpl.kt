package dk.eboks.app.domain.interactors.authentication

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.managers.AuthClient
import dk.eboks.app.domain.managers.CacheManager
import dk.eboks.app.domain.managers.UserManager
import dk.eboks.app.domain.managers.UserSettingsManager
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.repositories.MailCategoriesRepository
import dk.eboks.app.network.Api
import dk.eboks.app.network.managers.protocol.EAuth2
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by Christian on 5/28/2018.
 * @author   Christian
 * @since    5/28/2018.
 */
class TransformTokenInteractorImpl(
        executor: Executor, val api: Api,
        val appStateManager: AppStateManager,
        val userManager: UserManager,
        val userSettingsManager: UserSettingsManager,
        val authClient: AuthClient,
        val cacheManager: CacheManager,
        val foldersRepositoryMail: MailCategoriesRepository
) : BaseInteractor(executor), TransformTokenInteractor {
    override var output: TransformTokenInteractor.Output? = null
    override var input: TransformTokenInteractor.Input? = null

    override fun execute() {
        try {
            input?.loginState?.kspToken?.let { kspToken ->
                authClient.transformKspToken(kspToken, longClient = appStateManager.state?.currentSettings?.stayLoggedIn
                        ?: false)?.let { token ->
                    appStateManager.state?.loginState?.token = token

                    val userResult = api.getUserProfile().execute()
                    // user exists in nemid, but not in eboks, show error
                    if (userResult.code() == 400) {
                        runOnUIThread {
                            output?.onLoginError(ViewError(title = Translation.error.authenticationErrorTitle, message = Translation.error.authenticationErrorMessage, shouldCloseView = true))
                        }
                        return
                    }
                    // user exists in nemid, but is not the same as in eboks (used wrong Nem-ID), show error
                    if (userResult.code() == 403) {
                        runOnUIThread {
                            output?.onLoginError(ViewError(title = Translation.error.wrongIdErrorTitle, message = Translation.error.wrongIdErrorMessage, shouldCloseView = true))
                        }
                        return
                    }
                    userResult?.body()?.let { user ->
                        // update the states
                        Timber.e("Saving user $user")
                        val newUser = userManager.put(user)
                        val newSettings = userSettingsManager.get(newUser.id)

                        appStateManager.state?.loginState?.userLoginProviderId?.let {
                            newSettings.lastLoginProviderId = it
                        }
                        appStateManager.state?.loginState?.activationCode?.let {
                            newSettings.activationCode = it
                        }

                        /*
                        appStateManager.state?.loginState?.lastUser?.let { lastUser ->
                                Timber.e("Different user id detected on login, clearing caches")
                                cacheManager.clearStores()
                            }
                        }
                        */
                        cacheManager.clearStores()

                        userSettingsManager.put(newSettings)
                        appStateManager.state?.loginState?.lastUser = newUser
                        appStateManager.state?.currentUser = newUser
                        appStateManager.state?.currentSettings = newSettings
                    }
                    EAuth2.ignoreFurtherLoginRequests = false
                    appStateManager.save()

                    runOnUIThread {
                        output?.onLoginSuccess(token)
                    }
                    appStateManager.state?.selectedFolders = foldersRepositoryMail.getMailCategories(false, appStateManager.state?.impersoniateUser?.userId.toString())

                }.guard {
                    runOnUIThread {
                        output?.onLoginError(ViewError(title = Translation.error.genericTitle, message = Translation.error.genericMessage, shouldCloseView = true)) // TODO better error
                    }
                }
                input?.loginState?.kspToken = null // consume the token - it's only usable once anyway
            }
        } catch (t: Throwable) {
            Timber.e("Token transform fail: $t")
            runOnUIThread {
                output?.onLoginError(exceptionToViewError(t))
            }
        }
    }

}