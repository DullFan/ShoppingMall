package com.dullfan.shopping.ui.activity

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.dullfan.base.adapter.BaseRvAdapter
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.SubmitCarsBean
import com.dullfan.shopping.databinding.ActivitySubmitOrderBinding
import com.dullfan.shopping.databinding.ItemSubmitOrderRvLayoutBinding
import com.dullfan.shopping.ui.activity.my.AddressActivity
import com.dullfan.shopping.utils.ADDRESS_FLAG
import com.dullfan.shopping.utils.RETURN_ADDRESS
import com.dullfan.shopping.utils.SUBMIT_CARS

class SubmitOrderActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivitySubmitOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        viewDataBinding.titleBack.setOnClickListener { finish() }
        viewDataBinding.submitOrderRv.layoutManager = LinearLayoutManager(this)
        viewDataBinding.submitOrderRv.adapter = object : BaseRvAdapter<SubmitCarsBean>(
            R.layout.item_submit_order_rv_layout,
            SUBMIT_CARS
        ) {
            override fun onBind(
                rvDataBinding: ViewDataBinding,
                data: SubmitCarsBean,
                position: Int
            ) {
                rvDataBinding as ItemSubmitOrderRvLayoutBinding
                glide(data.dataList.cover_url, rvDataBinding.itemSubmitOrderImage)
                rvDataBinding.itemSubmitOrderName.text = data.dataList.title
                rvDataBinding.itemSubmitOrderMoney.text = data.dataList.price.toString()
                rvDataBinding.itemSubmitOrderNumber.text = "x${data.num}"
            }
        }

        var money = 0
        for (submitCar in SUBMIT_CARS) {
            money += submitCar.dataList.price * submitCar.num
        }

        viewDataBinding.submitOrderLayout04.setOnClickListener(myClickListener {
            ADDRESS_FLAG = true
            startA(AddressActivity::class.java)
        })

        viewDataBinding.submitOrderMoney.text = money.toString()
        viewDataBinding.submitOrderSelectedNumber.text = "共${SUBMIT_CARS.size}件"
        viewDataBinding.submitOrderCheck01.setOnClickListener {
            viewDataBinding.submitOrderCheck01.isChecked = true
            viewDataBinding.submitOrderCheck02.isChecked = false
            viewDataBinding.submitOrderCheck03.isChecked = false
        }

        viewDataBinding.submitOrderCheck02.setOnClickListener {
            viewDataBinding.submitOrderCheck01.isChecked = false
            viewDataBinding.submitOrderCheck02.isChecked = true
            viewDataBinding.submitOrderCheck03.isChecked = false
        }

        viewDataBinding.submitOrderCheck03.setOnClickListener {
            viewDataBinding.submitOrderCheck01.isChecked = false
            viewDataBinding.submitOrderCheck02.isChecked = false
            viewDataBinding.submitOrderCheck03.isChecked = true
        }

        viewDataBinding.submitOrderSettlement.setOnClickListener(myClickListener {
            showAlerter(text = "提交成功")
            finish()
        })

    }

    override fun onResume() {
        super.onResume()
        viewDataBinding.submitOrderAddress.text = RETURN_ADDRESS.name
        viewDataBinding.submitOrderPhone.text = RETURN_ADDRESS.phone
        viewDataBinding.submitOrderName.text =
            "${RETURN_ADDRESS.province} ${RETURN_ADDRESS.city} ${RETURN_ADDRESS.county} ${RETURN_ADDRESS.address}"
        if (RETURN_ADDRESS.name.length <= 2) {
            viewDataBinding.submitOrderImage.text = RETURN_ADDRESS.name
        } else {
            viewDataBinding.submitOrderImage.text =
                RETURN_ADDRESS.name.substring(
                    RETURN_ADDRESS.name.length - 2,
                    RETURN_ADDRESS.name.length
                )
        }
    }
}