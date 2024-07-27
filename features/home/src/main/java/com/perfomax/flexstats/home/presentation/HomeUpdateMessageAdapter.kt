package com.perfomax.flexstats.home.presentation

import android.app.Activity
import android.widget.ArrayAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.perfomax.home.R
import java.util.zip.Inflater


class HomeUpdateMessageAdapter(private val context: Activity, private val arrayList: ArrayList<String>):
    ArrayAdapter<String>(context, R.layout.message_list_item, arrayList)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.message_list_item, null)
        val textUpdateMessage: TextView = view.findViewById(R.id.text_update_message)
        textUpdateMessage.text = arrayList[position]
        return view
    }
}
