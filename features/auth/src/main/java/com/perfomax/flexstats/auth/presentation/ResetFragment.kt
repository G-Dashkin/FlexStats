package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.perfomax.auth.R
import com.perfomax.auth.databinding.FragmentResetBinding
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.auth.di.AuthFeatureDepsProvider
import com.perfomax.flexstats.auth.di.DaggerAuthComponent
import com.perfomax.flexstats.core.navigation.Router
import javax.inject.Inject

class ResetFragment: Fragment(R.layout.fragment_reset) {

    companion object {
        fun getInstance(): ResetFragment = ResetFragment()
    }

    private lateinit var binding: FragmentResetBinding

    @Inject
    lateinit var vmFactory: ResetViewModelFactory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    private val resetViewModel by viewModels<ResetViewModel> {
        vmFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeComponent = DaggerAuthComponent
            .builder()
            .addDeps(AuthFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentResetBinding.bind(view)
        binding.resetButton.setOnClickListener {
            resetViewModel.resetPassword(binding.email.text.toString())
        }
        binding.toLoginText.setOnClickListener {
            resetViewModel.showLoginScreen()
        }

        setScreen()
    }

    // navigation space-----------------------------------------------------------------------------
    private fun setScreen() {
        resetViewModel.resetScreen.observe(viewLifecycleOwner) {
            when(it) {
                is ResetScreen.Login -> showLoginScreen()
                is ResetScreen.Back -> toBackFragment()
                is ResetScreen.ResetPasswordSuccess -> passwordSendToEmail()
                is ResetScreen.ResetPasswordFailed -> emailNotExists()
            }
        }
    }

    private fun showLoginScreen() {
        router.navigateTo(
            fragment = authFeatureApi.openLogin(),
            addToBackStack = true
        )
    }

    // Toast space-----------------------------------------------------------------------------
    private fun emailNotExists() {
        Toast.makeText(activity, com.perfomax.ui.R.string.email_user_exists, Toast.LENGTH_LONG).show()
    }
    private fun passwordSendToEmail() {
        Toast.makeText(activity, com.perfomax.ui.R.string.password_send_to_email, Toast.LENGTH_LONG).show()
    }

    private fun toBackFragment() {
        router.back()
    }
}