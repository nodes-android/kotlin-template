package dk.eboks.app.presentation.ui.channels.components.content.ekey.detail

import dk.eboks.app.domain.models.channel.ekey.BaseEkey
import dk.eboks.app.presentation.base.BaseView
import dk.nodes.arch.presentation.base.BasePresenter

/**
 * Created by bison on 07-11-2017.
 */
interface EkeyDetailComponentContract {
    interface View : BaseView {
        fun onSuccess()

    }

    interface Presenter : BasePresenter<View> {
        fun putVault(items: MutableList<BaseEkey>, item: BaseEkey)
    }
}