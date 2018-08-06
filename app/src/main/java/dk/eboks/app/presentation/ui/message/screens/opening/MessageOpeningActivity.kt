package dk.eboks.app.presentation.ui.message.screens.opening

import android.os.Bundle
import android.view.View
import dk.eboks.app.R
import dk.eboks.app.domain.models.message.Message
import dk.eboks.app.presentation.base.BaseActivity
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.message.components.opening.protectedmessage.ProtectedMessageComponentFragment
import dk.eboks.app.util.guard
import dk.eboks.app.util.putArg
import kotlinx.android.synthetic.main.activity_message_opening.*
import javax.inject.Inject

class MessageOpeningActivity : BaseActivity(), MessageOpeningContract.View {
    @Inject lateinit var presenter: MessageOpeningContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dk.eboks.app.R.layout.activity_message_opening)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        intent?.extras?.getSerializable(Message::class.java.simpleName)?.let {
            presenter.setup(it as Message)
        }.guard { finish() }
    }

    override fun setOpeningFragment(cls: Class<out BaseFragment>, voluntaryReceipt : Boolean) {
        val fragment = cls.newInstance()
        progressPb.visibility = View.GONE
        fragment.putArg("voluntaryReceipt", voluntaryReceipt)
        fragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.contentFl, it, it::class.java.simpleName).commit()
        }
    }

    override fun showMessageLocked(loginProviderId: String) {
        val fragment = ProtectedMessageComponentFragment()
        fragment.putArg("loginProviderId", loginProviderId)
        fragment?.let{
            supportFragmentManager.beginTransaction().add(R.id.contentFl, it, it::class.java.simpleName).commit()
        }
        /*
        val intent = Intent(this, PopupLoginActivity::class.java).putExtra("verifyLoginProviderId", Config.getVerificationProviderId())
        startActivityForResult(intent, PopupLoginActivity.REQUEST_VERIFICATION)
        */
    }

    override fun onBackPressed() {
        presenter.signalMessageOpenDone()
        super.onBackPressed()
    }

    override fun getNavigationMenuAction(): Int { return R.id.actionMail }
}
