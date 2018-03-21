package dk.eboks.app.presentation.ui.screens.senders.detail

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.domain.models.sender.Sender
import dk.eboks.app.presentation.base.BaseActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_senders_detail.*
import kotlinx.android.synthetic.main.fragment_profile_main_component.*
import java.util.*
import timber.log.Timber
import javax.inject.Inject

class SenderDetailActivity : BaseActivity(), SenderDetailContract.View {
    var onLanguageChangedListener: (Locale) -> Unit = {
        setupTranslations()
    }

    @Inject
    lateinit var presenter: SenderDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_senders_detail)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val sender = intent.getSerializableExtra(Sender::class.simpleName) as Sender?
        if (sender == null) {
            finish()
        } else {
            updateHeader(sender)

            // pass the knowledge on to your siblings, so they in turn can use it
            val b = Bundle()
            b.putSerializable(Sender::class.simpleName, sender)

            senderGroupsListComponentF.arguments = b
            senderDetailInfoF.arguments = b

            presenter.loadSender(sender.id)

            //translations
            NStack.addLanguageChangeListener(onLanguageChangedListener)
            senderDetailRegisterTB.text = Translation.senderdetails.register
            senderDetailRegisterTB.textOn = Translation.senderdetails.register
            senderDetailRegisterTB.textOff = Translation.senderdetails.registeredTypeYes
        }

        senderDetailBodyTv.visibility = View.GONE // only for public authorities

        senderDetailTB.setNavigationOnClickListener {
            finish()
        }

        senderDetailCTL.isTitleEnabled = false

        senderDetailABL.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (appBarLayout.totalScrollRange + verticalOffset < 200) {
                senderDetailTB.title = sender!!.name
            } else {
                senderDetailTB.title = ""
            }
        }

        senderDetailRegisterTB.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_48_checkmark_white, 0)
            } else {
                buttonView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            }
        }

    }

    override fun setupTranslations() {

    }

    override fun showSender(sender: Sender) {
        // pass the knowledge on to your siblings, so they in turn can use it
        val b = Bundle()
        b.putSerializable(Sender::class.simpleName, sender)

        senderGroupsListComponentF.arguments = b
        senderDetailInfoF.arguments = b

        // also update the header
        updateHeader(sender)
    }

    private fun updateHeader(sender : Sender) {
        senderDetailTB.title = sender.name
        senderDetailNameTv.text = sender.name

        Glide.with(this)
                .load(sender.logo?.url)
                .apply(RequestOptions()
                        .fallback(R.drawable.icon_72_senders_private)
                        .placeholder(R.drawable.icon_72_senders_private)
                )
                .into(senderDetailIv)
    }

    override fun showError(msg: String) {
    }

    override fun onDestroy() {
        NStack.removeLanguageChangeListener(onLanguageChangedListener)
        super.onDestroy()
    }
}

