package dk.eboks.app.presentation.ui.components.message.viewers.image

import dk.eboks.app.domain.managers.AppStateManager
import dk.nodes.arch.presentation.base.BasePresenterImpl
import javax.inject.Inject

/**
 * Created by bison on 20-05-2017.
 */
class ImageViewComponentPresenter @Inject constructor(val appState: AppStateManager) : ImageViewComponentContract.Presenter, BasePresenterImpl<ImageViewComponentContract.View>() {

    init {
        appState.state?.currentViewerFileName?.let { filename->
            runAction { v->
                v.showImage(filename)
            }
        }
    }

}