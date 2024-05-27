package com.perfomax.flexstats.auth.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.perfomax.auth.R
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
    }

    // navigation space-----------------------------------------------------------------------------
    private fun setScreen() {
        registerViewModel.registerScreen.observe(viewLifecycleOwner) {
            when(it) {
                is RegisterScreen.Login -> showLoginScreen()
                is RegisterScreen.Back -> toBackFragment()
                is RegisterScreen.EmailExists -> emailExists()
                is RegisterScreen.EmailNotCorrect -> emailNotCorrect()
                is RegisterScreen.EmptyFields -> emptyFields()
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

    // Toast space-----------------------------------------------------------------------------
    private fun emailExists(){
        Toast.makeText(activity, "Пользователь с таким email уже существует", Toast.LENGTH_LONG).show()
    }
    private fun emailNotCorrect(){
        Toast.makeText(activity, "Укажите корректный email", Toast.LENGTH_LONG).show()
    }
    private fun emptyFields(){
        Toast.makeText(activity, "Все поля дожны быть заполнены", Toast.LENGTH_LONG).show()
    }
}