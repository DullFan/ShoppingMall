package com.dullfan.base.base

import android.content.Intent
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.dullfan.shopping.R
import com.tapadoo.alerter.Alerter

open class BaseFragment : Fragment() {

    fun<T> startA(clazz:Class<T>){
        val intent = Intent(requireContext(), clazz)
        startActivity(intent)
    }

    fun <T> showToast(value:T){
        Toast.makeText(requireContext(),"$value", Toast.LENGTH_LONG).show()
    }

    fun showAlerter(title:String = "提示",text:String = "修改成功",color: Int = R.color.purple_500){
        Alerter.create(requireActivity())
            .setTitle(title)
            .setText(text)
            .setBackgroundColorInt(resources.getColor(color))
            .show()
    }


    fun glide(url:String,view: ImageView){
        Glide.with(requireContext()).load(url).into(view)
    }

}