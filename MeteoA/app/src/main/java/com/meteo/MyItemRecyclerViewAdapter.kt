package com.meteo

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class MyItemRecyclerViewAdapter(
    private val values: MainActivity

    ) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    // un seul item ( pour fonctionner avec le recyclerview )
    override fun getItemCount(): Int = 1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    }
}