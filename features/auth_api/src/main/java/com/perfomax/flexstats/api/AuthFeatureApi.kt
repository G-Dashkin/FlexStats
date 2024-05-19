package com.perfomax.flexstats.api

import androidx.fragment.app.Fragment

interface AuthFeatureApi {
    fun openLogin(): Fragment
    fun openRegister(): Fragment
    fun openReset(): Fragment
}