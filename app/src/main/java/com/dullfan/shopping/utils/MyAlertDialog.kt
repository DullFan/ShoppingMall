package com.dullfan.base.utils

import android.app.AlertDialog
import android.content.Context

fun showAlertDialog(
    context: Context,
    message: String,
    title: String = "提示",
    positiveButtonClick: () -> Unit = {},
    negativeButtonClick: () -> Unit = {},
) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            "确定"
        ) { _, _ ->
            positiveButtonClick.invoke()
        }
        .setNegativeButton("取消") { _, _ ->
            negativeButtonClick.invoke()
        }.show()
}