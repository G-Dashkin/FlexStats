package com.perfomax.flexstats.presentation.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.perfomax.flexstats.R
import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.api.AuthFeatureApi
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.api.ProjectsFeatureApi
import com.perfomax.flexstats.api.StartFeatureApi
import com.perfomax.flexstats.core.contracts.CALL_MENU_LISTENER
import com.perfomax.flexstats.core.contracts.EMPTY
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.core.utils.getFragmentName
import com.perfomax.flexstats.databinding.FragmentNavigatorBinding
import com.perfomax.flexstats.di.DaggerProvider
import com.perfomax.flexstats.domain.usecases.GetAuthUserUseCase
import com.perfomax.flexstats.domain.usecases.LogoutUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetSelectedProjectUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetUserProjectsUseCase
import com.perfomax.flexstats.start.domain.usecases.GetAuthUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

private val START_SCREENS = listOf("StartFragment", "LoginFragment", "RegisterFragment", "ResetFragment")

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

    @Inject
    lateinit var getAuthUseCase: GetAuthUseCase

    @Inject
    lateinit var getSelectedProjectUseCase: GetSelectedProjectUseCase

    @Inject
    lateinit var getUserProjectsUseCase: GetUserProjectsUseCase

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: FragmentNavigatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerProvider.appComponent.inject(this)
        navigatorLifecycle.onCreate(this)
        router.navigateTo(fragment = startFeatureApi.open())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNavigatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggle = ActionBarDrawerToggle(requireActivity(),
                                       binding.drawerLayout,
                                       binding.materialToolbar,
                                       com.perfomax.ui.R.string.open,
                                       com.perfomax.ui.R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> router.navigateTo(fragment = homeFeatureApi.open())
                R.id.nav_projects -> router.navigateTo(fragment = projectsFeatureApi.open())
                R.id.nav_accounts -> router.navigateTo(fragment = accountsFeatureApi.openDirectList())
                R.id.nav_logout -> {
                    router.navigateTo(fragment = authFeatureApi.openLogin())
                    lifecycleScope.launch {
                        logoutUseCase.execute()
                        binding.materialToolbar.visibility = View.GONE
                        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        setActionBarSettings()
        childFragmentManager.setFragmentResultListener(CALL_MENU_LISTENER, viewLifecycleOwner){ _, _ ->
            setActionBarSettings()
        }

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

    private fun setActionBarSettings() {

        lifecycleScope.launch {
            val allProjects = getUserProjectsUseCase.execute()
            val authUser = getAuthUserUseCase.execute()
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.user_name).text = authUser.user
            binding.navView.getHeaderView(0).findViewById<TextView>(R.id.user_email).text = authUser.email
            toolbarSettings()

            if (allProjects.isNotEmpty()) {
                val selectedProject = getSelectedProjectUseCase.execute()
                binding.materialToolbar.title = "${resources.getString(com.perfomax.ui.R.string.project)} " +
                                                   selectedProject.name
            } else {
                binding.materialToolbar.title = EMPTY
            }
        }
    }

    private fun toolbarSettings(){
        lifecycleScope.launch {
            binding.materialToolbar.visibility = View.GONE
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            var currentFragmentName = childFragmentManager.getFragmentName()
            if (currentFragmentName !in START_SCREENS){
                binding.materialToolbar.visibility = View.VISIBLE
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                while (currentFragmentName in START_SCREENS) {
                    currentFragmentName = childFragmentManager.getFragmentName()
                    if (currentFragmentName !in START_SCREENS) {
                        binding.materialToolbar.visibility = View.VISIBLE
                        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    }
                    delay(100)
                }
            }
        }
    }

    override fun onDestroy() {
        navigatorLifecycle.onDestroy()
        super.onDestroy()
    }

    override fun manager(): FragmentManager = childFragmentManager

    override fun context(): Context = requireActivity()
}