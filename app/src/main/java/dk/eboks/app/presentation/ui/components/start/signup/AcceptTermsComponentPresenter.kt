package dk.eboks.app.presentation.ui.components.start.signup

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class AcceptTermsComponentPresenter @Inject constructor(val appState: AppStateManager) : AcceptTermsComponentContract.Presenter, BasePresenterImpl<AcceptTermsComponentContract.View>() {

    init {
    }

}