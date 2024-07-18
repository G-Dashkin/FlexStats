package com.perfomax.flexstats.accounts.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.perfomax.accounts.R
import com.perfomax.accounts.databinding.AccountDialogBinding
import com.perfomax.accounts.databinding.FragmentAccountsBinding
import com.perfomax.accounts.databinding.WebViewDialogBinding
import com.perfomax.flexstats.accounts.di.AccountsFeatureDepsProvider
import com.perfomax.flexstats.accounts.di.DaggerAccountsComponent
import com.perfomax.flexstats.api.AccountsFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.core.contracts.EMPTY
import com.perfomax.flexstats.core.contracts.METRIKA_LIST_SELECTED
import com.perfomax.flexstats.core.contracts.TOKEN_URL_OAUTH
import com.perfomax.flexstats.core.contracts.YANDEX_DIRECT
import com.perfomax.flexstats.core.contracts.YANDEX_METRIKA
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private lateinit var binding: FragmentAccountsBinding
    private lateinit var adapter: FragmentStateAdapter
    private lateinit var arrayAdapter: ArrayAdapter<String>

    private lateinit var accountDialogBinding: AccountDialogBinding
    private lateinit var webViewDialogBinding: WebViewDialogBinding

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
        binding.btnAddAccount.setOnClickListener { accountsViewModel.showAddAccountDialog() }

        setViewPager()
        setScreen()
    }

    private fun setViewPager(){
        val accountList = listOf(
            getString(com.perfomax.ui.R.string.yandex_direct),
            getString(com.perfomax.ui.R.string.yandex_metrika))

        val fragmentList = listOf(
            YandexDirectListFragment.newInstance(),
            YandexMetrikaListFragment.newInstance())

        adapter = ViewPagerAdapter(requireActivity(), fragmentList)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.account_type_item, accountList)
        binding.viewPager2.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager2){
                tab, pos -> tab.text = accountList[pos]
        }.attach()

        if (requireArguments().getBoolean(METRIKA_LIST_SELECTED))
            binding.viewPager2.post { binding.viewPager2.setCurrentItem(1, true) }
    }


    // AlertDialogs---------------------------------------------------------------------------------
    private fun setScreen() {
        accountsViewModel.accountsScreen.observe(viewLifecycleOwner) {
            when(it) {
                is AccountsScreen.AddNewAccount -> showAccountDialog()
                is AccountsScreen.ProjectNotExists -> projectNotExists()
                is AccountsScreen.DeleteAccount -> {}
            }
        }
    }

    private fun showAccountDialog() {
        val accountList = listOf(YANDEX_DIRECT, YANDEX_METRIKA)
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        accountDialogBinding = AccountDialogBinding.inflate(inflater)
        accountDialogBinding.autoCompleteTxt.setAdapter(arrayAdapter)
        dialog.setContentView(accountDialogBinding.root)
        dialog.show()

        accountDialogBinding.accountForm.setText("imedia-kcitilink")

        accountDialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        accountDialogBinding.autoCompleteTxt.setOnItemClickListener { parent, view, position, id ->
            if (position == 1) accountDialogBinding.metrikaCounter.visibility = View.VISIBLE
            else accountDialogBinding.metrikaCounter.visibility = View.GONE

        accountDialogBinding.btnConfirm.setOnClickListener {

            val directAccounts = accountsViewModel.accountsList.value?.filter { it.accountType == YANDEX_DIRECT }
            val metrikaAccounts = accountsViewModel.accountsList.value?.filter { it.accountType == YANDEX_METRIKA }

            if (accountDialogBinding.accountForm.text.isEmpty()) emptyAccount()
            else if (accountDialogBinding.metrikaCounterForm.text.isEmpty() && position == 1) emptyCounter()
            else if (directAccounts?.any { it.name == accountDialogBinding.accountForm.text.toString() && position == 0 } == true
                  || metrikaAccounts?.any { it.name == accountDialogBinding.accountForm.text.toString() && position == 1 } == true
            ) accountExists()
            else if (position != 0 && accountsViewModel.accountsList.value?.any {
                     it.metrikaCounter == accountDialogBinding.metrikaCounterForm.text.toString() } == true) counterExists()
            else {
                val login = accountDialogBinding.accountForm.text.toString()
                dialog.dismiss()
                showWebViewDialog(login, accountType = accountList[position])
                }
            }
        }
    }

    private fun showWebViewDialog(login: String, accountType: String, metrikaCounter: String?= EMPTY) {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        webViewDialogBinding = WebViewDialogBinding.inflate(inflater)
        dialog.setContentView(webViewDialogBinding.root)
        dialog.show()

        webViewDialogBinding.webView.visibility = View.VISIBLE
        webViewDialogBinding.webView.loadUrl(TOKEN_URL_OAUTH +login)
        webViewDialogBinding.webView.settings.javaScriptEnabled = true

        webViewDialogBinding.webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView, url: String) {
//                Log.d("MyLog", view.title.toString())
//                if (view.title.toString() == OUTPUT_ACCESS) {
//                    Log.d("MyLog", view.title.toString())
//                    webViewDialogBinding.webView.visibility = View.GONE
//                }
//                webViewDialogBinding.closeWebViewButton.visibility = View.VISIBLE
//            }
        }

        var webViewUrl = EMPTY
        lifecycleScope.launch {
            while (!webViewUrl.contains("&cid=")){
                webViewUrl = webViewDialogBinding.webView.url.toString()
                delay(300)
            }
            val tokenCode = webViewUrl.split("=")[1].split("&")[0]
            dialog.dismiss()
            accountsViewModel.addNewAccount(
                accountName = login,
                tokenCode = tokenCode,
                accountType = accountType,
                metrikaCounter = metrikaCounter?: EMPTY
            )
            delay(200)
            if (accountType == YANDEX_METRIKA)
            router.navigateTo(fragment = accountsFeatureApi.openMetrikaList(), addToBackStack = false)
            else router.navigateTo(fragment = accountsFeatureApi.openDirectList(), addToBackStack = false)
        }
        webViewDialogBinding.closeWebViewButton.setOnClickListener { dialog.dismiss() }
    }

    private fun settingsDialog(): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

    // Toast space-----------------------------------------------------------------------------
    private fun emptyAccount(){
        Toast.makeText(activity, getString(com.perfomax.ui.R.string.account_field_empty), Toast.LENGTH_LONG).show()
    }
    private fun emptyCounter(){
        Toast.makeText(activity, getString(com.perfomax.ui.R.string.metrika_counter_field_empty), Toast.LENGTH_LONG).show()
    }
    private fun accountExists(){
        Toast.makeText(activity, getString(com.perfomax.ui.R.string.account_exists), Toast.LENGTH_LONG).show()
    }
    private fun counterExists(){
        Toast.makeText(activity, getString(com.perfomax.ui.R.string.account_exists), Toast.LENGTH_LONG).show()
    }
    private fun projectNotExists(){
        val toast = Toast.makeText(activity, getString(com.perfomax.ui.R.string.project_must_be_created), Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    // Fragment Instance----------------------------------------------------------------------------
    companion object {
        fun getInstance(): AccountsFragment {
            return AccountsFragment().apply {
                val bundle = Bundle()
                bundle.putBoolean(METRIKA_LIST_SELECTED, false)
                arguments = bundle
            }
        }

        fun getInstanceMetrikaList(): AccountsFragment {
            return AccountsFragment().apply {
                val bundle = Bundle()
                bundle.putBoolean(METRIKA_LIST_SELECTED, true)
                arguments = bundle
            }
        }
    }

}