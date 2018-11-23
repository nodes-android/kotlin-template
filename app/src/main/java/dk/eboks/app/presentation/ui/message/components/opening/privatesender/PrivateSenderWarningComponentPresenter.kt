package dk.eboks.app.presentation.ui.message.components.opening.privatesender

import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.presentation.ui.mail.components.maillist.MailListComponentFragment
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class PrivateSenderWarningComponentPresenter @Inject constructor(val appState: AppStateManager, val executor: Executor) :
        PrivateSenderWarningComponentContract.Presenter,
        BasePresenterImpl<PrivateSenderWarningComponentContract.View>()
{
    init {
    }

    override fun setShouldProceed(proceed: Boolean) {
        appState.state?.openingState?.let { state ->
            state.shouldProceedWithOpening = proceed
        }
        runAction { v->v.showOpeningProgress(true) }
        executor.signal("messageOpenDone")
    }
}