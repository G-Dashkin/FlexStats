package com.perfomax.flexstats.accounts.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.CustomDialogBinding
import com.perfomax.accounts.databinding.FragmentAccountsBinding
import com.perfomax.accounts.databinding.FragmentYandexDirectListBinding
import com.perfomax.accounts.databinding.WebViewDialogBinding
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.accounts.di.DaggerAccountsComponent
import com.perfomax.flexstats.core.utils.EMPTY
import com.perfomax.flexstats.core.utils.TOKEN_URL_OAUTH
import com.perfomax.flexstats.core.utils.yandexDirectFilter
import com.perfomax.flexstats.models.Account
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class YandexDirectListFragment: Fragment(R.layout.fragment_yandex_direct_list) {

    companion object {
        fun newInstance() = YandexDirectListFragment()
    }

    private lateinit var binding: FragmentYandexDirectListBinding
    private lateinit var bindingCustomDialog: CustomDialogBinding

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
        binding = FragmentYandexDirectListBinding.bind(view)
        setAdapter()
        setScreen()
    }

    private fun setAdapter() {
        val adapter = AccountsAdapter(
            deleteAccountClick = { accountId, accountName ->
                accountsViewModel.showDeleteAccountDialog(accountId = accountId, accountName = accountName)
            }
        )
        binding.accountsRecyclerView.layoutManager = LinearLayoutManager(this@YandexDirectListFragment.context)
        binding.accountsRecyclerView.adapter = adapter
        accountsViewModel.accountsList.observe(viewLifecycleOwner) {
            adapter.submitList(it.yandexDirectFilter())
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
                is AccountsScreen.Nothing -> {}
            }
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