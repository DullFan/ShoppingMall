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
import com.dullfan.shopping.databinding.FragmentLoginBinding
import com.dullfan.shopping.ui.activity.LoginActivity
import com.dullfan.shopping.ui.activity.MainActivity
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel

class LoginFragment : BaseFragment() {

    val viewDataBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    val viewModel: HttpViewModel by viewModels()

    val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            showAlerter(text = "邮箱或密码错误", color = R.color.red)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewDataBinding.loginRegister.setOnClickListener(myClickListener {
            (activity as LoginActivity).startRegister()
        })

        viewDataBinding.loginForgotPassword.setOnClickListener(myClickListener {
            (activity as LoginActivity).startRetrievePassword()
        })

        viewDataBinding.loginButton.setOnClickListener(myClickListener {
            val email = viewDataBinding.loginEmailEdit.text.toString()
            val password = viewDataBinding.loginPasswordEdit.text.toString()

            if (password.isBlank() || email.isBlank()) {
                showAlerter(text = "请输入内容", color = R.color.red)
                return@myClickListener
            }

            if (password.length < 6) {
                showAlerter(text = "密码长度不能少于6位", color = R.color.red)
                return@myClickListener
            }

            viewModel.loginRequest(password, email, handler)
            viewModel.loginData.observe(viewLifecycleOwner){
                showAlerter(text = "登录成功")
                KV.encode(MMKVData.U_TOKEN,it.access_token)
                startA(MainActivity::class.java)
                requireActivity().finish()
            }
        })


        return viewDataBinding.root
    }
}