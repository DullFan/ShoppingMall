package com.dullfan.shopping.ui.activity.my

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showLog
import com.dullfan.base.utils.showToast
import com.dullfan.shopping.R
import com.dullfan.shopping.bean.AddressData
import com.dullfan.shopping.databinding.ActivityAddAddressBinding
import com.dullfan.shopping.http.AddressBody
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel
import com.lljjcoder.Interface.OnCityItemClickListener
import com.lljjcoder.bean.CityBean
import com.lljjcoder.bean.DistrictBean
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.style.cityjd.JDCityConfig
import com.lljjcoder.style.cityjd.JDCityPicker


class AddAddressActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityAddAddressBinding.inflate(layoutInflater)
    }

    val viewModel:HttpViewModel by viewModels()

    var cityPicker = JDCityPicker()

    var cityString01 = ""
    var cityString02 = ""
    var cityString03 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        viewDataBinding.addAddressCity.setOnClickListener(myClickListener {
            val jdCityConfig = JDCityConfig.Builder().build()

            jdCityConfig.showType = JDCityConfig.ShowType.PRO_CITY_DIS
            cityPicker.init(this)
            cityPicker.setConfig(jdCityConfig)
            cityPicker.setOnCityItemClickListener(object : OnCityItemClickListener() {
                override fun onSelected(
                    province: ProvinceBean,
                    city: CityBean,
                    district: DistrictBean
                ) {
                    cityString01 = province.name
                    cityString02 = city.name
                    cityString03 = district.name
                    viewDataBinding.addAddressCity.text = "${province.name} ${city.name} ${district.name}"
                    viewDataBinding.addAddressCity.setTextColor(resources.getColor(R.color.black))
                }

                override fun onCancel() {}
            })
            cityPicker.showCityPicker()
        })



        viewDataBinding.addAddressButton.setOnClickListener(myClickListener {
            val name = viewDataBinding.addAddressName.text.toString()
            val phone = viewDataBinding.addAddressPhone.text.toString()
            val city = viewDataBinding.addAddressCity.text.toString()
            val address = viewDataBinding.addAddressAddress.text.toString()
            val where = if (viewDataBinding.addAddressSwitch.isChecked) 1 else 0
            if(name.isBlank() || phone.isBlank() || city.isBlank() || address.isBlank() || cityString01.isBlank()){
                showAlerter(text = "请输入信息")
                return@myClickListener
            }
            val addressBody = AddressBody(
                name,
                address,
                phone,
                cityString01,
                cityString02,
                cityString03,
                where.toString()
            )
            viewModel.addAddress(addressBody,object :Handler(Looper.myLooper()!!){
                override fun handleMessage(msg: Message) {
                    if(msg.obj == "操作成功"){
                        showAlerter(text = "添加成功")
                        finish()
                    }else{
                        showAlerter(text = "请检查手机号是否合法", color = R.color.red)
                    }
                }
            })

        })

    }
}