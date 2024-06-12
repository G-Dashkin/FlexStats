package com.perfomax.flexstats.api

import androidx.fragment.app.Fragment

interface AccountsFeatureApi {
    fun openDirectList(): Fragment
    fun openMetrikaList(): Fragment
}