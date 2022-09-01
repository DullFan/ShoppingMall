package com.dullfan.shopping.bean

data class HomeBean(
    val categories: List<HomeCategory>,
    val goods: HomeGoods,
    val slides: List<HomeSlide>
)

data class HomeCategory(
    val children: List<HomeChildren>,
    val id: Int,
    val level: Int,
    val name: String,
    val pid: Int,
    val status: Int
)

data class HomeGoods(
    val current_page: Int,
    val `data`: List<HomeData>,
    val first_page_url: String,
    val from: Int,
    val next_page_url: String,
    val path: String,
    val per_page: Int,
    val prev_page_url: Any,
    val to: Int
)

data class HomeSlide(
    val created_at: String,
    val id: Int,
    val img: String,
    val img_url: String,
    val seq: Int,
    val status: Int,
    val title: String,
    val updated_at: String,
    val url: String
)

data class HomeChildren(
    val id: Int,
    val level: Int,
    val name: String,
    val pid: Int,
    val status: Int
)

data class HomeData(
    val collects_count: Int,
    val cover: String,
    val cover_url: String,
    val description: String,
    val id: Int,
    val price: Int,
    val sales: Int,
    val stock: Int,
    val title: String
)