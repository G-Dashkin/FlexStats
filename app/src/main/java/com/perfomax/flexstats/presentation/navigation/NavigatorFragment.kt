package com.perfomax.flexstats.presentation.navigation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.perfomax.flexstats.R
import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.api.ProjectsFeatureApi
import com.perfomax.flexstats.api.StartFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.databinding.FragmentNavigatorBinding
import com.perfomax.flexstats.di.DaggerProvider
import com.perfomax.flexstats.domain.usecases.GetAuthUserUseCase
import com.perfomax.flexstats.domain.usecases.LogoutUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class NavigatorFragment : Fragment(R.layout.fragment_navigator), NavigatorHolder {

    @Inject
    lateinit var navigatorLifecycle: NavigatorLifecycle

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var startFeatureApi: StartFeatureApi

    @Inject
    lateinit var authFeatureApi: AuthFeatureApi

    @Inject
    lateinit var homeFeatureApi: HomeFeatureApi

    @Inject
    lateinit var projectsFeatureApi: ProjectsFeatureApi

    @Inject
    lateinit var accountsFeatureApi: AccountsFeatureApi

    @Inject
    lateinit var logoutUseCase: LogoutUseCase

    @Inject
    lateinit var getAuthUserUseCase: GetAuthUserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerProvider.appComponent.inject(this)
        navigatorLifecycle.onCreate(this)
        router.navigateTo(fragment = startFeatureApi.open())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.nav_menu, menu)

                lifecycleScope.launch {
                    val authUser = getAuthUserUseCase.execute()
//                    menu.add(authUser.user).titleCondensed
                    (activity as AppCompatActivity?)!!.supportActionBar!!.title = "Пользователь: ${authUser.user}"
                }
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.nav_home -> router.navigateTo(fragment = homeFeatureApi.open())
                    R.id.nav_projects -> router.navigateTo(fragment = projectsFeatureApi.open())
                    R.id.nav_accounts -> router.navigateTo(fragment = accountsFeatureApi.open())
                    R.id.nav_logout -> {
                        lifecycleScope.launch{ logoutUseCase.execute() }
                        router.navigateTo(fragment = authFeatureApi.openLogin())
                    }
                }
                return true
            }
        }, viewLifecycleOwner)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (childFragmentManager.backStackEntryCount > 0) {
                        childFragmentManager.popBackStack()
                    } else {
                        requireActivity().finish()
                    }
                }
            }
        )


    }

    override fun onDestroy() {
        navigatorLifecycle.onDestroy()
        super.onDestroy()
    }

    override fun manager(): FragmentManager = childFragmentManager

    override fun context(): Context = requireActivity()
}