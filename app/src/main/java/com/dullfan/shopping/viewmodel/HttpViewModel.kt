package com.dullfan.shopping.viewmodel

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dullfan.base.http.getShoppingServiceApi
import com.dullfan.base.utils.showLog
import com.dullfan.shopping.adapter.Repository
import com.dullfan.shopping.bean.*
import com.dullfan.shopping.http.*
import com.dullfan.shopping.utils.KV
import com.dullfan.shopping.utils.MMKVData
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HttpViewModel : ViewModel() {
    val registerViewModel: MutableLiveData<ErrorsBean> = MutableLiveData()
    val loginData: MutableLiveData<LoginBean> = MutableLiveData()
    val sendEmailData: MutableLiveData<ErrorsBean> = MutableLiveData()
    val homeData:MutableLiveData<HomeBean> = MutableLiveData()
    val goodsDetailsData:MutableLiveData<GoodsDetailsBean> = MutableLiveData()
    val carsData:MutableLiveData<CarBean> = MutableLiveData()
    val myData:MutableLiveData<MyBean> = MutableLiveData()
    val addressData:MutableLiveData<AddressBean> = MutableLiveData()
    val orderData:MutableLiveData<OrderBean> = MutableLiveData()

    fun getPagingData():Flow<PagingData<HomeData>>{
        return Repository.getPagingData().cachedIn(viewModelScope)
    }

    fun findAddressRequest(){
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi().findAddressListRequest("Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                addressData.value = it
            }
        }
    }

    fun getOrderRequest(){
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi().getOrderRequest("Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                orderData.value = it
            }
        }
    }

    fun findCars(){
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi().findCarts("Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                carsData.value = it
            }
        }
    }

    fun findMyRequest(){
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi().findInfoData("Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                myData.value = it
            }
        }
    }

    fun findGoodsDetailsRequest(id:Int){
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi().findGoodsDetailsRequest(id)
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                goodsDetailsData.value = it
            }
        }
    }

    fun findHomeDataRequest(page:Int,handler: Handler){
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).findHomeDataRequest(page)
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                homeData.value = it
            }
        }
    }

    fun registerViewRequest(username: String, password: String, email: String,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).registerRequest(
                        RegisterBody(
                            username,
                            password,
                            email,
                            password
                        )
                    )
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                registerViewModel.value = it
            }
        }
    }

    fun loginRequest(password: String, email: String,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).registerRequest(
                        LoginBody(
                            email,
                            password
                        )
                    )
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                loginData.value = it
            }
        }
    }

    fun sendEmailRequest(email: String,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).sendEmailRequest(
                        SendEmailBody(email)
                    )
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                sendEmailData.value = it
            }
        }
    }

    fun retrievePasswordRequest(email: String, code: String, password: String,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).retrievePasswordRequest(
                        RetrievePasswordBody(
                            email,
                            code,
                            password,
                            password
                        )
                    )
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {
                registerViewModel.value = it
            }
        }
    }

    fun goodsCollectAndCancelRequest(id:Int,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).goodsCollectAndCancelRequest(id, "Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

    fun addGoodsCar(addCarGoods: AddCarGoods,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).addGoodsCar(addCarGoods, "Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

    fun putCarsNum(carsNumBody: CarsNumBody,id:Int,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).putCarsNum(id,carsNumBody ,"Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

    fun delCarsNum(carsNumBody: CarsNumBody,id:Int,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).delCarsNum(id,carsNumBody ,"Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }


    fun putInfoData(name:String,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).putUserData(PutUserBody(name) ,"Bearer ${KV.decodeString(MMKVData.U_TOKEN)}")
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

    fun defaultAddress(id:Int,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).defaultAddress( "Bearer ${KV.decodeString(MMKVData.U_TOKEN)}",id)
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

    fun delAddress(id:Int,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).delAddress( "Bearer ${KV.decodeString(MMKVData.U_TOKEN)}",id)
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

    fun addAddress(addressBody: AddressBody,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).addAddress( "Bearer ${KV.decodeString(MMKVData.U_TOKEN)}",addressBody)
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

    fun putAddress(addressBody: AddressBody,id:Int,handler: Handler) {
        viewModelScope.launch {
            flow {
                emit(
                    getShoppingServiceApi(handler).putAddress( "Bearer ${KV.decodeString(MMKVData.U_TOKEN)}",
                        addressBody,
                        id
                    )
                )
            }.flowOn(Dispatchers.IO).catch {
                it.printStackTrace()
            }.collect {

            }
        }
    }

}