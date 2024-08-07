package com.perfomax.flexstats.accounts.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.DeleteAccountDialogBinding
import com.perfomax.accounts.databinding.FragmentYandexMetrikaListBinding
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.accounts.di.DaggerAccountsComponent
import com.perfomax.flexstats.core.utils.yandexMetrikaFilter
import javax.inject.Inject

class YandexMetrikaListFragment: Fragment(R.layout.fragment_yandex_metrika_list) {

    companion object {
        fun newInstance() = YandexMetrikaListFragment()
    }

    private lateinit var binding: FragmentYandexMetrikaListBinding
    private lateinit var bindingCustomDialog: DeleteAccountDialogBinding

    @Inject
    lateinit var vmFactory: AccountsViewModelFactory

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
        binding = FragmentYandexMetrikaListBinding.bind(view)
        setAdapter()
        setScreen()
    }

    private fun setAdapter() {
        val adapter = AccountsAdapter(
            context = requireContext(),
            deleteAccountClick = { accountId, accountName ->
                accountsViewModel.showDeleteAccountDialog(accountId = accountId, accountName = accountName)
            }
        )
        binding.accountsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.accountsRecyclerView.adapter = adapter
        accountsViewModel.accountsList.observe(viewLifecycleOwner) {
            adapter.submitList(it.yandexMetrikaFilter())
        }
    }

    // AlertDialogs---------------------------------------------------------------------------------
    private fun setScreen() {
        accountsViewModel.accountsScreen.observe(viewLifecycleOwner) {
            when(it) {
                is AccountsScreen.AddNewAccount -> {}
                is AccountsScreen.DeleteAccount -> {
                    showDeleteAccountDialog(accountId = it.accountId, accountName = it.accountName)
                }
                is AccountsScreen.ProjectNotExists -> {}
                is AccountsScreen.MetrikaCounterNotExists -> {}
            }
        }
    }

    private fun showDeleteAccountDialog(accountId: Int, accountName: String) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        bindingCustomDialog = DeleteAccountDialogBinding.inflate(inflater)
        dialog.setContentView(bindingCustomDialog.root)
        dialog.show()

        bindingCustomDialog.accountName.text = accountName
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