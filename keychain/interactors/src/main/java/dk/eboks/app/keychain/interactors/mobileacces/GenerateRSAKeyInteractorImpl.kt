package dk.eboks.app.keychain.interactors.mobileacces

import dk.eboks.app.domain.exceptions.InteractorException
import dk.eboks.app.domain.managers.CryptoManager
import dk.eboks.app.util.exceptionToViewError
import dk.eboks.app.util.guard
import dk.nodes.arch.domain.executor.Executor
import dk.nodes.arch.domain.interactor.BaseInteractor
import javax.inject.Inject

internal class GenerateRSAKeyInteractorImpl @Inject constructor(
    executor: Executor,
    private val cryptoManager: CryptoManager
) : BaseInteractor(executor), GenerateRSAKeyInteractor {
    override var input: GenerateRSAKeyInteractor.Input? = null
    override var output: GenerateRSAKeyInteractor.Output? = null

    override fun execute() {
        try {
            input?.let {
                cryptoManager.generateRSAKey(it.userId)
                val deviceActivation = cryptoManager.getActivation(it.userId)
                deviceActivation?.publicKey?.let { publicKey ->
                    val rsakey = cryptoManager.getPublicKeyAsString(publicKey)
                    runOnUIThread { output?.onGenerateRSAKeySuccess(rsakey) }
                }.guard {
                    throw IllegalArgumentException("rsa public key null")
                }
            }.guard {
                throw InteractorException("no args")
            }
        } catch (t: Throwable) {
            runOnUIThread {
                output?.onGenerateRSAKeyError(exceptionToViewError(t))
            }
        }
    }
}