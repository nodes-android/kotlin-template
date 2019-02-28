package dk.eboks.app.domain.interactors.message.payment

import dk.eboks.app.domain.repositories.MessagesRepository
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import java.lang.Exception

class GetPaymentDetailsInteractorImpl(
        executor: Executor,
        private val repository: MessagesRepository)
    : BaseInteractor(executor),
        GetPaymentDetailsInteractor {

    override var input: GetPaymentDetailsInteractor.Input? = null
    override var output: GetPaymentDetailsInteractor.Output? = null

    override fun execute() {
        input?.let {
            try {
                val result = repository.getPaymentDetails(it.folderId, it.messageId)
                output?.onPaymentDetailsLoaded(result)
            } catch (e: Exception) {
                output?.onPaymentDetailsLoadingError(exceptionToViewError(e))
            }
        }
    }

}