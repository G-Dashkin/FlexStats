package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.perfomax.auth.R
import com.perfomax.auth.databinding.FragmentLoginBinding
import com.perfomax.auth.databinding.FragmentRegisterBinding
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.auth.di.AuthFeatureDepsProvider
import com.perfomax.flexstats.auth.di.DaggerAuthComponent
import com.perfomax.flexstats.core.navigation.Router
import javax.inject.Inject

class RegisterFragment: Fragment(R.layout.fragment_register) {

    companion object {
        fun getInstance(): RegisterFragment = RegisterFragment()
    }

    private lateinit var binding: FragmentRegisterBinding

    @Inject
    lateinit var vmFactory: RegisterViewModelFactory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    private val registerViewModel by viewModels<RegisterViewModel> {
        vmFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val homeComponent = DaggerAuthComponent
            .builder()
            .addDeps(AuthFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        binding.apply {
            registerButton.setOnClickListener { onRegister() }
            toLoginText.setOnClickListener { registerViewModel.showLoginScreen() }
        }
        setScreen()
    }

    private fun onRegister() {
        val email = binding.email.text.toString()
        val user = binding.user.text.toString()
        val password = binding.password.text.toString()
        registerViewModel.onRegisterClicked(email = email, user = user, password = password)
//        router.navigateTo(homeFeatureApi.open())
    }

    // navigation space-----------------------------------------------------------------------------
    private fun setScreen() {
        registerViewModel.registerScreen.observe(viewLifecycleOwner) {
            when(it) {
                is RegisterScreen.Login -> onRegister()
                is RegisterScreen.Back -> toBackFragment()
                else -> {}
            }
        }
    }

    private fun showLoginScreen() {
        router.navigateTo(
            fragment = authFeatureApi.openLogin(),
            addToBackStack = true
        )
    }

    private fun toBackFragment(){
        router.back()
    }
}