package com.dullfan.shopping.ui.fragment.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dullfan.base.base.BaseFragment
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.R
import com.dullfan.shopping.databinding.FragmentRetrievePasswordBinding
import com.dullfan.shopping.ui.activity.LoginActivity
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel

class RetrievePasswordFragment : BaseFragment() {

    val viewDataBinding by lazy {
        FragmentRetrievePasswordBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    var number = 10

    val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            if (msg.obj.toString() === "发送成功") {
                showAlerter(text = msg.obj.toString())
                handlerNumber.sendMessage(Message())
                viewDataBinding.retrieveButtonCode.isEnabled = false
                viewDataBinding.retrieveButtonCode.setBackgroundColor(resources.getColor(R.color.grey))
                viewDataBinding.retrieveButtonCode.setTextColor(resources.getColor(R.color.text_color))
            } else {
                showAlerter(text = msg.obj.toString(), color = R.color.red)
            }
        }
    }

    val retrievePasswordHandler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            if (msg.obj.toString() === "修改成功") {
                showAlerter(text = msg.obj.toString())
                (activity as LoginActivity).navigateUpFragment()
            } else {
                showAlerter(text = msg.obj.toString(), color = R.color.red)
            }
        }
    }


    val handlerNumber = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            showLog(number)
            if (number == 1) {
                viewDataBinding.retrieveButtonCode.isEnabled = true
                viewDataBinding.retrieveButtonCode.setBackgroundColor(resources.getColor(R.color.purple_700))
                viewDataBinding.retrieveButtonCode.setTextColor(resources.getColor(R.color.white))
                viewDataBinding.retrieveButtonCode.text = "获取验证码"
                number = 10
            } else {
                sendEmptyMessageDelayed(1, 1000)
                number--
                viewDataBinding.retrieveButtonCode.text = "${number}s后重试"
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handlerNumber.removeCallbacksAndMessages(null)
        handler.removeCallbacksAndMessages(null)
        retrievePasswordHandler.removeCallbacksAndMessages(null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding.retrieveForgotPassword.setOnClickListener(myClickListener {
            (activity as LoginActivity).navigateUpFragment()
        })


        viewDataBinding.retrieveButton.setOnClickListener(myClickListener {
            val email = viewDataBinding.retrieveEmailEdit.text.toString()
            val code = viewDataBinding.retrieveCodeEdit.text.toString()
            val password = viewDataBinding.retrievePasswordEdit.text.toString()

            if (code.isBlank() || password.isBlank() || email.isBlank()) {
                showAlerter(text = "请输入内容", color = R.color.red)
                return@myClickListener
            }

            if (password.length < 6) {
                showAlerter(text = "密码长度不能少于6位", color = R.color.red)
                return@myClickListener
            }

            viewModel.retrievePasswordRequest(email, code, password, retrievePasswordHandler)
        })

        viewDataBinding.retrieveButtonCode.setOnClickListener(myClickListener {
            val email = viewDataBinding.retrieveEmailEdit.text.toString()

            if (email.isBlank()) {
                showAlerter(text = "请输入邮箱", color = R.color.red)
                return@myClickListener
            }

            viewModel.sendEmailRequest(email, handler)
        })



        return viewDataBinding.root
    }

}