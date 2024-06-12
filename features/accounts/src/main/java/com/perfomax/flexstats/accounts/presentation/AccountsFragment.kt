package com.perfomax.flexstats.accounts.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TableLayout
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.AccountDialogBinding
import com.perfomax.accounts.databinding.CustomDialogBinding
import com.perfomax.accounts.databinding.FragmentAccountsBinding
import com.perfomax.accounts.databinding.WebViewDialogBinding
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.accounts.di.DaggerAccountsComponent
import com.perfomax.flexstats.api.AccountsFeatureApi
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
    private lateinit var adapter: FragmentStateAdapter
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var accountDialogBinding: AccountDialogBinding
    private lateinit var webViewDialogBinding: WebViewDialogBinding

    private val fragmentList = listOf(
        YandexDirectListFragment.newInstance(),
        YandexMetrikaListFragment.newInstance()
    )

    private val accountTypeMap = listOf(
        Pair("yandex_direct", "Яндекс директ"),
        Pair("yandex_metrika", "Яндекс метрика")
    )

    @Inject
    lateinit var vmFactory: AccountsViewModelFactory

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var accountsFeatureApi: AccountsFeatureApi

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

        adapter = ViewPagerAdapter(requireActivity(), fragmentList)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.account_type_item, accountTypeMap.toList().map { it.second })

        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2){
                tab, pos -> tab.text = accountTypeMap[pos].second
        }.attach()
        binding.btnAddAccount.setOnClickListener {
            accountsViewModel.showAddAccountDialog()
        }
        setScreen()
    }



    // AlertDialogs---------------------------------------------------------------------------------
    private fun setScreen() {
        accountsViewModel.accountsScreen.observe(viewLifecycleOwner) {
            when(it) {
                is AccountsScreen.AddNewAccount -> showAccountDialog()
                is AccountsScreen.DeleteAccount -> {}
                is AccountsScreen.Nothing -> {}
            }
        }
    }

    private fun showAccountDialog() {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        accountDialogBinding = AccountDialogBinding.inflate(inflater)
        accountDialogBinding.autoCompleteTxt.setAdapter(arrayAdapter)
        dialog.setContentView(accountDialogBinding.root)
        dialog.show()

        accountDialogBinding.accountForm.setText("imedia-citilink-xiaomi-v")

        accountDialogBinding.autoCompleteTxt.setOnItemClickListener { parent, view, position, id ->
            if (position == 1) accountDialogBinding.metrikaCounter.visibility = View.VISIBLE
            else accountDialogBinding.metrikaCounter.visibility = View.GONE
        accountDialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        accountDialogBinding.btnConfirm.setOnClickListener {
            val login = accountDialogBinding.accountForm.text.toString()
            dialog.dismiss()
            showWebViewDialog(login, accountType = accountTypeMap[position].first)
            }
        }
    }

    private fun showWebViewDialog(login: String, accountType: String, metrikaCounter: String?=EMPTY) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        webViewDialogBinding = WebViewDialogBinding.inflate(inflater)
        dialog.setContentView(webViewDialogBinding.root)
        dialog.show()

        webViewDialogBinding.webView.visibility = View.VISIBLE
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
            val tokenCode = webViewUrl.split("=")[1].split("&")[0]
            dialog.dismiss()
            accountsViewModel.addNewAccount(
                accountName = login,
                tokenCode = tokenCode,
                accountType = accountType,
                metrikaCounter = metrikaCounter?:EMPTY
            )
            delay(200)
            router.navigateTo(
                fragment = accountsFeatureApi.open(),
                addToBackStack = false
            )
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