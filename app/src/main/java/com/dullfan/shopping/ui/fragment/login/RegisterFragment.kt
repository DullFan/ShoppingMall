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
import com.dullfan.shopping.R
import com.dullfan.shopping.databinding.FragmentRegisterBinding
import com.dullfan.shopping.ui.activity.LoginActivity
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel

class RegisterFragment : BaseFragment() {

    val viewDataBinding by lazy {
        FragmentRegisterBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            if (msg.obj.toString() === "注册成功") {
                (activity as LoginActivity).navigateUpFragment()
                showAlerter(text = msg.obj.toString())
            } else {
                showAlerter(text = msg.obj.toString(), color = R.color.red)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewDataBinding.registerForgotPassword.setOnClickListener(myClickListener {
            (activity as LoginActivity).navigateUpFragment()
        })

        viewDataBinding.registerButton.setOnClickListener(myClickListener {
            val email = viewDataBinding.registerEmailEdit.text.toString()
            val password = viewDataBinding.registerPasswordEdit.text.toString()
            val username = viewDataBinding.registerUsernameEdit.text.toString()

            if (username.isBlank() || password.isBlank() || email.isBlank()) {
                showAlerter(text = "请输入内容", color = R.color.red)
                return@myClickListener
            }

            if (password.length < 6) {
                showAlerter(text = "密码长度不能少于6位", color = R.color.red)
                return@myClickListener
            }

            viewModel.registerViewRequest(username, password, email, handler)
        })

        return viewDataBinding.root
    }

}