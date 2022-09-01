package com.dullfan.shopping.bean

data class CarBean(
    val `data`: List<CarData>
)

data class CarData(
    val goods_id: Int,
    val id: Int,
    val is_checked: Int,
    val num: Int,
    val user_id: Int
)