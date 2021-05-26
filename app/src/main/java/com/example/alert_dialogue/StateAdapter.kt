package com.example.alert_dialogue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class StateAdapter(val states : List<State>) : BaseAdapter(){
    override fun getCount() : Int{
        return states.size
    }

    override fun getItem(position: Int): State{
        return states[position]
    }

    override fun getItemId(position: Int): Long{
        return states[position].state_name.hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
        val convertView = LayoutInflater.from(parent!!.context).inflate(R.layout.state_item, parent, false)

        val state_name = convertView.findViewById<TextView>(R.id.state)
        val country_name = convertView.findViewById<TextView>(R.id.country)

        state_name.text = getItem(position).state_name
        country_name.text = getItem(position).country_name

        return convertView
    }
}