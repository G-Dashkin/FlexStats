package com.perfomax.flexstats.core.navigation

import androidx.fragment.app.Fragment

interface Router {
    fun navigateTo(fragment: Fragment, addToBackStack: Boolean = false)
    fun back()
}