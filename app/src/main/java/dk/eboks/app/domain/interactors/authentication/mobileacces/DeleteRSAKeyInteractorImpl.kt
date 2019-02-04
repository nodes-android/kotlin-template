package dk.eboks.app.domain.interactors.authentication.mobileacces

import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.util.exceptionToViewError
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor

class DeleteRSAKeyInteractorImpl(executor: Executor, val cryptoManager: CryptoManager) :
    BaseInteractor(executor), DeleteRSAKeyInteractor {
    override var output: DeleteRSAKeyInteractor.Output? = null

    override fun execute() {
        try {
            cryptoManager.deleteAllActivations()
            runOnUIThread {
                output?.onDeleteRSAKeySuccess()
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onDeleteRSAKeyError(exceptionToViewError(t))
            }
        }
    }
}