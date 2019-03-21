package dk.eboks.app.mail.presentation.ui.message.components.opening.privatesender

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
internal class PrivateSenderWarningComponentPresenter @Inject constructor(
    private val appState: AppStateManager,
    private val executor: Executor
) :
    PrivateSenderWarningComponentContract.Presenter,
    BasePresenterImpl<PrivateSenderWarningComponentContract.View>() {
    init {
    }

    override fun setShouldProceed(proceed: Boolean) {
        appState.state?.openingState?.let { state ->
            state.shouldProceedWithOpening = proceed
        }
        view { showOpeningProgress(true) }
        executor.signal("messageOpenDone")
    }
}