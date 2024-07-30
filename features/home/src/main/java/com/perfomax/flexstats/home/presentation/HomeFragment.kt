package com.perfomax.flexstats.home.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.util.Pair
import android.view.Gravity
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.perfomax.flexstats.core.navigation.Router
import com.perfomax.flexstats.core.utils.toTimestamp
import com.perfomax.flexstats.home.di.DaggerHomeComponent
import com.perfomax.flexstats.home.di.HomeFeatureDepsProvider
import com.perfomax.home.R
import com.perfomax.home.databinding.ClearStatsDialogBinding
import com.perfomax.home.databinding.FragmentHomeBinding
import com.perfomax.home.databinding.UpdateMessageDialogBinding
import javax.inject.Inject

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var clearStatsDialogBinding: ClearStatsDialogBinding
    private lateinit var updateMessageDialogBinding: UpdateMessageDialogBinding

    private lateinit var updateMessageDialogView: Dialog
    private lateinit var messageUpdateArrayList: ArrayList<String>
    private lateinit var messageUpdateArrayAdapter: ArrayAdapter<String>

    @Inject
    lateinit var vmFactory: HomeViewModelFactory

    @Inject
    lateinit var router: Router

    private val homeViewModel by viewModels<HomeViewModel> {
        vmFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val homeComponent = DaggerHomeComponent
            .builder()
            .addDeps(HomeFeatureDepsProvider.deps)
            .build()
        homeComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.selectStatsPeriodButton.setOnClickListener {
            homeViewModel.showStatsDatePiker()
        }

        binding.selectUpdatePeriodButton.setOnClickListener {
            homeViewModel.showUpdateDataPiker()
        }

        binding.updateStatsButton.setOnClickListener {
            homeViewModel.updateStats()
        }

        binding.clearStatsButton.setOnClickListener {
            showClearStatsDialog()
        }

        setAdapter()
        setScreen()
    }

    private fun setScreen() {
        homeViewModel.homeScreen.observe(viewLifecycleOwner) {
            when(it) {
                is HomeScreen.ShowTitle -> binding.twTitle.visibility = View.VISIBLE
                is HomeScreen.HideTitle -> binding.twTitle.visibility = View.GONE
                is HomeScreen.StartLoadingProcess -> {
                    showProgressIndicator()
                    showUpdateMessageDialog()
                }
                is HomeScreen.EndLoadingProcess -> {
                    hideProgressIndicator()
                    hideUpdateMessageDialog()
                }
                is HomeScreen.ShowDatePicker -> showStatsDatePiker()
                is HomeScreen.ShowUpdatePicker -> showUpdateDataPiker()
                is HomeScreen.ShowToast -> showToast(it.updateDate)
                is HomeScreen.SendUpdatedMessage -> sendUpdateMessage(it.message)
            }
        }
    }

    private fun setAdapter() {
        val adapter = HomeAdapter()
        binding.statsRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.statsRecyclerView.adapter = adapter
        homeViewModel.statsList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun showStatsDatePiker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(com.perfomax.ui.R.style.MaterialCalendarTheme)
            .setTitleText(com.perfomax.ui.R.string.select_show_stats_period)
            .setSelection(
                Pair(
                    homeViewModel.selectedStatsPeriod.value?.first?.toTimestamp(),
                    homeViewModel.selectedStatsPeriod.value?.second?.toTimestamp(),
                )
            )
            .build()

        picker.show(this.parentFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {
            homeViewModel.selectStatsPeriod(
                firstDate = homeViewModel.convertTimeToDate(it.first),
                secondDate = homeViewModel.convertTimeToDate(it.second)
            )
        }
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
    }

    private fun showUpdateDataPiker() {
        val picker = MaterialDatePicker.Builder.dateRangePicker()
            .setTheme(com.perfomax.ui.R.style.MaterialCalendarTheme)
            .setTitleText(com.perfomax.ui.R.string.select_update_stats_period)
            .setSelection(
                Pair(
                    homeViewModel.selectedUpdateStatsPeriod.value?.first?.toTimestamp(),
                    homeViewModel.selectedUpdateStatsPeriod.value?.second?.toTimestamp(),
                )
            )
            .build()

        picker.show(this.parentFragmentManager, "TAG")
        picker.addOnPositiveButtonClickListener {
            homeViewModel.selectUpdatePeriod(
                firstDate = homeViewModel.convertTimeToDate(it.first),
                secondDate = homeViewModel.convertTimeToDate(it.second)
            )
        }
        picker.addOnNegativeButtonClickListener {
            picker.dismiss()
        }
    }

    private fun showClearStatsDialog() {
        val dialog = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        clearStatsDialogBinding = ClearStatsDialogBinding.inflate(inflater)
        dialog.setContentView(clearStatsDialogBinding.root)
        dialog.show()
        clearStatsDialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        clearStatsDialogBinding.btnConfirm.setOnClickListener {
            homeViewModel.clearStats()
            dialog.dismiss()
        }
    }

    private fun showToast(message: String){
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.toast_layout, null)
        val text = layout.findViewById<TextView>(R.id.toast_text)
        text.text = message
        val toast = Toast(activity)
        toast.view = layout
        toast.duration = Toast.LENGTH_LONG
        toast.apply {
            setGravity(Gravity.BOTTOM, 0, 350)
            show()
        }
    }

    private fun showUpdateMessageDialog() {
        updateMessageDialogView = settingsDialog()
        val inflater = requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        updateMessageDialogBinding = UpdateMessageDialogBinding.inflate(inflater)
        updateMessageDialogView.setContentView(updateMessageDialogBinding.root)
        updateMessageDialogView.show()
        updateMessageDialogView.window?.setGravity(Gravity.BOTTOM)
        messageUpdateArrayList = arrayListOf()
    }

    private fun hideUpdateMessageDialog(){
        updateMessageDialogView.dismiss()
    }

    private fun sendUpdateMessage(message: String){
        messageUpdateArrayList.add(message)
        messageUpdateArrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, messageUpdateArrayList)
        updateMessageDialogBinding.updateMessageListView.adapter = messageUpdateArrayAdapter
        updateMessageDialogBinding.updateMessageListView.setSelection(messageUpdateArrayAdapter.getCount() - 1)
    }

    private fun settingsDialog(): Dialog {
        val dialog = Dialog(requireContext(), com.perfomax.ui.R.style.UpdateMessageDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

    private fun showProgressIndicator() {
        binding.circularProgressIndicator.visibility = View.VISIBLE
        binding.circularProgressIndicator.bringToFront()
    }

    private fun hideProgressIndicator() {
        binding.circularProgressIndicator.visibility = View.GONE
    }

    companion object {
        fun getInstance(): HomeFragment = HomeFragment()
    }
}

