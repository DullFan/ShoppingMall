package com.dullfan.shopping.http

import com.dullfan.shopping.bean.*
import retrofit2.http.*

interface ShoppingMallApi {

    /**
     * 注册
     */
    @POST("/api/auth/register")
    suspend fun registerRequest(@Body registerBody: RegisterBody): ErrorsBean

    /**
     * 登录
     */
    @POST("/api/auth/login")
    suspend fun registerRequest(@Body loginBody: LoginBody): LoginBean

    /**
     * 发送邮箱验证码
     */
    @POST("/api/auth/reset/password/email/code")
    suspend fun sendEmailRequest(@Body sendEmailBody: SendEmailBody): ErrorsBean

    /**
     * 找回密码
     */
    @POST("/api/auth/reset/password/email")
    suspend fun retrievePasswordRequest(@Body retrievePasswordBody: RetrievePasswordBody): ErrorsBean

    /**
     * 获取首页数据
     */
    @GET("/api/index")
    suspend fun findHomeDataRequest(@Query("page") page: Int): HomeBean

    /**
     * 获取商品详情
     */
    @GET("/api/goods/{id}")
    suspend fun findGoodsDetailsRequest(@Path("id") id: Int): GoodsDetailsBean

    /**
     * 收藏和取消
     */
    @POST("/api/collects/goods/{id}")
    suspend fun goodsCollectAndCancelRequest(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): String

    /**
     * 收藏和取消
     */
    @POST("/api/carts")
    suspend fun addGoodsCar(
        @Body addCarGoods: AddCarGoods,
        @Header("Authorization") token: String
    ): String

    /**
     * 获取商品详情
     */
    @GET("/api/carts")
    suspend fun findCarts(@Header("Authorization") token: String): CarBean

    /**
     * 修改购物车数量
     */
    @PUT("/api/carts/{cart}}")
    suspend fun putCarsNum(
        @Path("cart") id: Int,
        @Body carsNumBody: CarsNumBody,
        @Header("Authorization") token: String
    ): CarBean

    /**
     * 移除购物车
     */
    @HTTP(method = "DELETE", path = "/api/carts/{cart}", hasBody = true)
    suspend fun delCarsNum(
        @Path("cart") id: Int,
        @Body carsNumBody: CarsNumBody,
        @Header("Authorization") token: String
    ): CarBean

    /**
     * 获取用户信息
     */
    @GET("/api/user")
    suspend fun findInfoData(@Header("Authorization") token: String): MyBean

    /**
     * 更新用户信息
     */
    @PUT("/api/user")
    suspend fun putUserData(
        @Body putUserBody: PutUserBody,
        @Header("Authorization") token: String
    ): String

    /**
     * 获取地址列表
     */
    @GET("/api/address")
    suspend fun findAddressListRequest(@Header("Authorization") token: String): AddressBean

    /**
     * 设置默认地址
     */
    @PATCH("/api/address/{address}/default")
    suspend fun defaultAddress(
        @Header("Authorization") token: String,
        @Path("address") address: Int
    ): String

    /**
     * 删除地址
     */
    @DELETE("/api/address/{address}")
    suspend fun delAddress(
        @Header("Authorization") token: String,
        @Path("address") address: Int
    ): String

    /**
     * 添加地址
     */
    @POST("/api/address")
    suspend fun addAddress(
        @Header("Authorization") token: String,
        @Body addressBody: AddressBody
    ): String

    /**
     * 修改地址
     */
    @PUT("/api/address/{address}")
    suspend fun putAddress(
        @Header("Authorization") token: String,
        @Body addressBody: AddressBody,
        @Path("address") address:Int
    ): String

    /**
     * 获取订单列表
     */
    @GET("/api/orders")
    suspend fun getOrderRequest(
        @Header("Authorization") token: String
    ): OrderBean


}

data class RegisterBody(
    val name: String,
    val password: String,
    val email: String,
    val password_confirmation: String
)

data class AddressBody(
    val name: String,
    val address: String,
    val phone: String,
    val province: String,
    val city: String,
    val county: String,
    val is_default: String,
)

data class LoginBody(val email: String, val password: String)

data class CarsNumBody(val num: String)

data class SendEmailBody(val email: String)

data class PutUserBody(val name: String)

data class AddCarGoods(val goods_id: String, val num: String)

data class RetrievePasswordBody(
    val email: String,
    val code: String,
    val password: String,
    val password_confirmation: String
)
