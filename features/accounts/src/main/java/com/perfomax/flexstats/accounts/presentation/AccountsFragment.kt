package com.perfomax.flexstats.accounts.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.AccountDialogBinding
import com.perfomax.accounts.databinding.FragmentAccountsBinding
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.accounts.di.DaggerAccountsComponent
import com.perfomax.flexstats.core.navigation.Router
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    companion object {
        fun getInstance(): AccountsFragment = AccountsFragment()
    }

    private lateinit var binding: FragmentAccountsBinding
    private lateinit var accountDialogBinding: AccountDialogBinding

    @Inject
    lateinit var vmFactory: AccountsViewModelFactory

    @Inject
    lateinit var router: Router

    private val accountsViewModel by viewModels<AccountsViewModel> {
        vmFactory
    }

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
        binding.btnAddAccount.setOnClickListener { accountsViewModel.showAddAccountDialog() }
        setAdapter()
    }

    private fun setAdapter() {
        val adapter = AccountsAdapter(
            deleteAccountClick = { accountId, accountName ->
                accountsViewModel.showDeleteAccountDialog(accountId = accountId, accountName = accountName)
            }
        )
        binding.accountsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.accountsRecyclerView.adapter = adapter

        accountsViewModel.accountsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

}