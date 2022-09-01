package com.dullfan.base.http

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.http.ShoppingMallApi
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.*

import okhttp3.Interceptor
import java.lang.String
import java.util.concurrent.TimeUnit

const val TEAM_URI = "https://api.shop.eduwork.cn"

fun <T> getShoppingServiceApi(claszz: Class<T>, handler: Handler): T {
    val mOkHttpClient = OkHttpClient().newBuilder().apply {
        addInterceptor(LoggingInterceptor(handler))
        //添加读取超时时间
        readTimeout(10000, TimeUnit.SECONDS)
        //添加连接超时时间
        connectTimeout(10000, TimeUnit.SECONDS)
        //添加写出超时时间
        writeTimeout(10000, TimeUnit.SECONDS)
    }.build()

    val build = retrofit2.Retrofit.Builder()
        .client(mOkHttpClient)
        .baseUrl(TEAM_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return build.create(claszz)
}


fun getShoppingServiceApi(handler: Handler): ShoppingMallApi {
    val mOkHttpClient = OkHttpClient().newBuilder().apply {
        addInterceptor(LoggingInterceptor(handler))
        //添加读取超时时间
        readTimeout(10000, TimeUnit.SECONDS)
        //添加连接超时时间
        connectTimeout(10000, TimeUnit.SECONDS)
        //添加写出超时时间
        writeTimeout(10000, TimeUnit.SECONDS)
    }.build()

    val build = retrofit2.Retrofit.Builder()
        .client(mOkHttpClient)
        .baseUrl(TEAM_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return build.create(ShoppingMallApi::class.java)
}


fun getShoppingServiceApi(): ShoppingMallApi {
    val mOkHttpClient = OkHttpClient().newBuilder().apply {
        addInterceptor(LoggingInterceptor())
        //添加读取超时时间
        readTimeout(10000, TimeUnit.SECONDS)
        //添加连接超时时间
        connectTimeout(10000, TimeUnit.SECONDS)
        //添加写出超时时间
        writeTimeout(10000, TimeUnit.SECONDS)
    }.build()

    val build = retrofit2.Retrofit.Builder()
        .client(mOkHttpClient)
        .baseUrl(TEAM_URI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return build.create(ShoppingMallApi::class.java)
}


class LoggingInterceptor() : Interceptor {
    lateinit var handler: Handler

    constructor(_handler: Handler) : this() {
        handler = _handler
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        val request: Request = chain.request()
        val t1 = System.nanoTime() //请求发起的时间
        Log.i(
            "http", String.format(
                "发送请求 %s on %s%n%s",
                request.url(), chain.connection(), request.headers()
            )
        )
        val response: Response = chain.proceed(request)
        val t2 = System.nanoTime() //收到响应的时间

        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
        //个新的response给应用层处理
        val responseBody = response.peekBody(1024 * 1024)
        val format = String.format(
            "接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
            response.request().url(),
            responseBody.string(),
            (t2 - t1) / 1e6,
            response.headers()
        )

        //TODO 获取网络请求信息
        showLog(format, tag = "http")

        if (request.url().toString().contains("recommend/songs?cookie")) {
            showLog(format)
        }

        /**
         * 注册时判断
         */
        if (request.url().toString().contains("/api/auth/register")) {
            val message = Message()
            if (format.contains("errors")) {
                message.obj = "邮箱已存在"
            } else {
                message.obj = "注册成功"
            }
            handler.sendMessage(message)
        }

        /**
         * 登录时判断
         */
        if (request.url().toString().contains("/api/auth/login")) {
            val message = Message()
            if (format.contains("errors")) {
                message.obj = "邮箱或密码错误"
            }
            handler.sendMessage(message)
        }

        /**
         * 发送邮件判断
         */
        if (request.url().toString().contains("api/auth/reset/password/email/code")) {
            val message = Message()
            if (format.contains("errors")) {
                message.obj = "邮箱不存在或邮箱不合法"
            } else {
                message.obj = "发送成功"
            }
            handler.sendMessage(message)
        }


        /**
         * 找回密码判断
         */
        if (request.url()
                .toString() == "https://api.shop.eduwork.cn/api/auth/reset/password/email"
        ) {
            val message = Message()
            if (format.contains("message")) {
                message.obj = "验证码或邮箱错误"
            } else {
                message.obj = "修改成功"
            }
            handler.sendMessage(message)
        }

        /**
         * 收藏与取消
         */
        if (request.url()
                .toString().contains("/api/collects/goods")
        ) {
            val message = Message()
            if (format.contains("message")) {
                message.obj = "Token过期"
            } else {
                message.obj = "操作成功"
            }
            handler.sendMessage(message)
        }

        /**
         * 添加购物车
         */
        if (request.url()
                .toString().contains("/api/carts") && ::handler.isInitialized
        ) {
            val message = Message()
            if (format.contains("message")) {
                message.obj = "添加失败"
            } else {
                message.obj = "添加成功"
            }
            handler.sendMessage(message)
        }

        /**
         * 添加购物车 || 删除购物车
         */
        if (request.url()
                .toString().contains("/api/carts/") && ::handler.isInitialized
        ) {
            val message = Message()
            if (format.contains("message")) {
                message.obj = "数量变化失败"
            } else {
                message.obj = "数量变化成功"
            }
            handler.sendMessage(message)
        }

        /**
         * 添加购物车
         */
        if (request.url()
                .toString().contains("/api/user") && ::handler.isInitialized
        ) {
            val message = Message()
            if (format.contains("message")) {
                message.obj = "修改失败"
            } else {
                message.obj = "修改成功"
            }
            handler.sendMessage(message)
        }

        /**
         * 地址管理
         */
        if (request.url()
                .toString().contains("/api/address") && ::handler.isInitialized
        ) {
            val message = Message()
            if (format.contains("message")) {
                message.obj = "操作失败"
            } else {
                message.obj = "操作成功"
            }
            handler.sendMessage(message)
        }

        return response
    }
}