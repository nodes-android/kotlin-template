package dk.eboks.app.domain.interactors

import android.os.Build
import dk.eboks.app.BuildConfig
import dk.eboks.app.domain.managers.ProtocolManager
import dk.eboks.app.domain.models.request.AppInfo
import dk.eboks.app.domain.models.request.LoginRequest
import dk.eboks.app.domain.models.request.UserInfo
import dk.eboks.app.network.Api
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

/**
 * Created by bison on 24-06-2017.
 */
class LoginInteractorImpl(executor: Executor, val api: Api, val protocolManager: ProtocolManager) : BaseInteractor(executor), LoginInteractor {
    override var output : LoginInteractor.Output? = null
    override var input : LoginInteractor.Input? = null

    override fun execute() {
        // we don't use input in this example but we could:

        try {
            input?.let {
                // do something with unwrapped input
                val version = BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE
                val os_version = Build.VERSION.RELEASE
                val device = Build.MODEL

                val arg = LoginRequest(AppInfo(version = version, os = "Android", device = device, osVersion = os_version), it.userInfo)
                it.userInfo.loginDateTime = protocolManager.getDateTime()
                protocolManager.userInfo = it.userInfo
                val res = api.login(arg).blockingGet()

                runOnUIThread {
                    output?.onLogin()
                }
            }
            if(input == null) {
                runOnUIThread {
                    output?.onLoginError("Interactor missing input")
                }
            }

        } catch (e: Exception) {
            runOnUIThread {
                output?.onLoginError(e.message ?: "Unknown error")
            }
        }
    }
}