package com.dullfan.shopping.utils

import android.R
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


class MyImageGetter(val textView: TextView, val context: Context) : Html.ImageGetter {

    override fun getDrawable(source: String?): Drawable {
        //在getDrawable中的source就是 img标签里src的值也就是图片的路径
        val drawable = LevelListDrawable() //等级列表图片

        val simpleTarget: SimpleTarget<Bitmap> = object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                val bitmapDrawable = BitmapDrawable(context.resources, resource)
                drawable.addLevel(1, 1, bitmapDrawable)
                drawable.setBounds(0, 0, resource.width, resource.height)
                drawable.level = 1
                textView.invalidate()
                textView.text = textView.text //解决图文重叠
            }
        }
        //设置占位符
        val options: RequestOptions = RequestOptions()
            .placeholder(R.drawable.stat_notify_error) //占位图片
            .error(R.drawable.stat_notify_error) //错误图片
            .fallback(R.drawable.stat_notify_error)
            .override(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)


        //加载图片
        Glide.with(context)
            .asBitmap()
            .load(source)
            .apply(options)
            .into(simpleTarget)
        return drawable
    }
}