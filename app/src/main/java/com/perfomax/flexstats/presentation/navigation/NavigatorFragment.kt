package com.perfomax.flexstats.presentation.navigation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isNotEmpty
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
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.databinding.FragmentNavigatorBinding
import com.perfomax.flexstats.di.DaggerProvider
import com.perfomax.flexstats.domain.usecases.GetAuthUserUseCase
import com.perfomax.flexstats.domain.usecases.LogoutUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetUserProjectsUseCase
import com.perfomax.flexstats.projects.domain.usecases.GetSelectedProjectUseCase
import com.perfomax.flexstats.start.domain.usecases.GetAuthUseCase
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

    @Inject
    lateinit var getAuthUseCase: GetAuthUseCase

    @Inject
    lateinit var getSelectedProjectUseCase: GetSelectedProjectUseCase

    @Inject
    lateinit var getUserProjectsUseCase: GetUserProjectsUseCase

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: FragmentNavigatorBinding
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

        toggle = ActionBarDrawerToggle((activity as AppCompatActivity?),
            binding.drawerLayout,
            binding.materialToolbar,
            com.perfomax.ui.R.string.open,
            com.perfomax.ui.R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)

        binding.navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> {
                    router.navigateTo(fragment = homeFeatureApi.open())
//                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
//                    binding.linearLayout3.visibility = View.VISIBLE
                }
                R.id.nav_projects -> router.navigateTo(fragment = projectsFeatureApi.open())
                R.id.nav_accounts -> router.navigateTo(fragment = accountsFeatureApi.open())
                R.id.nav_logout -> {
                    lifecycleScope.launch { logoutUseCase.execute() }
                    router.navigateTo(fragment = authFeatureApi.openLogin())
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    binding.materialToolbar.visibility = View.GONE
                }
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        setActionBarSettings()
        childFragmentManager.setFragmentResultListener("callMenuListener", viewLifecycleOwner){ _, _ ->
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

    private fun setActionBarSettings(){
        lifecycleScope.launch {

            val userIsAuth = getAuthUseCase.execute()
            val allProjects = getUserProjectsUseCase.execute()

            if (userIsAuth) {
                binding.materialToolbar.visibility = View.VISIBLE
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                binding.materialToolbar.visibility = View.GONE
//                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }

            if (allProjects.isNotEmpty()) {
                val authUser = getAuthUserUseCase.execute()
                val selectedProject = getSelectedProjectUseCase.execute()
                binding.materialToolbar.title = "Проект: ${selectedProject.name}"
            } else {
                binding.materialToolbar.title = ""
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