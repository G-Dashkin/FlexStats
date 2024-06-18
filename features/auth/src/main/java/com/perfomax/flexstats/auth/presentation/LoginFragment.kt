package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.perfomax.auth.R
import com.perfomax.auth.databinding.FragmentLoginBinding
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.auth.di.AuthFeatureDepsProvider
import com.perfomax.flexstats.auth.di.DaggerAuthComponent
import com.perfomax.flexstats.core.navigation.Router
import javax.inject.Inject

class LoginFragment: Fragment(R.layout.fragment_login) {

    companion object {
        fun getInstance(): LoginFragment = LoginFragment()
    }

    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var vmFactory: LoginViewModelFactory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi

    private val loginViewModel by viewModels<LoginViewModel> {
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
        binding = FragmentLoginBinding.bind(view)
        binding.apply {
            loginButton.setOnClickListener { onLogin() }
            registerText.setOnClickListener { loginViewModel.toRegisterClicked() }
            resetText.setOnClickListener { loginViewModel.toResetClicked() }
        }
        setScreen()
    }

    private fun onLogin() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        loginViewModel.onLoginClicked(email = email, password = password)
    }

    // navigation space-----------------------------------------------------------------------------
    private fun setScreen() {
        loginViewModel.loginScreen.observe(viewLifecycleOwner) {
            when(it) {
                is LoginScreen.Login -> showHomeScreen()
                is LoginScreen.Register -> showRegisterScreen()
                is LoginScreen.Reset -> showResetScreen()
                is LoginScreen.EmailNotExists -> emailNotExists()
                is LoginScreen.PasswordNotCorrect -> passwordNotCorrect()
                is LoginScreen.EmptyFields -> emptyFields()
                is LoginScreen.Back -> toBackFragment()
            }
        }
    }

    private fun showHomeScreen() {
        parentFragmentManager.setFragmentResult("callMenuListener", bundleOf())
        router.navigateTo(
            fragment = homeFeatureApi.open(),
            addToBackStack = false
        )
    }

    private fun showRegisterScreen() {
        router.navigateTo(
            fragment = authFeatureApi.openRegister(),
            addToBackStack = true
        )
    }

    private fun showResetScreen() {
        router.navigateTo(
            fragment = authFeatureApi.openReset(),
            addToBackStack = true
        )
    }

    private fun toBackFragment(){
        router.back()
    }

    // Toast space-----------------------------------------------------------------------------
    private fun emailNotExists(){
        Toast.makeText(activity, com.perfomax.ui.R.string.email_user_exists, Toast.LENGTH_LONG).show()
    }
    private fun passwordNotCorrect(){
        Toast.makeText(activity, com.perfomax.ui.R.string.incorrect_password, Toast.LENGTH_LONG).show()
    }
    private fun emptyFields(){
        Toast.makeText(activity, com.perfomax.ui.R.string.empty_fields, Toast.LENGTH_LONG).show()
    }
}