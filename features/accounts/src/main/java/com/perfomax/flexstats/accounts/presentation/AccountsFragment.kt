package com.perfomax.flexstats.accounts.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.AccountDialogBinding
import com.perfomax.accounts.databinding.CustomDialogBinding
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
    private lateinit var bindingCustomDialog: CustomDialogBinding
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
        setScreen()
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

    // AlertDialogs---------------------------------------------------------------------------------
    private fun setScreen() {
        accountsViewModel.accountsScreen.observe(viewLifecycleOwner) {
            when(it) {
                is AccountsScreen.AddNewAccount -> showAccountDialog()
                is AccountsScreen.DeleteAccount -> {
                    showDeleteAccountDialog(accountId = it.accountId, accountName = it.accountName)
                }
                is AccountsScreen.Nothing -> {}
            }
        }
    }

    private fun showAccountDialog() {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        accountDialogBinding = AccountDialogBinding.inflate(inflater)
        dialog.setContentView(accountDialogBinding.root)
        dialog.show()

        accountDialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        accountDialogBinding.btnConfirm.setOnClickListener {
            val login = accountDialogBinding.accountForm.text.toString()
            val password = accountDialogBinding.passwordForm.text.toString()
            accountsViewModel.addNewAccount(accountName = login, accountPassword = password )
            dialog.dismiss()
        }
    }

    private fun showDeleteAccountDialog(accountId: Int, accountName: String) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        bindingCustomDialog = CustomDialogBinding.inflate(inflater)
        dialog.setContentView(bindingCustomDialog.root)
        dialog.show()

        bindingCustomDialog.text2.text = accountName
        bindingCustomDialog.btnCancel.setOnClickListener { dialog.dismiss() }
        bindingCustomDialog.btnConfirm.setOnClickListener {
            accountsViewModel.deleteAccountClicked(accountId)
            dialog.dismiss()
        }
    }

    private fun settingsDialog(): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

}