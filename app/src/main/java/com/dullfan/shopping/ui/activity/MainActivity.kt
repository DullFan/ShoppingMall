package com.dullfan.shopping.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.shopping.R
import com.dullfan.shopping.adapter.MyViewPager2Adapter
import com.dullfan.shopping.databinding.ActivityMainBinding
import com.dullfan.shopping.ui.fragment.home.CarFragment
import com.dullfan.shopping.ui.fragment.home.HomeFragment
import com.dullfan.shopping.ui.fragment.home.MyFragment
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData

class MainActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(HomeFragment())
        fragmentList.add(CarFragment())
        fragmentList.add(MyFragment())
        val myViewPager2Adapter = MyViewPager2Adapter(this, fragmentList)
        viewDataBinding.mainVp2.adapter = myViewPager2Adapter
        viewDataBinding.mainVp2.offscreenPageLimit = 5
        viewDataBinding.mainBottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.main_menu_home -> viewDataBinding.mainVp2.setCurrentItem(0, false)
                R.id.main_menu_car -> viewDataBinding.mainVp2.setCurrentItem(1, false)
                R.id.main_menu_my -> viewDataBinding.mainVp2.setCurrentItem(2, false)
            }
            return@setOnItemSelectedListener true
        }
        viewDataBinding.mainVp2.isUserInputEnabled = false
    }
}