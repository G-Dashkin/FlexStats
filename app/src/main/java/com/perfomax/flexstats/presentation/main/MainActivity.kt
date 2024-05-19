package com.perfomax.flexstats.presentation.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.FragmentManager
import com.perfomax.flexstats.R
import com.perfomax.flexstats.api.HomeFeatureApi
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.databinding.ActivityMainBinding
import com.perfomax.flexstats.databinding.FragmentNavigatorBinding
import com.perfomax.flexstats.di.DaggerProvider
import com.perfomax.flexstats.presentation.navigation.NavigatorHolder
import com.perfomax.flexstats.presentation.navigation.NavigatorLifecycle
import javax.inject.Inject

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}