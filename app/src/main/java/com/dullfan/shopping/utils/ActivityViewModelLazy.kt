package com.dullfan.shopping.utils

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider

/**
 * 返回一个 Lazy 委托以访问 ComponentActivity 的 ViewModel，如果指定了 factoryProducer，
 * 则它返回的 ViewModelProvider.Factory 将用于第一次创建 ViewModel。
 */
@MainThread
public inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        defaultViewModelProviderFactory
    }

    return ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
}