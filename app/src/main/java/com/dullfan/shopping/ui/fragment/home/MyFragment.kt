package com.dullfan.shopping.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.dullfan.base.adapter.BaseRvAdapter
import com.dullfan.base.base.BaseFragment
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showAlertDialog
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.R
import com.dullfan.shopping.databinding.FragmentMyBinding
import com.dullfan.shopping.databinding.ItemMyRvLayoutBinding
import com.dullfan.shopping.ui.activity.LoginActivity
import com.dullfan.shopping.ui.activity.SkinActivity
import com.dullfan.shopping.ui.activity.my.AddressActivity
import com.dullfan.shopping.ui.activity.my.InfoActivity
import com.dullfan.shopping.ui.activity.my.OrderActivity
import com.dullfan.shopping.utils.ADDRESS_FLAG
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel
import kotlinx.coroutines.channels.ticker


class MyFragment : BaseFragment() {

    val viewDataBinding by lazy {
        FragmentMyBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.findMyRequest()
        viewModel.myData.observe(viewLifecycleOwner) {
            glide(it.avatar_url, viewDataBinding.myAvatar)
            viewDataBinding.myName.text = it.name
            viewDataBinding.myEmail.text = it.email
        }
        val arrayListOf = arrayListOf("订单管理", "支付设置", "地址管理")
        val arrayListOf2 = arrayListOf("应用程序", "主题装扮", "关于我们")

        viewDataBinding.mySet.setOnClickListener(myClickListener {
            startA(InfoActivity::class.java)
        })

        viewDataBinding.myRv.layoutManager = LinearLayoutManager(requireContext())
        viewDataBinding.myRv2.layoutManager = LinearLayoutManager(requireContext())

        viewDataBinding.myRv.adapter =
            object : BaseRvAdapter<String>(R.layout.item_my_rv_layout, arrayListOf) {
                override fun onBind(rvDataBinding: ViewDataBinding, data: String, position: Int) {
                    rvDataBinding as ItemMyRvLayoutBinding
                    rvDataBinding.itemMyRvText.text = data
                    rvDataBinding.root.setOnClickListener(myClickListener {
                        when (position) {
                            0 -> {
                                startA(OrderActivity::class.java)
                            }
                            1 -> {}
                            2 -> {
                                ADDRESS_FLAG = false
                                startA(AddressActivity::class.java)
                            }
                            3 -> {}
                        }
                    })
                }
            }

        viewDataBinding.myRv2.adapter =
            object : BaseRvAdapter<String>(R.layout.item_my_rv_layout, arrayListOf2) {
                override fun onBind(rvDataBinding: ViewDataBinding, data: String, position: Int) {
                    rvDataBinding as ItemMyRvLayoutBinding
                    rvDataBinding.itemMyRvText.text = data
                    rvDataBinding.root.setOnClickListener(myClickListener {
                        when (position) {
                            0 -> {}
                            1 -> {
                                startA(SkinActivity::class.java)
                            }
                            2 -> {}
                        }
                    })
                }
            }


        viewDataBinding.myButton.setOnClickListener(myClickListener {
            showAlertDialog(requireContext(), message = "确定要退出吗?", positiveButtonClick = {
                requireActivity().finish()
                KV.encode(MMKVData.U_TOKEN, "空")
                startA(LoginActivity::class.java)
            })
        })
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.findMyRequest()
    }

}