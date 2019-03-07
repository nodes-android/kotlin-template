package dk.eboks.app.presentation.ui.start.screens

import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.config.AppConfig
import dk.eboks.app.domain.interactors.BootstrapInteractor
import dk.eboks.app.domain.managers.PrefManager
import dk.eboks.app.domain.models.local.ViewError
import dk.eboks.app.domain.models.login.User
import dk.eboks.app.profile.interactors.GetUserProfileInteractor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class StartPresenter @Inject constructor(
    private val bootstrapInteractor: BootstrapInteractor,
    private val prefManager: PrefManager,
    private val appConfig: AppConfig
) :
    StartContract.Presenter,
    BasePresenterImpl<StartContract.View>(),
    BootstrapInteractor.Output,
    GetUserProfileInteractor.Output {

    init {
        bootstrapInteractor.output = this
    }

    override fun startup() {
        Timber.e("Startup, running version control")
        if (appConfig.isDebug) {
            view { performVersionControl() }
        } else {
            Timber.e("Release not running appOpen call")
            proceed()
        }
    }

    override fun proceed() {
        Timber.e("Proceeding to run bootstrap interactor")
        bootstrapInteractor.run()
    }

    override fun onBootstrapDone(hasUsers: Boolean, autoLogin: Boolean) {
        Timber.e("Boostrap done")
        view {
            bootstrapDone()
            if (autoLogin) {
                startMain()
            } else if (hasUsers) {
                showUserCarouselComponent()
            } else {
                if (BuildConfig.ENABLE_BETA_DISCLAIMER) {
                    if (!prefManager.getBoolean("didShowBetaDisclaimer", false)) {
                        prefManager.setBoolean("didShowBetaDisclaimer", true)
                        showDisclaimer()
                    } else
                        showWelcomeComponent()
                } else
                    showWelcomeComponent()
            }
        }
    }

    override fun onBootstrapError(error: ViewError) {
        view { showErrorDialog(error) }
    }

    override fun onGetUser(user: User) {
        view { startMain() }
    }

    override fun onGetUserError(error: ViewError) {
        Timber.w("WARNING: SERVER COULDN'T FIND THE USER")
        view { showUserCarouselComponent() }
    }
}