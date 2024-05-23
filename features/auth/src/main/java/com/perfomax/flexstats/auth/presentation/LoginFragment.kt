package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

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
            loginButton.setOnClickListener {
                router.navigateTo(homeFeatureApi.open())
            }
//            registerText.setOnClickListener {
//                router.navigateTo(
//                    fragment = authFeatureApi.openRegister(),
//                    addToBackStack = true
//                )
//            }
            registerText.setOnClickListener {
                loginViewModel.toRegisterClicked()
            }
            resetText.setOnClickListener {
                router.navigateTo(
                    fragment = authFeatureApi.openReset(),
                    addToBackStack = true
                )
            }
        }
        setScreen()
    }

    private fun setScreen() {
        loginViewModel.screen.observe(viewLifecycleOwner) {
            when(it) {
                is Screen.Login -> {

                }
                is Screen.Register -> showRegisterScreen()
                is Screen.Reset -> {

                }
                else -> {}
            }
        }
    }

    private fun showRegisterScreen() {
        router.navigateTo(
            fragment = authFeatureApi.openRegister(),
            addToBackStack = true
        )
    }

    companion object {
        fun getInstance(): LoginFragment = LoginFragment()
    }
}