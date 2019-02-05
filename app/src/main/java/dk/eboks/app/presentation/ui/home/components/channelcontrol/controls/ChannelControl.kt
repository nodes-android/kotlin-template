package dk.eboks.app.presentation.ui.home.components.channelcontrol.controls

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import dk.eboks.app.R
import dk.eboks.app.domain.models.channel.Channel
import dk.eboks.app.domain.models.home.Control
import java.util.Observable

abstract class ChannelControl(
    val channel: Channel,
    val control: Control,
    val view: View,
    val inflater: LayoutInflater,
    val handler: Handler
) : Observable() {
    var logoIv: ImageView
    var progressPb: ProgressBar
    var headerTv: TextView
    var rowsContainerLl: LinearLayout

    init {
        logoIv = view.findViewById(R.id.logoIv)
        progressPb = view.findViewById(R.id.progressPb)
        headerTv = view.findViewById(R.id.headerTv)
        rowsContainerLl = view.findViewById(R.id.rowsContainerLl)
    }

    fun showProgress(show: Boolean) {
        logoIv.visibility = if (show) View.GONE else View.VISIBLE
        progressPb.visibility = if (!show) View.GONE else View.VISIBLE
    }

    abstract fun buildView()
}