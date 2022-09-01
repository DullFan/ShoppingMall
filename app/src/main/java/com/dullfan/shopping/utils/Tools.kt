package com.dullfan.base.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData
import java.util.*
import java.util.regex.Pattern


/**
 * 显示Log
 * @param tag Log标识
 * @param content 输出的内容
 */
fun showLog(content: Any, tag: String = "TAG") {
    Log.e(tag, "$content")
}

private var toast: Toast? = null

/**
 * 封装Toast
 */
fun Any.showToast(context: Context?) {
    if (toast == null) {
        toast = Toast.makeText(context, this.toString(), Toast.LENGTH_SHORT)
    } else {
        toast!!.cancel()
        toast = Toast.makeText(context, this.toString(), Toast.LENGTH_SHORT)
    }
    toast!!.show()
}

/**
 * 将URI路径转化为path路径
 */
fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
    //获取图片路径
    val filePathColumns = arrayOf(MediaStore.Images.Media.DATA)
    //通过selectedImage和filePathColumns 获取图片真实位置
    val c: Cursor? = context.contentResolver.query(contentURI, filePathColumns, null, null, null)
    //移动到第一行
    c?.moveToFirst()
    val columnIndex: Int? = c?.getColumnIndex(filePathColumns[0])
    //获取图片位置,不存在返回-1
    val path = columnIndex?.let { c.getString(it) }
    c?.close()
    return path
}

/**
 * 判断手机号码是否合法
 */
fun regexPhone(phone: String): Boolean {
    if (phone.length != 11) {
        return false
    }
    var mainRegex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,3,5-9])|(177))\\d{8}$"
    var p = Pattern.compile(mainRegex)
    val m = p.matcher(phone)
    return m.matches()
}

/**
 * 判断网络是否连接
 */
fun isNetConnected(context: Context?): Boolean {
    if (context != null) {
        val mConnectivityManager = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNetworkInfo != null) {
            // 有网络返回true
            return mNetworkInfo.isAvailable
        }
    }
    // 无网络返回false
    return false
}

/**
 * 判断是否使用网络请求
 */
fun isNet(context: Context, launch: () -> Unit) {
    if (isNetConnected(context)) {
        launch.invoke()
    } else {
        "当前无网络".showToast(context)
    }
}

/**
 * 判断是否有登录
 */
fun isCookie(context: Context, launch: () -> Unit) {
    if (KV.decodeInt(MMKVData.U_ID) != 0) {
        launch.invoke()
    } else {
        "当前还未登录哦!!!".showToast(context)
    }
}


/**
 * 判断是否有网络或登录
 */
fun isNetCookie(context: Context, launch: () -> Unit) {
    if (isNetConnected(context)) {
        if (KV.decodeInt(MMKVData.U_ID) != 0) {
            launch.invoke()
        } else {
            "当前还未登录哦!!!".showToast(context)

        }
    } else {
        "当前无网络".showToast(context)
    }
}

/**
 * 选择日期以及时间
 */
fun initDateTime(context: Context, textView: TextView) {
    val calendar = Calendar.getInstance()
    val calendarYear = calendar.get(Calendar.YEAR)
    val calendarMonth = calendar.get(Calendar.MONTH)
    val calendarDay = calendar.get(Calendar.DAY_OF_MONTH)
    val calendarHour = calendar.get(Calendar.HOUR_OF_DAY)
    val calendarMinute = calendar.get(Calendar.MINUTE)
    val datePickerDialog =
        DatePickerDialog(context, { view, year, month, dayOfMonth ->

            val date = "${year}-${month + 1}-${dayOfMonth}"

            val timePickerDialog = TimePickerDialog(
                context, { view, hourOfDay, minute ->
                    val dateTime = "${hourOfDay}:${minute}"
                    textView.text = "$date $dateTime"
                },
                calendarHour,
                calendarMinute,
                true
            )
            timePickerDialog.show()
        }, calendarYear, calendarMonth, calendarDay)
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
    datePickerDialog.show()
}

/**
 * 选择日期以及时间
 */
fun initDate(context: Context, textView: TextView) {
    val calendar = Calendar.getInstance()
    val calendarYear = calendar.get(Calendar.YEAR)
    val calendarMonth = calendar.get(Calendar.MONTH)
    val calendarDay = calendar.get(Calendar.DAY_OF_MONTH)
    val datePickerDialog =
        DatePickerDialog(context, { view, year, month, dayOfMonth ->

            val date = "${year}-${month + 1}-${dayOfMonth}"
            textView.text = date

        }, calendarYear, calendarMonth, calendarDay)
    datePickerDialog.datePicker.minDate = System.currentTimeMillis()
    datePickerDialog.show()
}


