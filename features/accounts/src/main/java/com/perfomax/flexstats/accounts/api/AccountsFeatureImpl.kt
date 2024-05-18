package com.perfomax.flexstats.accounts.api

import androidx.fragment.app.Fragment
import com.perfomax.flexstats.accounts.presentation.AccountsFragment
import com.perfomax.flexstats.api.AccountsFeatureApi
import javax.inject.Inject

class AccountsFeatureImpl @Inject constructor() : AccountsFeatureApi {
    override fun open(): Fragment = AccountsFragment.getInstance()
}