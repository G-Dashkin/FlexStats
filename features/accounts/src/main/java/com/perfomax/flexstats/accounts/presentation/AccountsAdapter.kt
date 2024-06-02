package com.perfomax.flexstats.accounts.presentation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.perfomax.accounts.databinding.ItemAccountBinding
import com.perfomax.flexstats.models.Account
import com.perfomax.flexstats.models.Project

class AccountsAdapter(
    private val deleteAccountClick: (Int, String) -> Unit
): ListAdapter<Account, RecyclerView.ViewHolder>(AccountsDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  AccountHolder(binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position) as Account
        val viewHolder = holder as AccountHolder
        viewHolder.bind(item)
    }

    inner class AccountHolder(private val binding: ItemAccountBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(account: Account) {
            binding.projectName.text = account.name
            binding.btnDelete.setOnClickListener { deleteAccountClick.invoke(account.id!!, account.name) }
        }
    }



    internal class AccountsDiffCallback : DiffUtil.ItemCallback<Account>(){
        override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
            oldItem == newItem
        override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
            oldItem.id == newItem.id
    }
}