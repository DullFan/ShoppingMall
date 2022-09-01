package com.dullfan.shopping.ui.activity.my

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.dullfan.base.adapter.BaseRvAdapter
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showAlertDialog
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.AddressData
import com.dullfan.shopping.databinding.ActivityAddressBinding
import com.dullfan.shopping.databinding.ItemAddressRvLayoutBinding
import com.dullfan.shopping.utils.ADDRESS_DATA
import com.dullfan.shopping.utils.ADDRESS_FLAG
import com.dullfan.shopping.utils.RETURN_ADDRESS
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel

class AddressActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityAddressBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    lateinit var rvAdapter: BaseRvAdapter<AddressData>
    val itemAddressRvLayoutList: ArrayList<ItemAddressRvLayoutBinding> = ArrayList()

    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        initData()

    }

    override fun onResume() {
        super.onResume()
        if (flag) {
            initData()
        }
        flag = true
    }

    private fun initData() {
        itemAddressRvLayoutList.clear()
        viewModel.findAddressRequest()
        viewDataBinding.titleBack.setOnClickListener(myClickListener {
            finish()
        })
        viewModel.addressData.observe(this) {
            for (datum in it.data) {
                if (datum.is_default == 1) {
                    RETURN_ADDRESS = datum
                }
            }
            val rvData = it.data as ArrayList<AddressData>
            viewDataBinding.addressRv.layoutManager = LinearLayoutManager(this)
            rvAdapter =
                object : BaseRvAdapter<AddressData>(R.layout.item_address_rv_layout, rvData) {
                    override fun onBind(
                        rvDataBinding: ViewDataBinding,
                        data: AddressData,
                        position: Int
                    ) {
                        rvDataBinding as ItemAddressRvLayoutBinding
                        itemAddressRvLayoutList.add(rvDataBinding)
                        if (ADDRESS_FLAG) {
                            rvDataBinding.root.setOnClickListener(myClickListener {
                                RETURN_ADDRESS = data
                                finish()
                            })
                        }


                        rvDataBinding.itemAddressName.text = data.name
                        if (data.name.length <= 2) {
                            rvDataBinding.itemAddressImage.text = data.name
                        } else {
                            rvDataBinding.itemAddressImage.text =
                                data.name.substring(data.name.length - 2, data.name.length)
                        }

                        if (data.is_default == 0) {
                            rvDataBinding.itemAddressDefault.visibility = View.GONE
                            rvDataBinding.itemAddressDefaultCheck.isChecked = false
                        } else {
                            rvDataBinding.itemAddressDefault.visibility = View.VISIBLE
                            rvDataBinding.itemAddressDefaultCheck.isChecked = true
                        }
                        rvDataBinding.itemAddressAddress.text =
                            "${data.province} ${data.city} ${data.county} ${data.address}"
                        rvDataBinding.itemAddressPhone.text = data.phone

                        rvDataBinding.itemAddressEditImage.setOnClickListener(myClickListener {
                            ADDRESS_DATA = data
                            startA(PutAddressActivity::class.java)
                        })

                        rvDataBinding.itemAddressDel.setOnClickListener(myClickListener {
                            showAlertDialog(this@AddressActivity,
                                message = "地址删除后无法恢复,是否删除地址?",
                                positiveButtonClick = {
                                    viewModel.delAddress(data.id,
                                        object : Handler(Looper.myLooper()!!) {
                                            override fun handleMessage(msg: Message) {
                                                if (msg.obj.toString() == "操作成功") {
                                                    rvData.removeAt(position)
                                                    rvAdapter.dataList = rvData
                                                }
                                            }
                                        })
                                })
                        })

                        rvDataBinding.itemAddressDefaultCheck.setOnClickListener { _ ->
                            for (itemAddressRvLayoutBinding in itemAddressRvLayoutList) {
                                itemAddressRvLayoutBinding.itemAddressDefaultCheck.isChecked = false
                                itemAddressRvLayoutBinding.itemAddressDefault.visibility = View.GONE
                            }

                            for (datum in it.data) {
                                if (datum.is_default == 1) {
                                    RETURN_ADDRESS = datum
                                }
                            }

                            rvDataBinding.itemAddressDefaultCheck.isChecked = true
                            rvDataBinding.itemAddressDefault.visibility = View.VISIBLE
                            viewModel.defaultAddress(data.id,
                                object : Handler(Looper.myLooper()!!) {
                                    override fun handleMessage(msg: Message) {

                                    }
                                })
                        }
                    }
                }
            viewDataBinding.addressRv.adapter = rvAdapter
        }

        viewDataBinding.titleRightText.setOnClickListener(myClickListener {
            if (viewDataBinding.titleRightText.text.toString() == "修改") {
                viewDataBinding.titleRightText.text = "完成"
                for (itemAddressRvLayoutBinding in itemAddressRvLayoutList) {
                    itemAddressRvLayoutBinding.itemAddressDel.visibility = View.VISIBLE
                    itemAddressRvLayoutBinding.itemAddressDefaultCheck.visibility = View.VISIBLE
                    itemAddressRvLayoutBinding.itemAddressView.visibility = View.VISIBLE
                }
            } else {
                viewDataBinding.titleRightText.text = "修改"
                for (itemAddressRvLayoutBinding in itemAddressRvLayoutList) {
                    itemAddressRvLayoutBinding.itemAddressDel.visibility = View.GONE
                    itemAddressRvLayoutBinding.itemAddressDefaultCheck.visibility = View.GONE
                    itemAddressRvLayoutBinding.itemAddressView.visibility = View.GONE
                }
            }
        })

        viewDataBinding.addressButton.setOnClickListener(myClickListener {
            startA(AddAddressActivity::class.java)
        })
    }
}