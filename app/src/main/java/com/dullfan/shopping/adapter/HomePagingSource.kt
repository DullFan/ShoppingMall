package com.dullfan.shopping.adapter

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.bean.HomeData
import com.dullfan.shopping.http.ShoppingMallApi

class HomePagingSource(private val shoppingMallApi: ShoppingMallApi): PagingSource<Int, HomeData>() {
    override fun getRefreshKey(state: PagingState<Int, HomeData>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeData> {
        return try {
            val page = params.key ?: 1

            val repoResponse = shoppingMallApi.findHomeDataRequest(page)

            val repoItems = repoResponse.goods.data
            val prevKey = if(page > 1) page - 1 else null
            val nextKey = if(repoItems.isNotEmpty()) page + 1 else null
            LoadResult.Page(repoItems,prevKey,nextKey)
        } catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}