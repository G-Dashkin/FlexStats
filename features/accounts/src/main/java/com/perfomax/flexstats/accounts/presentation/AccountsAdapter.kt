package com.perfomax.flexstats.accounts.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.perfomax.accounts.databinding.ItemAccountBinding
import com.perfomax.flexstats.models.Account

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

    inner class AccountHolder(private val binding: ItemAccountBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(account: Account) {
            binding.accountName.text = account.name
            binding.metrikaCounter.text = "Счетчик метрики: ${account.metrikaCounter}"
            if (account.accountType == "yandex_metrika") binding.metrikaCounter.visibility = View.VISIBLE
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