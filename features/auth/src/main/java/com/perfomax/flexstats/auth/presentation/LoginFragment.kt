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
                loginViewModel.onLoginClicked()
            }
            registerText.setOnClickListener {
                loginViewModel.toRegisterClicked()
            }
            resetText.setOnClickListener {
                loginViewModel.toResetClicked()
            }
        }
        setScreen()
    }

    // navigation space-----------------------------------------------------------------------------
    private fun setScreen() {
        loginViewModel.screen.observe(viewLifecycleOwner) {
            when(it) {
                is Screen.Login -> onLogin()
                is Screen.Register -> showRegisterScreen()
                is Screen.Reset -> showResetScreen()
                else -> {}
            }
        }
    }

    private fun onLogin() {
        router.navigateTo(homeFeatureApi.open())
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
}

//логика работы навигации:

//Уровень фрагмента
//1) Во фрагменте вызывается метод из вьюМодели для перехода
//
//Уровень вьюМодели
//2) Метод устанавливает во вьюМоделе значение экрана на который нужно перейти в переменную screen
//-Переменная screen является лайфДатой sealed-класса содержащего все варианты экрнов для перехода
//
//Уровень фрагмента
//3) У нас есть метод с навигацией фрагмента navigateTo(), который  меняет один фрагмент на другой.
//-В принципе этот метод мы могли бы вызыватать напрямую, но гугл рекомендует, что в
//MVVM моделе, нужно сначала менять значение во вьюМодели и затем вызывать нужный метод
//навигации, поэтому получилась такая зацикленность.
//
//4) И для вызова этого метода навигации у нас используется та самая лайфДата из вью модели, которая
//отслеживает изменения экранов в переменной screen. В зависимости от выбранного экрана у нас вызывается
//один из методов для перехода/управления фрагментами