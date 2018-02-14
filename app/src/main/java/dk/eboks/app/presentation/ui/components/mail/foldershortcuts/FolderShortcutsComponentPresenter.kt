package dk.eboks.app.presentation.ui.components.mail.foldershortcuts

import android.arch.lifecycle.Lifecycle
import dk.eboks.app.domain.interactors.GetCategoriesInteractor
import dk.eboks.app.domain.managers.AppStateManager
import dk.eboks.app.domain.models.Folder
import dk.nodes.arch.presentation.base.BasePresenterImpl
import org.greenrobot.eventbus.EventBus
import timber.log.Timber
import javax.inject.Inject
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe



/**
 * Created by bison on 20-05-2017.
 */
class FolderShortcutsComponentPresenter @Inject constructor(val appState: AppStateManager, val getCategoriesInteractor: GetCategoriesInteractor) :
        FolderShortcutsComponentContract.Presenter,
        BasePresenterImpl<FolderShortcutsComponentContract.View>(),
        GetCategoriesInteractor.Output {

    init {
        refresh()
    }

    override fun onViewCreated(view: FolderShortcutsComponentContract.View, lifecycle: Lifecycle) {
        super.onViewCreated(view, lifecycle)
        EventBus.getDefault().register(this)
    }

    override fun onViewDetached() {
        EventBus.getDefault().unregister(this)
        super.onViewDetached()
    }

    override fun setCurrentFolder(folder: Folder) {
        appState.state?.currentFolder = folder
        appState.save()
    }


    override fun onGetCategories(folders: List<Folder>) {
        Timber.e("Received them folders")
        runAction { v ->
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
            v.showFolders(folders)
        }
    }

    override fun onGetCategoriesError(msg: String) {
        runAction { v->
            EventBus.getDefault().post(RefreshFolderShortcutsDoneEvent())
        }
        Timber.e(msg)
    }

    fun refresh() {
        getCategoriesInteractor.output = this
        getCategoriesInteractor.input = GetCategoriesInteractor.Input(false)
        getCategoriesInteractor.run()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: RefreshFolderShortcutsEvent) {
        refresh()
    }

}