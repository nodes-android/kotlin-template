package dk.eboks.app.presentation.ui.components.start.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_signup_terms_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class TermsComponentFragment : BaseFragment(), SignupComponentContract.TermsView {

    @Inject
    lateinit var presenter : SignupComponentContract.Presenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_signup_terms_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        continueBtn.setOnClickListener { onContinueClicked() }
        setupTopBar()
    }

    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.red_navigationbar)
        mainTb.title = Translation.signup.termsTitle
        mainTb.setNavigationOnClickListener {
            fragmentManager.popBackStack()
        }
    }


    override fun setupTranslations() {
        textTv.text = Translation.signup.termsText
        continueBtn.text = Translation.signup.continueButton
    }

    override fun showError() {

    }

    override fun showProgress(show: Boolean) {
        content.visibility = if(show) View.GONE else View.VISIBLE
        progress.visibility = if(show) View.VISIBLE else View.GONE
    }

    fun onContinueClicked()
    {
        //(activity as StartActivity).showLogo(false)
        showProgress(true)
        content.postDelayed({
            //(activity as StartActivity).startMain()
        }, 1000)
    }
}