package com.dullfan.base.base

import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dullfan.shopping.R
import com.tapadoo.alerter.Alerter

open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
    }

    fun<T> startA(clazz:Class<T>){
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

    fun showAlerter(title:String = "提示",text:String,color: Int = R.color.purple_500){
        Alerter.create(this)
            .setTitle(title)
            .setText(text)
            .setBackgroundColorInt(resources.getColor(color))
            .show()
    }

    fun glide(url:String,view:ImageView){
        Glide.with(this).load(url).into(view)
    }
}