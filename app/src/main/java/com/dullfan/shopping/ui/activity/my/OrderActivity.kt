package com.dullfan.shopping.ui.activity.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.dullfan.base.adapter.BaseRvAdapter
import com.dullfan.base.base.BaseActivity
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.OrderData
import com.dullfan.shopping.databinding.ActivityOrderBinding
import com.dullfan.shopping.databinding.ItemOrderLayoutBinding
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel

class OrderActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityOrderBinding.inflate(layoutInflater)
    }
    val viewModel : HttpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        viewModel.getOrderRequest()
        viewDataBinding.titleBack.setOnClickListener {
            finish()
        }
        viewModel.orderData.observe(this){
            viewDataBinding.orderRv.layoutManager = LinearLayoutManager(this)
            viewDataBinding.orderRv.adapter = object :BaseRvAdapter<OrderData>(R.layout.item_order__layout,it.data){
                override fun onBind(
                    rvDataBinding: ViewDataBinding,
                    data: OrderData,
                    position: Int
                ) {
                    rvDataBinding as ItemOrderLayoutBinding
                    rvDataBinding.orderId.text = "订单编号:${data.order_no}"
                    rvDataBinding.orderDateText.text = data.created_at.substring(0,data.created_at.length - 8)
                }
            }
        }



    }
}