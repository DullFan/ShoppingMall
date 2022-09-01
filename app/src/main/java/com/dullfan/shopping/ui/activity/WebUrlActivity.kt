package com.dullfan.shopping.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.shopping.databinding.ActivityWebUrlBinding
import com.dullfan.shopping.utils.TITLE
import com.dullfan.shopping.utils.WEB_VIEW_URL

class WebUrlActivity : BaseActivity() {
    private lateinit var binding: ActivityWebUrlBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebUrlBinding.inflate(layoutInflater)

        //TODO 标题 直接复制
        binding.titleBack.setOnClickListener(myClickListener {
            finish()
        })
        binding.titleText.text = TITLE


        setContentView(binding.root)
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.apply {
                //支持JS
                javaScriptEnabled = true
                //支持自动加载图片
                loadsImagesAutomatically = true
                //自适应屏幕
                layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                loadWithOverviewMode = true
                //扩大比例的缩放
                useWideViewPort = true
                //设置出现缩放工具
                builtInZoomControls = true
                //设置可以支持缩放
                setSupportZoom(true)
                setWebChromeClient(object : WebChromeClient() {
                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                        if (newProgress == 100) {
                            binding.webProgressBar.visibility = View.GONE
                        } else {
                            binding.webProgressBar.visibility = View.VISIBLE
                            binding.webProgressBar.progress = newProgress
                        }
                    }
                })
            }

            loadUrl(WEB_VIEW_URL)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
            return
        }
        super.onBackPressed()
    }
}