package com.dullfan.shopping.ui.fragment.home

import android.media.SubtitleData
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dullfan.base.adapter.BaseRvAdapter
import com.dullfan.base.base.BaseFragment
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showAlertDialog
import com.dullfan.base.utils.showLog
import com.dullfan.base.utils.showToast
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.GoodsDetailsGoods
import com.dullfan.shopping.bean.SubmitCarsBean
import com.dullfan.shopping.databinding.FragmentCarBinding
import com.dullfan.shopping.databinding.ItemCarRvLayoutBinding
import com.dullfan.shopping.http.CarsNumBody
import com.dullfan.shopping.ui.activity.GoodsDetailsActivity
import com.dullfan.shopping.ui.activity.SubmitOrderActivity
import com.dullfan.shopping.utils.GOODS_ID
import com.dullfan.shopping.utils.SUBMIT_CARS
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel

class CarFragment : BaseFragment() {

    val viewDataBinding by lazy {
        FragmentCarBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    val array = arrayListOf<GoodsDetailsGoods>()

    lateinit var rvAdapter: BaseRvAdapter<GoodsDetailsGoods>

    val carCheckData: ArrayList<Boolean> = ArrayList()
    val rvCheckView: ArrayList<CheckBox> = ArrayList()
    val moneyTextView: ArrayList<TextView> = ArrayList()
    val numTextView: ArrayList<TextView> = ArrayList()

    var flag = false

    val handler = object : Handler(
        Looper.myLooper()!!
    ) {
        override fun handleMessage(msg: Message) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initData()

        viewModel.carsData.observe(viewLifecycleOwner) {
            array.clear()
            carCheckData.clear()
            moneyTextView.clear()
            numTextView.clear()
            rvCheckView.clear()

            if (it.data.size == 0) {
                viewDataBinding.carLayout.visibility = View.GONE
                viewDataBinding.carNoData.visibility = View.VISIBLE
            }

            viewDataBinding.carLayout.visibility = View.VISIBLE
            viewDataBinding.carNoData.visibility = View.GONE

            for (datum in it.data) {
                viewModel.findGoodsDetailsRequest(datum.goods_id)
            }

            viewDataBinding.carRv.layoutManager = LinearLayoutManager(requireContext())

            rvAdapter =
                object : BaseRvAdapter<GoodsDetailsGoods>(R.layout.item_car_rv_layout, array) {
                    override fun onBind(
                        rvDataBinding: ViewDataBinding,
                        data: GoodsDetailsGoods,
                        position: Int
                    ) {
                        rvDataBinding as ItemCarRvLayoutBinding
                        rvDataBinding.itemCarRvName.text = data.title
                        rvDataBinding.itemCarRvMoney.text = "${data.price}"

                        Glide.with(requireContext()).load(data.cover_url)
                            .into(rvDataBinding.itemCarRvImage)

                        for (datum in it.data) {
                            if (data.id == datum.goods_id) {
                                rvDataBinding.carNumber.text = "${datum.num}"
                            }
                        }

                        rvDataBinding.root.setOnClickListener(myClickListener {
                            GOODS_ID = data.id
                            startA(GoodsDetailsActivity::class.java)
                        })

                        rvDataBinding.carAdd.setOnClickListener(myClickListener {
                            if (rvDataBinding.carNumber.text.toString().toInt() <= data.stock) {
                                addNumber(rvDataBinding.carNumber, it.data[position].id)
                                if (rvDataBinding.itemCarRvCheck.isChecked) {
                                    calculateThePrice(
                                        1,
                                        data.price,
                                        rvDataBinding.itemCarRvCheck.isChecked
                                    )
                                }
                            } else {
                                "数量超出范围~".showToast(requireContext())
                            }
                        })

                        rvDataBinding.carCut.setOnClickListener(myClickListener {
                            if (rvDataBinding.carNumber.text.toString().toInt() != 1) {
                                cutNumber(rvDataBinding.carNumber, it.data[position].id)
                                if (rvDataBinding.itemCarRvCheck.isChecked) {
                                    calculateThePrice(1, data.price, false)
                                }
                            } else {
                                "数量低于范围~".showToast(requireContext())
                            }
                        })

                        rvDataBinding.itemCarRvCheck.setOnClickListener { _ ->
                            carCheckData[position] = rvDataBinding.itemCarRvCheck.isChecked
                            carSelectedNumberVisibility()
                            calculateThePrice(
                                rvDataBinding.carNumber.text.toString().toInt(),
                                data.price,
                                rvDataBinding.itemCarRvCheck.isChecked
                            )
                        }

                        rvCheckView.add(rvDataBinding.itemCarRvCheck)
                        moneyTextView.add(rvDataBinding.itemCarRvMoney)
                        numTextView.add(rvDataBinding.carNumber)

                        rvDataBinding.root.setOnLongClickListener { _ ->
                            showAlertDialog(requireContext(), "确定要删除吗?", positiveButtonClick = {
                                viewModel.delCarsNum(
                                    CarsNumBody(it.data[position].id.toString()),
                                    it.data[position].id, handler
                                )
                                array.removeAt(position)
                                rvAdapter.dataList = array
                            })
                            return@setOnLongClickListener true
                        }

                        viewDataBinding.carSelectAll.setOnClickListener {
                            if (viewDataBinding.carSelectAll.isChecked) {
                                rvCheckView.forEachIndexed { index, checkBox ->
                                    if (!checkBox.isChecked) {
                                        checkBox.isChecked = true
                                        carCheckData.forEachIndexed { index, b ->
                                            carCheckData[index] = true
                                        }
                                        calculateThePrice(
                                            numTextView[index].text.toString().toInt(),
                                            moneyTextView[index].text.toString().toInt(),
                                            true
                                        )
                                    }
                                }
                                viewDataBinding.carSelectedNumber.visibility = View.VISIBLE
                                viewDataBinding.carSelectedNumber.text = "已选${array.size}件"
                            } else {
                                for (checkBox in rvCheckView) {
                                    checkBox.isChecked = false
                                }
                                viewDataBinding.carMoney.text = "0"
                                viewDataBinding.carSelectedNumber.visibility = View.GONE
                            }
                        }
                    }
                }

            viewDataBinding.carRv.adapter = rvAdapter


            viewDataBinding.carSettlement.setOnClickListener(myClickListener {
                if(viewDataBinding.carMoney.text.toString() != "0"){
                    val dataList = ArrayList<SubmitCarsBean>()
                    array.forEachIndexed { index, goodsDetailsGoods ->
                        if(carCheckData[index]){
                            dataList.add(SubmitCarsBean(goodsDetailsGoods,numTextView[index].text.toString().toInt()))
                        }
                    }
                    SUBMIT_CARS = dataList
                    startA(SubmitOrderActivity::class.java)
                }else{
                    showAlerter(text = "你还没有选择宝贝哦!")
                }
            })
        }

        viewModel.goodsDetailsData.observe(viewLifecycleOwner) {
            array.add(it.goods)
            carCheckData.add(false)
            rvAdapter.dataList = array
        }

        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        if (flag) {
            viewDataBinding.carMoney.text = "0"
            viewDataBinding.carSelectedNumber.visibility = View.GONE
            initData()
        }
        flag = true
    }

    private fun initData() {
        array.clear()
        carCheckData.clear()
        viewModel.findCars()
    }

    private fun calculateThePrice(num: Int, price: Int, checked: Boolean) {
        if (checked) {
            var money = viewDataBinding.carMoney.text.toString().toInt()
            money += num * price
            viewDataBinding.carMoney.text = "${money}"
        } else {
            var money = viewDataBinding.carMoney.text.toString().toInt()
            money -= num * price
            viewDataBinding.carMoney.text = "${money}"
        }

    }

    private fun carSelectedNumberVisibility() {
        var i = 0
        for (carCheckDatum in carCheckData) {
            if (carCheckDatum) {
                i += 1
            }
        }

        viewDataBinding.carSelectAll.isChecked = i == carCheckData.size

        if (i == 0) {
            viewDataBinding.carSelectedNumber.visibility = View.GONE
        } else {
            viewDataBinding.carSelectedNumber.visibility = View.VISIBLE
            viewDataBinding.carSelectedNumber.text = "已选${i}件"
        }
    }

    private fun cutNumber(dialogAddCarCut: TextView, id: Int) {
        var toInt = dialogAddCarCut.text.toString().toInt()
        toInt -= 1
        dialogAddCarCut.text = toInt.toString()
        viewModel.putCarsNum(
            CarsNumBody(toInt.toString()),
            id,
            object : Handler(Looper.myLooper()!!) {
                override fun handleMessage(msg: Message) {

                }
            })
    }

    private fun addNumber(carNumber: TextView, id: Int) {
        var toInt = carNumber.text.toString().toInt()
        toInt += 1
        carNumber.text = toInt.toString()
        viewModel.putCarsNum(
            CarsNumBody(toInt.toString()),
            id,
            object : Handler(Looper.myLooper()!!) {
                override fun handleMessage(msg: Message) {

                }
            })
    }

}