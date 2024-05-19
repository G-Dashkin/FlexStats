package com.perfomax.flexstats.auth.api

import androidx.fragment.app.Fragment
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.auth.presentation.LoginFragment
import com.perfomax.flexstats.auth.presentation.RegisterFragment
import com.perfomax.flexstats.auth.presentation.ResetFragment
import javax.inject.Inject

class AuthFeatureImpl @Inject constructor() : AuthFeatureApi {
    override fun openLogin(): Fragment = LoginFragment.getInstance()
    override fun openRegister(): Fragment = RegisterFragment.getInstance()
    override fun openReset(): Fragment = ResetFragment.getInstance()
}
