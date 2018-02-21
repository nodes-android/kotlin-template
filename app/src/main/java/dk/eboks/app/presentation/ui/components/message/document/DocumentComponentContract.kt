package dk.eboks.app.presentation.ui.components.message.document

import dk.eboks.app.domain.models.Message
import dk.nodes.arch.presentation.base.BasePresenter
import dk.nodes.arch.presentation.base.BaseView

/**
 * Created by bison on 07-11-2017.
 */
interface DocumentComponentContract {
    interface View : BaseView {
        fun updateView(message : Message)
        fun openExternalViewer(filename: String, mimeType : String)
    }

    interface Presenter : BasePresenter<DocumentComponentContract.View> {
        fun openExternalViewer(message : Message)
    }
}