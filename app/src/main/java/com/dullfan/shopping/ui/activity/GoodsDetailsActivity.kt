package com.dullfan.shopping.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dullfan.base.adapter.BaseRvAdapter
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showLog
import com.dullfan.base.utils.showToast
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.GoodsDetailsGoods
import com.dullfan.shopping.bean.GoodsDetailsLikeGood
import com.dullfan.shopping.bean.SubmitCarsBean
import com.dullfan.shopping.databinding.ActivityGoodsDetailsBinding
import com.dullfan.shopping.databinding.DialogAddCarLayoutBinding
import com.dullfan.shopping.databinding.ItemRecommendGoodsLayoutBinding
import com.dullfan.shopping.http.AddCarGoods
import com.dullfan.shopping.utils.GOODS_ID
import com.dullfan.shopping.utils.MyImageGetter
import com.dullfan.shopping.utils.SUBMIT_CARS
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialog

class GoodsDetailsActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityGoodsDetailsBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            if (msg.obj.toString() == "操作成功") {
                if (viewDataBinding.goodsDetailsLike.isVisible) {
                    viewDataBinding.goodsDetailsLike.visibility = View.GONE
                    viewDataBinding.goodsDetailsLikeNo.visibility = VISIBLE
                } else {
                    viewDataBinding.goodsDetailsLike.visibility = VISIBLE
                    viewDataBinding.goodsDetailsLikeNo.visibility = View.GONE
                }

