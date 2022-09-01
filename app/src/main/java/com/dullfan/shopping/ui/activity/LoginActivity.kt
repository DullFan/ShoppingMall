package com.dullfan.shopping.ui.activity

import android.os.Bundle
import androidx.navigation.Navigation
import com.dullfan.base.base.BaseActivity
import com.dullfan.shopping.R
import com.dullfan.shopping.databinding.ActivityLoginBinding
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData

class LoginActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    val navigation by lazy {
        Navigation.findNavController(this,R.id.login_fragmentContainerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)

        if(KV.decodeString(MMKVData.U_TOKEN,"空") != "空"){
            finish()
            startA(MainActivity::class.java)
        }

    }

    fun startRegister(){
        navigation.navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun startRetrievePassword(){
        navigation.navigate(R.id.action_loginFragment_to_retrievePasswordFragment)
    }

    fun navigateUpFragment(){
        navigation.navigateUp()
    }
}