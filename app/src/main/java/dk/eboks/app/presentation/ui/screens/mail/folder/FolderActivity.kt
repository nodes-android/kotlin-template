package dk.eboks.app.presentation.ui.screens.mail.folder

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.include_toolnar.*
import javax.inject.Inject

class FolderActivity : MainNavigationBaseActivity(), FolderContract.View {
    @Inject lateinit var presenter: FolderContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@FolderActivity)
    }

    override fun onShake() {
        if(showEmptyState)
        {
        }
        else
        {
        }
    }

    override fun setupTranslations() {
        toolbarTv.visibility = View.GONE
        toolbarLargeTv.visibility = View.VISIBLE
        toolbarLargeTv.text = Translation.folders.foldersHeader

    }


}
