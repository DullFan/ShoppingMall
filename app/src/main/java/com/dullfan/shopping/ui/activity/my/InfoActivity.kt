package com.dullfan.shopping.ui.activity.my

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.R
import com.dullfan.shopping.databinding.ActivityInfoBinding
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel

class InfoActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityInfoBinding.inflate(layoutInflater)
    }

    val viewModel:HttpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)

        viewModel.findMyRequest()
        viewModel.myData.observe(this){
            glide(it.avatar_url,viewDataBinding.infoAvatar)
            viewDataBinding.infoName.setText(it.name)
            viewDataBinding.infoEmail.text = it.email
            viewDataBinding.infoCreateTime.text = it.created_at
            viewDataBinding.infoName.isEnabled = false
        }

        viewDataBinding.titleBack.setOnClickListener {
            finish()
        }

        viewDataBinding.titleRightText.setOnClickListener(myClickListener {
            val name = viewDataBinding.titleRightText.text.toString()
            if(name == "完成"){
                viewDataBinding.titleRightText.text = "修改"
                viewDataBinding.infoName.isEnabled = false
                viewModel.putInfoData(viewDataBinding.infoName.text.toString(),object :Handler(Looper.myLooper()!!){
                    override fun handleMessage(msg: Message) {
                        if(msg.obj.toString() == "修改成功"){
                            showAlerter(text = "修改成功")
                        }else{
                            showAlerter(text = "修改失败", color = R.color.red)
                        }
                    }
                })
            }else{
                viewDataBinding.titleRightText.text = "完成"
                viewDataBinding.infoName.isEnabled = true
            }
        })


    }
}