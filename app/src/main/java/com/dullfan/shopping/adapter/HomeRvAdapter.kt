package com.dullfan.shopping.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.dullfan.base.adapter.ViewHolder
import com.dullfan.base.utils.myClickListener
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.HomeData
import com.dullfan.shopping.databinding.ItemRecommendGoodsLayoutBinding
import com.dullfan.shopping.ui.activity.GoodsDetailsActivity
import com.dullfan.shopping.utils.GOODS_ID

class HomeRvAdapter(val context: Context) :PagingDataAdapter<HomeData,ViewHolder>(COMPARATOR){

    companion object {

        private val COMPARATOR = object : DiffUtil.ItemCallback<HomeData>() {
            override fun areItemsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HomeData, newItem: HomeData): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemRecommendGoodsLayoutBinding = holder.viewBinding as ItemRecommendGoodsLayoutBinding
        val data = getItem(position)
        Glide.with(context).load(data!!.cover_url).into(itemRecommendGoodsLayoutBinding.itemRecommendGoodsImage)
        itemRecommendGoodsLayoutBinding.itemRecommendGoodsText.text = data.title
        itemRecommendGoodsLayoutBinding.itemRecommendGoodsMoney.text = "${data.price}"
        itemRecommendGoodsLayoutBinding.itemRecommendGoodsPayNumber.text = "剩余${data.stock}件"
        itemRecommendGoodsLayoutBinding.root.setOnClickListener(myClickListener {
            GOODS_ID = data.id
            context.startActivity(Intent(context,GoodsDetailsActivity::class.java))
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_recommend_goods_layout,
            parent,
            false
        )
        return ViewHolder(viewBinding)
    }

}