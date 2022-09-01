package com.dullfan.shopping.adapter

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dullfan.base.http.getShoppingServiceApi
import com.dullfan.shopping.bean.HomeData
import com.dullfan.shopping.http.ShoppingMallApi
import kotlinx.coroutines.flow.Flow

object Repository {
    private const val PAGE_SIZE = 10

    fun getPagingData(): Flow<PagingData<HomeData>> {
        //这里创建了一个Pager对象，并调用.flow将它转换成一个Flow对象。
        return Pager(
            //每页所包含的数据量。
            config = PagingConfig(PAGE_SIZE),
            //用于分页的数据源
            pagingSourceFactory = {HomePagingSource(getShoppingServiceApi())}
        ).flow
    }
}