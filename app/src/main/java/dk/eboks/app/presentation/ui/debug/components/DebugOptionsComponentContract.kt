package dk.eboks.app.presentation.ui.debug.components

import dk.eboks.app.domain.config.Environments
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface DebugOptionsComponentContract {
    interface View : BaseView {
        fun showCountrySpinner(configIndex : Int)
        fun showEnvironmentSpinner(environments: Map<String, Environments>, curEnv : Environments?)
    }

    interface Presenter : BasePresenter<View> {
        fun setup()
        fun setConfig(name : String)
        fun setEnvironment(name : String)
    }
}