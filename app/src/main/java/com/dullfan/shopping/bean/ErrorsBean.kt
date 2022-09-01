package com.dullfan.shopping.bean

data class ErrorsBean(
    val debug: ErrorsDebug,
    val errors: ErrorsErrors,
    val message: String,
    val status_code: Int
)

data class ErrorsDebug(
    val `class`: String,
    val `file`: String,
    val line: Int,
    val trace: List<String>
)

data class ErrorsErrors(
    val email: List<String>
)