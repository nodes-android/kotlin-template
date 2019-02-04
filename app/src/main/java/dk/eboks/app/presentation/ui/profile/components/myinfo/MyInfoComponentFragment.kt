package dk.eboks.app.presentation.ui.profile.components.myinfo

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.EmailVerificationComponentFragment
import dk.eboks.app.presentation.ui.profile.components.drawer.PhoneVerificationComponentFragment
import dk.eboks.app.presentation.ui.profile.components.main.ProfileInfoComponentFragment
import dk.eboks.app.util.isValidEmail
import dk.eboks.app.util.visible
import dk.nodes.nstack.kotlin.NStack
import dk.nodes.nstack.kotlin.util.OnLanguageChangedListener
import kotlinx.android.synthetic.main.fragment_profile_myinformation_component.*
import kotlinx.android.synthetic.main.include_toolbar.*
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

class MyInfoComponentFragment : BaseFragment(), MyInfoComponentContract.View,
    OnLanguageChangedListener, TextWatcher {
    @Inject
    lateinit var presenter: MyInfoComponentContract.Presenter
    var menuSave: MenuItem? = null

    // val mobilenumber: ContactPoint = ContactPoint()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_myinformation_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupTopBar()
        newsletterSw.text = Translation.myInformation.newsletter
        onLanguageChanged(NStack.language)
        presenter.setup()
    }

    // shamelessly ripped from chnt
    private fun setupTopBar() {
        mainTb.setNavigationIcon(R.drawable.icon_48_chevron_left_red_navigationbar)
        mainTb.setNavigationOnClickListener {
            hideKeyboard()
            menuSave?.let { save ->
                if (save.isEnabled) {
                    showUnsavedChangesDialog()
                    return@setNavigationOnClickListener
                }
            }
            fragmentManager?.popBackStack()
        }

        menuSave = mainTb.menu.add(Translation.defaultSection.save)
        menuSave?.isEnabled = false
        menuSave?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menuSave?.setOnMenuItemClickListener { item: MenuItem ->
            hideKeyboard()
            presenter.save()
            ProfileInfoComponentFragment.refreshOnResume = true
            true
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun attachListeners() {
        nameEt.addTextChangedListener(this)
        primaryMailEt.addTextChangedListener(this)
        secondaryMailEt.addTextChangedListener(this)
        mobilEt.addTextChangedListener(this)
        newsletterSw.setOnCheckedChangeListener { compoundButton, b ->
            afterTextChanged(null)
        }

        verifyEmailBtn.setOnClickListener {
            menuSave?.let { save ->
                if (save.isEnabled) {
                    showMustSaveDialog()
                    return@setOnClickListener
                }
            }
            if (primaryMailEt.text.isValidEmail()) {
                val args = Bundle()
                args.putString("mail", primaryMailEt.text.toString())
                getBaseActivity()?.openComponentDrawer(
                    EmailVerificationComponentFragment::class.java,
                    args
                )
            }
        }

        verifySecondaryEmailBtn.setOnClickListener {
            menuSave?.let { save ->
                if (save.isEnabled) {
                    showMustSaveDialog()
                    return@setOnClickListener
                }
            }
            if (secondaryMailEt.text.isValidEmail()) {
                val args = Bundle()
                args.putString("mail", secondaryMailEt.text.toString())
                getBaseActivity()?.openComponentDrawer(
                    EmailVerificationComponentFragment::class.java,
                    args
                )
            }
        }

        verifyMobileNumberBtn.setOnClickListener {
            menuSave?.let { save ->
                if (save.isEnabled) {
                    showMustSaveDialog()
                    return@setOnClickListener
                }
            }
            if (!mobilEt.text.isNullOrEmpty()) {
                val args = Bundle()
                args.putString("mobile", mobilEt.text.toString())
                getBaseActivity()?.openComponentDrawer(
                    PhoneVerificationComponentFragment::class.java,
                    args
                )
            }
        }

        getBaseActivity()?.backPressedCallback = {
            if (menuSave?.isEnabled == true) {
                showUnsavedChangesDialog()
            } else {
                activity?.onBackPressed()
            }
            true
        }

        updateVerifyButtonVisibility()
    }

    private fun showMustSaveDialog() {
        AlertDialog.Builder(activity ?: return)
            .setTitle(Translation.myInformation.saveBeforeVerifyTitle)
            .setMessage(Translation.myInformation.saveBeforeVerifyMessage)
            .setPositiveButton(Translation.myInformation.unsavedChangesSaveButton) { dialog, which ->
                ProfileInfoComponentFragment.refreshOnResume = true
                presenter.save(false)
            }
            .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
            }
            .show()
    }

    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(activity ?: return)
            .setTitle(Translation.myInformation.unsavedChangesTitle)
            .setMessage(Translation.myInformation.unsavedChangesMessage)
            .setPositiveButton(Translation.myInformation.unsavedChangesSaveButton) { dialog, which ->
                ProfileInfoComponentFragment.refreshOnResume = true
                presenter.save(false)
            }
            .setNegativeButton(Translation.defaultSection.cancel) { dialog, which ->
                fragmentManager?.popBackStack()
            }
            .show()
    }

    private fun detachListeners() {
        getBaseActivity()?.backPressedCallback = null
        nameEt.removeTextChangedListener(this)
        primaryMailEt.removeTextChangedListener(this)
        secondaryMailEt.removeTextChangedListener(this)
        mobilEt.removeTextChangedListener(this)
    }

    private fun updateVerifyButtonVisibility() {
        verifyEmailBtn.setVerifiedButtonVissible(primaryMailEt)
        verifySecondaryEmailBtn.setVerifiedButtonVissible(secondaryMailEt)
        verifyMobileNumberBtn.setVerifiedButtonVissible(mobilEt)
    }

    override fun onDone() {
        fragmentManager?.popBackStack()
    }

    override fun onResume() {
        super.onResume()
        NStack.addLanguageChangeListener(this)
        attachListeners()
        if (refreshOnResume) {
            refreshOnResume = false
            presenter.refresh()
        }
    }

    override fun onPause() {
        NStack.removeLanguageChangeListener(this)
        detachListeners()
        super.onPause()
    }

    override fun onLanguageChanged(locale: Locale) {
        mainTb.title = Translation.myInformation.title

        nameEt.floatingLabelText = Translation.myInformation.name
        primaryMailEt.setHelperText(Translation.myInformation.notifyByEmailText)
        primaryMailEt.floatingLabelText = Translation.myInformation.primaryEmail

        secondaryMailEt.floatingLabelText = Translation.myInformation.secondaryEmail

        mobilEt.setHelperText(Translation.myInformation.notifyBySMSText)
        mobilEt.floatingLabelText = Translation.myInformation.mobileNumber
        menuSave?.title = Translation.defaultSection.save
    }

    override fun setName(name: String) {
        nameEt.setText(name)
    }

    override fun setPrimaryEmail(email: String, verified: Boolean, userVerified: Boolean) {
        primaryMailEt.setText(email)
        verifyEmailBtn.tag = verified
        verifyEmailBtn.visible = (!verified)
        primaryMailEt.isEnabled = userVerified
        updateVerifyButtonVisibility()
    }

    override fun setSecondaryEmail(email: String, verified: Boolean) {
        secondaryMailEt.setText(email)
        verifyMobileNumberBtn.tag = verified
        updateVerifyButtonVisibility()
        verifySecondaryEmailBtn.visible = (!verified)
    }

    override fun setMobileNumber(mobile: String, verified: Boolean) {
        mobilEt.setText(mobile)
        // mobilenumber.value = mobile
        // mobilenumber.verified = verified
        Timber.e("SetMobileNumber mobile $mobile veri: $verified")
        verifyMobileNumberBtn.tag = verified
        updateVerifyButtonVisibility()
        verifyMobileNumberBtn.visible = (!verified)
    }

    override fun setNewsletter(b: Boolean) {
        newsletterSw.isChecked = b
    }

    override fun getName(): String {
        return nameEt.text.toString().trim()
    }

    override fun getPrimaryEmail(): String {
        return primaryMailEt.text.toString().trim()
    }

    override fun getSecondaryEmail(): String {
        return secondaryMailEt.text.toString().trim()
    }

    override fun getMobileNumber(): String {
        return mobilEt.text.toString().trim()
    }

    override fun getNewsletter(): Boolean {
        return newsletterSw.isChecked
    }

    override fun showProgress(show: Boolean) {
        progressFl.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun showSecondaryEmail(show: Boolean) {
        secondaryMailFl.visible = (show)
    }

    override fun setSaveEnabled(enabled: Boolean) {
        menuSave?.isEnabled = enabled
    }

    override fun setNeutralFocus() {
        focusThief.requestFocus()
    }

    override fun afterTextChanged(target: Editable?) {
        menuSave?.isEnabled = true
        updateVerifyButtonVisibility()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    companion object {
        var refreshOnResume = false
    }

    private fun Button.setVerifiedButtonVissible(input: EditText) {
        val verfiedTag = tag as? Boolean ?: false
        visible = !input.text.isNullOrBlank() && !verfiedTag
    }
}