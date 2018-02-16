package dk.eboks.app.domain.interactors.message

import dk.eboks.app.domain.models.Message
import dk.nodes.arch.domain.interactor.Interactor

/**
 * Created by bison on 01/02/18.
 */
interface OpenMessageInteractor : Interactor {
    var output : Output?
    var input : Input?

    data class Input(var msg : Message)

    interface Output {
        fun onOpenMessageDone()
        fun onOpenMessageError(msg : String)
    }
}