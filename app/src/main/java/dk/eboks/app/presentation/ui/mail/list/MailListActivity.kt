package dk.eboks.app.presentation.ui.mail.list

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import dk.eboks.app.R
import dk.eboks.app.domain.models.Attachment
import dk.eboks.app.domain.models.Message
import dk.eboks.app.domain.models.Sender
import dk.eboks.app.injection.components.DaggerPresentationComponent
import dk.eboks.app.injection.components.PresentationComponent
import dk.eboks.app.injection.modules.PresentationModule
import dk.eboks.app.presentation.base.MainNavigationBaseActivity
import dk.eboks.app.presentation.ui.message.MessageActivity
import dk.eboks.app.presentation.ui.message.sheet.MessageSheetActivity
import dk.nodes.nstack.kotlin.NStack
import kotlinx.android.synthetic.main.activity_mail_list.*
import kotlinx.android.synthetic.main.include_toolnar.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MailListActivity : MainNavigationBaseActivity(), MailListContract.View {
    @Inject lateinit var presenter: MailListContract.Presenter

    var messages: MutableList<Message> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_list)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupRecyclerView()

        refreshSrl.setOnRefreshListener {
            presenter.refresh()
        }
    }

    override fun onResume() {
        super.onResume()
        NStack.translate(this@MailListActivity)
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
        toolbarLargeTv.text = "_All mail"
    }

    fun setupRecyclerView()
    {
        messagesRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        messagesRv.adapter = MessageAdapter()
    }


    override fun showFolderName(name: String) {
        toolbarLargeTv.text = name
    }

    override fun showError(msg: String) {
        Log.e("debug", msg)
    }


    override fun showMessages(messages: List<Message>) {
        this.messages.clear()
        this.messages.add(Message(0, "Police 42342342", false, Date(), Sender(0, "Alka Forsikring"), null, listOf(Attachment(1, "Sorteper.pdf", "13 KB"), Attachment(1, "HalvgrønneBent.pdf", "2,7 MB"), Attachment(1, "SvartaGudrun.pdf", "236 KB"), Attachment(1, "Lyserøde Lars.pdf", "1,2 MB"))))
        this.messages.add(Message(1, "Kontoudskrift", false, Date(), Sender(0, "Danske Bank"), null, listOf(Attachment(1, "Pels for begyndere.pdf", "13 KB"), Attachment(1, "alverdens_sten.pdf", "236 KB"))))
        this.messages.addAll(messages)
        messagesRv.adapter.notifyDataSetChanged()
    }
    /*
    override fun showSenders(messages: List<Sender>) {
        this.messages.addAll(messages)
        sendersRv.adapter.notifyDataSetChanged()
    }
    */


    override fun showRefreshProgress(show: Boolean) {
            refreshSrl.isRefreshing = show
    }


    inner class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

        inner class MessageViewHolder(val root : View) : RecyclerView.ViewHolder(root)
        {
            val circleIv = root.findViewById<ImageView>(R.id.circleIv)
            val titleTv = root.findViewById<TextView>(R.id.titleTv)
            val subTitleTv = root.findViewById<TextView>(R.id.subTitleTv)
            val urgentTv = root.findViewById<TextView>(R.id.urgentTv)
            val dateTv = root.findViewById<TextView>(R.id.dateTv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val v = LayoutInflater.from(this@MailListActivity).inflate(R.layout.viewholder_message, parent, false)
            val vh = MessageViewHolder(v)
            return vh
        }

        override fun getItemCount(): Int {
            return messages.size
        }

        override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
            if(messages[position].sender != null) {
                holder?.circleIv?.let {

                    Glide.with(this@MailListActivity).load(messages[position].sender?.imageUrl).into(it)
                    it.isSelected = !messages[position].isRead
                }
            }
            holder?.titleTv?.text = messages[position].sender?.name
            holder?.subTitleTv?.text = messages[position].name
            holder?.root?.setOnClickListener {
                Timber.e("supposed to launch")
                presenter.setCurrentMessage(messages[position])
                startActivity(Intent(this@MailListActivity, MessageSheetActivity::class.java))
            }
            holder?.root?.setOnLongClickListener {
                Timber.e("supposed to launch")
                presenter.setCurrentMessage(messages[position])
                startActivity(Intent(this@MailListActivity, MessageActivity::class.java))
                true
            }
        }
    }
}