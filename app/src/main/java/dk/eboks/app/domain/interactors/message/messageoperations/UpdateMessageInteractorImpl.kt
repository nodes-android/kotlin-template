package dk.eboks.app.domain.interactors.message.messageoperations

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import timber.log.Timber

class UpdateMessageInteractorImpl(executor: Executor, val messagesRepository: MessagesRepository) :
    BaseInteractor(executor),
    UpdateMessageInteractor {
    override var input: UpdateMessageInteractor.Input? = null
    override var output: UpdateMessageInteractor.Output? = null

    override fun execute() {
        try {
            input?.messagePatch?.let { messagePatch ->
                input?.messages?.let { messages ->
                    for (msg in messages) {
                        messagesRepository.updateMessage(msg, messagePatch)
                        runOnUIThread {
                            output?.onUpdateMessageSuccess()
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            runOnUIThread {
                Timber.e(t)
                output?.onUpdateMessageError(exceptionToViewError(t))
            }
        }
    }
}