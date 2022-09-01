package com.dullfan.shopping.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import com.dullfan.base.adapter.BaseRvAdapter
import com.dullfan.base.base.BaseActivity
import com.dullfan.base.utils.myClickListener
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.R
import com.dullfan.shopping.databinding.ActivitySkinBinding
import com.dullfan.shopping.databinding.ItemSkinLayoutBinding
import com.dullfan.shopping.skin.SkinManager
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData
import com.tencent.mmkv.MMKV

class SkinActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivitySkinBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        val arrayListOf = arrayListOf("原皮", "淘宝色", "京东色", "皮肤3")
        viewDataBinding.skinRv.layoutManager = GridLayoutManager(this,3)
        viewDataBinding.skinRv.adapter = object :BaseRvAdapter<String>(R.layout.item_skin_layout,arrayListOf){
            override fun onBind(rvDataBinding: ViewDataBinding, data: String, position: Int) {
                rvDataBinding as ItemSkinLayoutBinding
                rvDataBinding.itemSkinText.text = data

                rvDataBinding.root.setOnClickListener(myClickListener {
                    notifyItemChanged(KV.decodeInt(MMKVData.SKIN_POSITION))
                    KV.encode(MMKVData.SKIN_POSITION,position)
                    notifyItemChanged(position)
                    when(position){
                        0->{
                            SkinManager.getInstance().loadSkin(null)
                        }
                        1->{
                            SkinManager.getInstance().loadSkin("${baseContext.filesDir}/taobao.skin")
                        }
                        2->{
                            SkinManager.getInstance().loadSkin("${baseContext.filesDir}/jingdong.skin")
                        }
                        3->{
                            SkinManager.getInstance().loadSkin("${baseContext.filesDir}/xianyu.skin")
                        }
                    }
                    window.statusBarColor = Color.TRANSPARENT
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                })

                showLog(KV.decodeInt(MMKVData.SKIN_POSITION,0))
                if(KV.decodeInt(MMKVData.SKIN_POSITION,0) == position){
                    rvDataBinding.itemSkinCard.visibility = View.VISIBLE
                }else{
                    rvDataBinding.itemSkinCard.visibility = View.GONE
                }

            }
        }
    }
}