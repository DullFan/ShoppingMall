package com.dullfan.shopping.utils

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.reflect.KClass


/**
 * 返回一个属性委托以访问父活动的 ViewModel，如果指定了 factoryProducer，则它返回的 ViewModelProvider.Factory 将用于第一次创建 ViewModel。
 * 否则，将使用活动的 androidx.activity.ComponentActivity.getDefaultViewModelProviderFactory。
 */
@MainThread
public inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> = createViewModelLazy(
    VM::class, { ownerProducer().viewModelStore },
    factoryProducer ?: {
        (ownerProducer() as? HasDefaultViewModelProviderFactory)?.defaultViewModelProviderFactory
            ?: defaultViewModelProviderFactory
    }
)

/**
 * 用于创建 ViewModelLazy 的辅助方法，它将作为 factoryProducer 传递的 null 解析为默认工厂。
 */
@MainThread
public fun <VM : ViewModel> Fragment.createViewModelLazy(
    viewModelClass: KClass<VM>,
    storeProducer: () -> ViewModelStore,
    factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }
    return ViewModelLazy(viewModelClass, storeProducer, factoryPromise)
}