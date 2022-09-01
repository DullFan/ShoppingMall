package com.dullfan.shopping.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import com.dullfan.base.base.BaseActivity
import com.dullfan.shopping.R
import com.dullfan.shopping.databinding.ActivitySearchBinding
import com.dullfan.shopping.utils.viewModels
import com.dullfan.shopping.viewmodel.HttpViewModel
import com.google.android.material.chip.Chip

class SearchActivity : BaseActivity() {

    val viewDataBinding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }

    private val list = mutableListOf<String>()

    val viewModel: HttpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewDataBinding.root)
        viewDataBinding.titleBack.setOnClickListener { finish() }
        viewDataBinding.searchSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewDataBinding.searchSearch.isIconified = true
                if (query!!.isNotEmpty()) {
                    list.add(query)
                    initData()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })





    }


    private fun initData() {
        viewDataBinding.searchChipGroup.removeAllViews()
        for (i in 0 until list.size) {
            val inflate = LayoutInflater.from(this).inflate(R.layout.item_search_layout, null)
            val chip: Chip = inflate.findViewById(R.id.item_search_chip)
            chip.text = list.get(i)
            viewDataBinding.searchChipGroup.addView(chip)
            chip.setOnCloseIconClickListener {
                list.removeAt(i)
                viewDataBinding.searchChipGroup.removeViewAt(i)
                initData()
            }
        }
    }
}