package com.perfomax.flexstats.accounts.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.webkit.WebViewClient
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.AccountDialogBinding
import com.perfomax.accounts.databinding.CustomDialogBinding
import com.perfomax.accounts.databinding.FragmentAccountsBinding
import com.perfomax.accounts.databinding.WebViewDialogBinding
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.accounts.di.DaggerAccountsComponent
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.core.utils.EMPTY
import com.perfomax.flexstats.core.utils.TOKEN_URL_OAUTH
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
    private lateinit var webViewDialogBinding: WebViewDialogBinding

    @Inject
    lateinit var vmFactory: AccountsViewModelFactory

    @Inject
    lateinit var router: Router

    private val accountsViewModel by viewModels<AccountsViewModel> {
        vmFactory
    }

    var tokenCode = ""

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
            dialog.dismiss()
            val tokenCode = showWebViewDialog(login)
//            Log.d("MyLog", tokenCode)
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

    private fun showWebViewDialog(login: String) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        webViewDialogBinding = WebViewDialogBinding.inflate(inflater)
        dialog.setContentView(webViewDialogBinding.root)
        dialog.show()

//        val url = "https://oauth.yandex.ru/authorize?response_type=code&client_id=4f078917869842f2932a9d30fa5d0bb5&redirect_uri=http://flexstats.ru/yandex_direct_oauth.html&login_hint=imedia-citilink-xiaomi-v"
        webViewDialogBinding.webView.visibility = View.VISIBLE
//        webViewDialogBinding.webView.loadUrl(url)
        webViewDialogBinding.webView.loadUrl(TOKEN_URL_OAUTH+login)
        webViewDialogBinding.webView.settings.javaScriptEnabled = true
        webViewDialogBinding.webView.webViewClient = WebViewClient()
        webViewDialogBinding.webView.settings.setSupportZoom(true)

        var webViewUrl = EMPTY
        lifecycleScope.launch {
            while (!webViewUrl.contains("&cid=")){
                webViewUrl = webViewDialogBinding.webView.url.toString()
                delay(100)
            }
            tokenCode = webViewUrl.split("=")[1].split("&")[0]
            dialog.dismiss()
            accountsViewModel.addNewAccount(accountName = login, tokenCode = tokenCode)
        }

        webViewDialogBinding.closeWebViewButton.setOnClickListener {
            dialog.dismiss()
            webViewUrl = "&cid="
        }
    }

    private fun settingsDialog(): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

}