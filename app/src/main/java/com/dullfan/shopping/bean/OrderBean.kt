package com.dullfan.shopping.bean

data class OrderBean(
    val `data`: List<OrderData>,
    val meta: Meta
)

data class OrderData(
    val address_id: Int,
    val amount: Int,
    val created_at: String,
    val express_no: Any,
    val express_type: Any,
    val id: Int,
    val order_no: String,
    val pay_time: Any,
    val pay_type: Any,
    val status: Int,
    val trade_no: Any,
    val updated_at: String,
    val user_id: Int
)

data class Meta(
    val pagination: OrderPagination
)

data class OrderPagination(
    val count: Int,
    val current_page: Int,
    val links: OrderLinks,
    val per_page: Int,
    val total: Int,
    val total_pages: Int
)

data class OrderLinks(
    val next: String,
    val previous: Any
)