package com.dullfan.shopping.utils

import com.tencent.mmkv.MMKV


val KV: MMKV = MMKV.defaultMMKV()

object MMKVData {
    const val U_ID = "id"
    const val U_TOKEN = "token"
    const val GUIDE_FLAG = "guide_flag"
    const val SKIN_POSITION = "skin_position"
}