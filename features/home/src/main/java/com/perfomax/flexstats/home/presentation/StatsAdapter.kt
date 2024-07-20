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
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

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

        val df = DecimalFormat("#0.00")
        val dec = DecimalFormat("###,###,###,###,###", DecimalFormatSymbols(Locale.ENGLISH))

        fun bind(stats: GeneralStats) {
            binding.valueDate.text = stats.date

            binding.valueImpressions.text = dec.format(stats.impressions).replace(",", " ")
            binding.valueClicks.text = dec.format(stats.clicks).replace(",", " ")
            binding.valueCosts.text = dec.format(stats.cost).replace(",", " ") + "р."

            binding.valueTransactions.text = dec.format(stats.transactions).replace(",", " ")
            binding.valueRevenue.text = dec.format(stats.revenue).replace(",", " ") + "р."

            binding.valueCPC.text = df.format(((stats.cost?:0).div(stats.clicks?.toDouble()!!))) + "р."
            binding.valueCTR.text = df.format(((stats.clicks?:0).div(stats.impressions?.toDouble()!!)*100)) + "%"
            binding.valueCR.text = df.format(((stats.clicks?:0).div(stats.transactions?.toDouble()!!))) + "%"
            binding.valueCPO.text = df.format(((stats.cost?:0).div(stats.transactions?.toDouble()!!))) + "р."
            binding.valueDRR.text = df.format(((stats.cost?:0).div(stats.revenue?.toDouble()!!)*100)) + "%"
        }
    }

    internal class StatsDiffCallback : DiffUtil.ItemCallback<GeneralStats>(){
        override fun areItemsTheSame(oldItem: GeneralStats, newItem: GeneralStats): Boolean =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: GeneralStats, newItem: GeneralStats): Boolean =
            oldItem.id == newItem.id
    }

}