                showAlerter(text = msg.obj.toString())
            } else {
                showAlerter(text = msg.obj.toString(), color = R.color.red)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        viewDataBinding.goodsDetailsBackCard.setOnClickListener(myClickListener {
            finish()
        })

        viewDataBinding.goodsDetailsBack.setOnClickListener(myClickListener {
            finish()
        })

        viewModel.findGoodsDetailsRequest(GOODS_ID)

        viewDataBinding.goodsDetailsLikeNo.setOnClickListener(myClickListener {
            viewModel.goodsCollectAndCancelRequest(GOODS_ID, handler)
        })

        viewDataBinding.goodsDetailsLike.setOnClickListener(myClickListener {
            viewModel.goodsCollectAndCancelRequest(GOODS_ID, handler)
        })

        viewModel.goodsDetailsData.observe(this) {
            viewDataBinding.goodsDetailsButton.setOnClickListener(myClickListener {
                initDialog2(it.goods)
            })

            viewDataBinding.goodsDetailsTitle.text = it.goods.title
            viewDataBinding.goodsDetailsDescription.text = it.goods.description
            glide(it.goods.cover_url, viewDataBinding.goodsDetailsImage)
            val myImageGetter = MyImageGetter(viewDataBinding.goodsDetailsDetails, this)

            val sequence: CharSequence
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                sequence = Html.fromHtml(
                    it.goods.details,
                    Html.FROM_HTML_MODE_LEGACY,
                    myImageGetter,
                    null
                );
            } else {
                sequence = Html.fromHtml(it.goods.details);
            }

            viewDataBinding.goodsDetailsDetails.text = sequence

            if (it.goods.is_collect == 0) {
                viewDataBinding.goodsDetailsLike.visibility = View.GONE
                viewDataBinding.goodsDetailsLikeNo.visibility = VISIBLE
            } else {
                viewDataBinding.goodsDetailsLike.visibility = VISIBLE
                viewDataBinding.goodsDetailsLikeNo.visibility = View.GONE
            }

            viewDataBinding.goodsDetailsRv.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            viewDataBinding.goodsDetailsRv.adapter = object : BaseRvAdapter<GoodsDetailsLikeGood>(
                R.layout.item_recommend_goods_layout,
                it.like_goods
            ) {
                override fun onBind(
                    rvDataBinding: ViewDataBinding,
                    data: GoodsDetailsLikeGood,
                    position: Int
                ) {
                    val itemRecommendGoodsLayoutBinding =
                        rvDataBinding as ItemRecommendGoodsLayoutBinding
                    glide(data.cover_url, itemRecommendGoodsLayoutBinding.itemRecommendGoodsImage)
                    itemRecommendGoodsLayoutBinding.itemRecommendGoodsText.text = data.title
                    itemRecommendGoodsLayoutBinding.itemRecommendGoodsMoney.text = "${data.price}"
                    itemRecommendGoodsLayoutBinding.itemRecommendGoodsPayNumber.text =
                        "${data.sales}人购买"
                    itemRecommendGoodsLayoutBinding.root.setOnClickListener(myClickListener {
                        GOODS_ID = data.id
                        startA(GoodsDetailsActivity::class.java)
                    })

                }
            }


            viewDataBinding.goodsDetailsCar.setOnClickListener(myClickListener {
                initDialog(it.goods)
            })

            viewDataBinding.goodsDetailsCarCard.setOnClickListener(myClickListener {
                initDialog(it.goods)
            })
        }
    }

    private fun initDialog(goods: GoodsDetailsGoods) {
        val dialogViewDataBinding = DialogAddCarLayoutBinding.inflate(LayoutInflater.from(this))
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogViewDataBinding.root)
        bottomSheetDialog.behavior.state = STATE_EXPANDED

        dialogViewDataBinding.apply {
            dialogAddCarCancel.setOnClickListener(myClickListener {
                bottomSheetDialog.dismiss()
            })

            glide(goods.cover_url, dialogAddCarImage)
            dialogAddCarMoney.text = "${goods.price}"
            dialogAddCarMoney2.text = "${goods.price}"
            dialogAddCarContent.text = goods.title
            if (goods.stock != 0) {
                dialogAddCarNo.text = "有货"
            } else {
                dialogAddCarNo.text = "无货"
            }

            dialogViewDataBinding.dialogAddCarAdd.setOnClickListener(myClickListener {
                if (dialogAddCarNumber.text.toString().toInt() <= goods.stock) {
                    addNumber(dialogViewDataBinding.dialogAddCarNumber)
                    dialogViewDataBinding.dialogAddCarCut.setBackgroundColor(resources.getColor(R.color.add_grey))
                } else {
                    "数量超出范围~".showToast(this@GoodsDetailsActivity)
                }
            })

            dialogViewDataBinding.dialogAddCarCut.setOnClickListener(myClickListener {
                if (dialogAddCarNumber.text.toString().toInt() != 1) {
                    cutNumber(
                        dialogViewDataBinding.dialogAddCarNumber,
                        dialogViewDataBinding.dialogAddCarCut
                    )
                } else {
                    "数量低于范围~".showToast(this@GoodsDetailsActivity)
                }
            })

            dialogAddCarButton.setOnClickListener(myClickListener {
                if (goods.stock != 0) {
                    if (dialogAddCarNumber.text.toString().toInt() <= goods.stock) {
                        viewModel.addGoodsCar(
                            AddCarGoods(
                                GOODS_ID.toString(),
                                dialogAddCarNumber.text.toString()
                            ), object : Handler(
                                Looper.myLooper()!!
                            ) {
                                override fun handleMessage(msg: Message) {
                                    showLog(msg.obj)
                                    if (msg.obj.toString() == "添加成功") {
                                        showAlerter(text = "已将宝贝添加至购物车")
                                        bottomSheetDialog.dismiss()
                                    } else {
                                        showAlerter(text = "添加失败", color = R.color.red)
                                        bottomSheetDialog.dismiss()
                                    }
                                }
                            })
                    } else {
                        showAlerter(text = "当前商品库存没有那么多哦", color = R.color.red)
                    }
                } else {
                    showAlerter(text = "当前商品没有库存了~~~", color = R.color.red)
                    bottomSheetDialog.dismiss()
                }
            })
        }
        bottomSheetDialog.show()
    }

    /**
     * 立即购买
     */
    private fun initDialog2(goods: GoodsDetailsGoods) {
        val dialogViewDataBinding = DialogAddCarLayoutBinding.inflate(LayoutInflater.from(this))
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(dialogViewDataBinding.root)
        bottomSheetDialog.behavior.state = STATE_EXPANDED

        dialogViewDataBinding.apply {
            dialogAddCarCancel.setOnClickListener(myClickListener {
                bottomSheetDialog.dismiss()
            })

            glide(goods.cover_url, dialogAddCarImage)
            dialogAddCarMoney.text = "${goods.price}"
            dialogAddCarMoney2.text = "${goods.price}"
            dialogAddCarContent.text = goods.title
            if (goods.stock != 0) {
                dialogAddCarNo.text = "有货"
            } else {
                dialogAddCarNo.text = "无货"
            }

            dialogViewDataBinding.dialogAddCarAdd.setOnClickListener(myClickListener {
                if (dialogAddCarNumber.text.toString().toInt() <= goods.stock) {
                    addNumber(dialogViewDataBinding.dialogAddCarNumber)
                    dialogViewDataBinding.dialogAddCarCut.setBackgroundColor(resources.getColor(R.color.add_grey))
                } else {
                    "数量超出范围~".showToast(this@GoodsDetailsActivity)
                }
            })

            dialogViewDataBinding.dialogAddCarCut.setOnClickListener(myClickListener {
                if (dialogAddCarNumber.text.toString().toInt() != 1) {
                    cutNumber(
                        dialogViewDataBinding.dialogAddCarNumber,
                        dialogViewDataBinding.dialogAddCarCut
                    )
                } else {
                    "数量低于范围~".showToast(this@GoodsDetailsActivity)
                }
            })
            dialogViewDataBinding.dialogAddCarButton.setText("提交订单")
            dialogAddCarButton.setOnClickListener(myClickListener {
                if (goods.stock != 0) {
                    if (dialogAddCarNumber.text.toString().toInt() <= goods.stock) {
                        val dataList = ArrayList<SubmitCarsBean>()
                        dataList.add(
                            SubmitCarsBean(
                                goods,
                                dialogViewDataBinding.dialogAddCarNumber.text.toString().toInt()
                            )
                        )
                        SUBMIT_CARS = dataList
                        startA(SubmitOrderActivity::class.java)
                    } else {
                        showAlerter(text = "当前商品库存没有那么多哦", color = R.color.red)
                    }
                } else {
                    showAlerter(text = "当前商品没有库存了~~~", color = R.color.red)
                    bottomSheetDialog.dismiss()
                }
            })
        }
        bottomSheetDialog.show()
    }


    private fun cutNumber(dialogAddCarCut: TextView, dialogAddCarCut1: TextView) {
        var toInt = dialogAddCarCut.text.toString().toInt()
        toInt -= 1
        dialogAddCarCut.text = toInt.toString()
        if (toInt == 1) {
            dialogAddCarCut1.setBackgroundColor(resources.getColor(R.color.cut_grey))
        }
    }

    private fun addNumber(dialogAddCarNumber: TextView) {
        var toInt = dialogAddCarNumber.text.toString().toInt()
        toInt += 1
        dialogAddCarNumber.text = toInt.toString()
    }


}