package com.perfomax.flexstats.accounts.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.FragmentAccountsBinding
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.accounts.di.DaggerAccountsComponent
import com.perfomax.flexstats.core.navigation.Router
import javax.inject.Inject

class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private lateinit var binding: FragmentAccountsBinding

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeComponent = DaggerAccountsComponent
            .builder()
            .addDeps(AccountsFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountsBinding.bind(view)
    }

    companion object {
        fun getInstance(): AccountsFragment = AccountsFragment()
    }
}