package com.dullfan.shopping.bean

data class AddressBean(
    val `data`: List<AddressData>
)

data class AddressData(
    val address: String,
    val city: String,
    val county: String,
    val created_at: String,
    val id: Int,
    val is_default: Int,
    val name: String,
    val phone: String,
    val province: String,
    val updated_at: String
)