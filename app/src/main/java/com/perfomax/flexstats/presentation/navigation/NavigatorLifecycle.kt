package com.perfomax.flexstats.presentation.navigation

interface NavigatorLifecycle {
    fun onCreate(holder: NavigatorHolder)
    fun onDestroy()
}