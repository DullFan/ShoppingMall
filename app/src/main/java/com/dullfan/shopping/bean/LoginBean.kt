package com.dullfan.shopping.bean

data class LoginBean(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)