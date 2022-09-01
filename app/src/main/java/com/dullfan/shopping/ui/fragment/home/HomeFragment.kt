package com.dullfan.shopping.ui.fragment.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dullfan.base.base.BaseFragment
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.R
import com.dullfan.shopping.adapter.HomeRvAdapter
import com.dullfan.shopping.bean.HomeSlide
import com.dullfan.shopping.databinding.FragmentHomeBinding
import com.dullfan.shopping.ui.activity.SearchActivity
import com.dullfan.shopping.ui.activity.WebUrlActivity
import com.dullfan.shopping.utils.RETURN_ADDRESS
import com.dullfan.shopping.utils.TITLE
import com.dullfan.shopping.utils.WEB_VIEW_URL
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.RectangleIndicator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    val viewDataBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.findHomeDataRequest(1, Handler(Looper.myLooper()!!))
        viewModel.homeData.observe(viewLifecycleOwner) {
            initBanner(it.slides)
            initGoods()
        }

        viewDataBinding.homeSearch.setOnClickListener {
            startA(SearchActivity::class.java)
        }

        viewModel.findAddressRequest()
        viewModel.addressData.observe(viewLifecycleOwner){
            for (datum in it.data) {
                if(datum.is_default == 1){
                    RETURN_ADDRESS = datum
                }
            }
        }
        return viewDataBinding.root
    }

    override fun onResume() {
        super.onResume()
        initGoods()
    }

    private fun initGoods() {
        viewDataBinding.homeRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        val homeRvAdapter = HomeRvAdapter(requireContext())
        viewDataBinding.homeRv.adapter = homeRvAdapter
        viewDataBinding.homeRvText2.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.getPagingData().collect {
                homeRvAdapter.submitData(it)
            }
        }
    }

    private fun initBanner(slides: List<HomeSlide>) {
        viewDataBinding.homeBanner.adapter = object : BannerImageAdapter<HomeSlide>(slides) {
            override fun onBindView(
                holder: BannerImageHolder,
                data: HomeSlide,
                position: Int,
                size: Int
            ) {
                holder.imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                glide(data.img_url, holder.imageView)
            }
        }
        viewDataBinding.homeBanner.setIndicator(RectangleIndicator(requireContext()))
            .setIndicatorWidth(12, 50)
            .setIndicatorHeight(12)
            .setIndicatorSelectedColor(resources.getColor(R.color.white))
            .setIndicatorNormalColor(resources.getColor(R.color.grey))
            .setOnBannerListener { data, position ->
                TITLE = slides[position].title
                WEB_VIEW_URL = slides[position].url
                startA(WebUrlActivity::class.java)
            }
    }
}