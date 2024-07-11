package com.perfomax.flexstats.home.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.perfomax.flexstats.models.GeneralStats
import com.perfomax.flexstats.models.Project
import com.perfomax.flexstats.models.YandexDirectStats
import com.perfomax.home.databinding.ItemStatsBinding

class StatsAdapter(): ListAdapter<GeneralStats, RecyclerView.ViewHolder>(StatsDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  StatsHolder(binding = ItemStatsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) as GeneralStats
        val viewHolder = holder as StatsHolder
        viewHolder.bind(item)
    }

    inner class StatsHolder(private val binding: ItemStatsBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(stats: GeneralStats) {
            binding.valueDate.text = stats.date

            binding.valueImpressions.text = stats.impressions.toString()
            binding.valueClicks.text = stats.clicks.toString()
            binding.valueCosts.text = stats.cost.toString()

            binding.valueTransactions.text
            binding.valueRevenue.text

//            binding.valueCPC.text = (stats.cost!! / stats.impressions?.toDouble()!!).toString()
            binding.valueCR.text
            binding.valueDRR
        }
    }

    internal class StatsDiffCallback : DiffUtil.ItemCallback<GeneralStats>(){
        override fun areItemsTheSame(oldItem: GeneralStats, newItem: GeneralStats): Boolean =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: GeneralStats, newItem: GeneralStats): Boolean =
            oldItem.id == newItem.id
    }

}