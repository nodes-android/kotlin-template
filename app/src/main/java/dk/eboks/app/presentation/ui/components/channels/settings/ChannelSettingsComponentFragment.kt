package dk.eboks.app.presentation.ui.components.channels.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_channel_settings_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class ChannelSettingsComponentFragment : BaseFragment(), ChannelSettingsComponentContract.View {

    @Inject
    lateinit var presenter : ChannelSettingsComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_channel_settings_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
    }

    override fun setupTranslations() {
        headerTv.text = Translation.settings.header
        creditCardHeaderTv.text = Translation.settings.creditCardHeader
        removeChannelBtn.text = Translation.settings.removeChannelBtn
    }

}