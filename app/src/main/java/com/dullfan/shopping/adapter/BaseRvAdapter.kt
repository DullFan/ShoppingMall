package com.dullfan.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRvAdapter<T>(var layoutId: Int, _dataList: List<T>, _index: Int = 0) :
    RecyclerView.Adapter<ViewHolder>() {

    var dataList = _dataList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var index = _index
        set(value) {
            notifyItemChanged(field)
            field = value
            notifyItemChanged(index)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBind(holder.viewBinding, dataList[position], position)
    }

    abstract fun onBind(rvDataBinding: ViewDataBinding, data: T, position: Int)

    override fun getItemCount(): Int {
        return dataList.size
    }
}

class ViewHolder(val viewBinding: ViewDataBinding) : RecyclerView.ViewHolder(viewBinding.root)