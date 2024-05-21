package com.perfomax.flexstats.presentation.navigation

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.perfomax.flexstats.R
import com.perfomax.flexstats.core.navigation.Router

class RouterImpl: Router, NavigatorLifecycle {

    private var navigatorHolder: NavigatorHolder? = null

    override fun onCreate(holder: NavigatorHolder) {
        navigatorHolder = holder
    }

    override fun onDestroy() {
        navigatorHolder = null
    }

    override fun navigateTo(fragment: Fragment, addToBackStack: Boolean) {
        Log.d("MyLog", "fragment in navigateTo $fragment")
        Log.d("MyLog", "navigatorHolder? $navigatorHolder")
        Log.d("MyLog", "navigatorHolder?.manager() ${navigatorHolder?.manager()}")
        val manager = navigatorHolder?.manager() ?: throw IllegalArgumentException("NavigationHolder is null")
        when {
            addToBackStack -> manager.commit {
                addToBackStack(null)
                replace(R.id.main_fragment_container, fragment)
            }
            else -> manager.commit {
                replace(R.id.main_fragment_container, fragment)
            }
        }
    }

    override fun back() {
        val manager = navigatorHolder?.manager() ?: throw IllegalArgumentException("NavigationHolder is null")
        manager.popBackStack()
    }
}