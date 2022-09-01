package com.dullfan.shopping.bean

data class GoodsDetailsBean(
    val goods: GoodsDetailsGoods,
    val like_goods: List<GoodsDetailsLikeGood>
)

data class GoodsDetailsGoods(
    val category_id: Int,
    val collects_count: Int,
    val comments: List<Any>,
    val cover: String,
    val cover_url: String,
    val created_at: String,
    val description: String,
    val details: String,
    val id: Int,
    val is_collect: Int,
    val is_on: Int,
    val is_recommend: Int,
    val pics: Any,
    val pics_url: List<Any>,
    val price: Int,
    val sales: Int,
    val stock: Int,
    val title: String,
    val updated_at: String,
    val user_id: Int
)

data class GoodsDetailsLikeGood(
    val collects_count: Int,
    val cover: String,
    val cover_url: String,
    val id: Int,
    val price: Int,
    val sales: Int,
    val title: String
)