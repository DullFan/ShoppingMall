package com.dullfan.base.utils

import android.view.View

/**
 * 防止多次点击触发多次点击事件
 */
abstract class OnMultiCLickListener : View.OnClickListener{
    private val minClickTime = 700
    private var lastClickTime:Long = 0

    public abstract fun onMultiClick(v: View)

    override fun onClick(v: View) {
        val curClickTime = System.currentTimeMillis()
        if(curClickTime - lastClickTime >= minClickTime){
            lastClickTime = curClickTime
            onMultiClick(v)
        }

    }
}

fun myClickListener(action:()-> Unit):OnMultiCLickListener {
    return object :OnMultiCLickListener(){
        override fun onMultiClick(v: View) {
            action.invoke()
        }
    }
}