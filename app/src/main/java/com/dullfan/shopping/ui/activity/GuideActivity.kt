package com.dullfan.shopping.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.dullfan.base.adapter.ViewHolder
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.GuideBean
import com.dullfan.shopping.databinding.ActivityGuideBinding
import com.dullfan.shopping.databinding.ItemGuideBannerLayoutBinding
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.RectangleIndicator
import com.youth.banner.listener.OnPageChangeListener

class GuideActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityGuideBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)

        if (KV.decodeBool(MMKVData.GUIDE_FLAG, false)) {
            finish()
            startA(LoginActivity::class.java)
        } else {
            initBanner()
        }
    }

    private fun initBanner() {
        val arrayListOf = arrayListOf<GuideBean>(
            GuideBean(
                R.drawable.guide_img1,
                "轻松搜索您的产品",
                "现在你只需扫一下你的智能手机就可以搜索到",
                "你最喜欢的产品了"
            ),
            GuideBean(
                R.drawable.guide_img2,
                "优秀的生活态度",
                "有了我们的新功能,您可以先享受产品而不必担心付",
                "款问题"
            ),
            GuideBean(
                R.drawable.guide_img3,
                "足不出户",
                "我们的成员会直接把你的产品送到你家门口,放松享受你",
                "的美丽人生"
            )
        )

        val adapter = object : BannerAdapter<GuideBean, ViewHolder>(arrayListOf) {
            override fun onCreateHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val bannerDataBinding: ItemGuideBannerLayoutBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_guide_banner_layout,
                    parent,
                    false
                )
                return ViewHolder(bannerDataBinding)
            }

            override fun onBindView(
                holder: ViewHolder,
                data: GuideBean,
                position: Int,
                size: Int
            ) {
                val itemGuideBannerLayoutBinding =
                    holder.viewBinding as ItemGuideBannerLayoutBinding

                itemGuideBannerLayoutBinding.itemGuideImage.setImageResource(data.Image)
                itemGuideBannerLayoutBinding.itemGuideText01.text = data.string1
                itemGuideBannerLayoutBinding.itemGuideText02.text = data.string2
                itemGuideBannerLayoutBinding.itemGuideText03.text = data.string3
            }
        }

        viewDataBinding.guideBanner.setAdapter(adapter, false)
            .isAutoLoop(false)
            .addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    if (position == arrayListOf.size - 1) {
                        viewDataBinding.guideButton.text = "登录"
                    } else {
                        viewDataBinding.guideButton.text = "下一步"
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })
            .setIndicator(RectangleIndicator(this))
            .setIndicatorWidth(15, 40)
            .setIndicatorHeight(15)
            .setIndicatorSelectedColor(resources.getColor(R.color.purple_700))
            .setIndicatorNormalColor(resources.getColor(R.color.grey))

        viewDataBinding.guideButton.setOnClickListener(myClickListener {
            when (viewDataBinding.guideBanner.currentItem) {
                0 -> {
                    viewDataBinding.guideBanner.setCurrentItem(1)
                }
                1 -> {
                    viewDataBinding.guideBanner.setCurrentItem(2)
                }
                2 -> {
                    KV.encode(MMKVData.GUIDE_FLAG, true)
                    finish()
                    startA(LoginActivity::class.java)
                }
            }
        })
    }

}