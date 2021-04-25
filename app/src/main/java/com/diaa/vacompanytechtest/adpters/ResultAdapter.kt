package com.diaa.vacompanytechtest.adpters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diaa.vacompanytechtest.R
import com.diaa.vacompanytechtest.model.PendingModel
import com.diaa.vacompanytechtest.model.ResultModel
import java.util.*

private const val TAG = "ResultAdapter"
class ResultAdapter :
    RecyclerView.Adapter<ResultAdapter.ViewHolder>() {
    private var resultList = ArrayList<ResultModel>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.rv_result_layout, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(resultList[position])
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    fun addItem(resultModel: ResultModel) {
        resultList.add(resultModel)
        Log.e(TAG, "addItem: " )
        notifyDataSetChanged()
    }


    fun deleteItem(index: Int) {
        resultList.removeIf { result -> result.index == index }
        notifyDataSetChanged()
    }

    fun setList(moviesList: ArrayList<ResultModel>) {
        this.resultList = moviesList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(result: ResultModel) {

            val textViewIndex = itemView.findViewById(R.id.tv_index) as TextView
            val textViewResult = itemView.findViewById(R.id.tv_result) as TextView
            textViewIndex.text = result.index.toString()
            textViewResult.text = result.result
        }

    }

}