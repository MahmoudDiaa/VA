package com.diaa.vacompanytechtest.adpters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diaa.vacompanytechtest.R
import com.diaa.vacompanytechtest.model.PendingModel
import java.util.*

private const val TAG = "PendingAdapter"

class PendingAdapter :
    RecyclerView.Adapter<PendingAdapter.ViewHolder>() {
    private var pendingList = ArrayList<PendingModel>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(
                parent.context
            ).inflate(R.layout.rv_pending_layout, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        Log.e(TAG, "onBindViewHolder: " )
        holder.bind(pendingList[position])

    }

    override fun getItemCount(): Int {
        return pendingList.size
    }

    fun addItem(pendingModel: PendingModel) {
        Log.e(TAG, "addItem: ")
        pendingList.add(pendingModel)
        notifyDataSetChanged()
        notifyItemInserted(pendingList.lastIndex)
    }

    fun deleteItem(index: Int) {
        pendingList.removeIf { result -> result.index == index }
        notifyDataSetChanged()
    }

    fun setList(moviesList: ArrayList<PendingModel>) {
        this.pendingList = moviesList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(pendingModel: PendingModel) {

            val textViewIndex = itemView.findViewById(R.id.tv_index) as TextView
            val textViewEquation = itemView.findViewById(R.id.tv_pending_equation) as TextView
            Log.e(TAG, "bind: ")
            textViewEquation.text = pendingModel.equation
            textViewIndex.text = pendingModel.index.toString()
        }


    }
}