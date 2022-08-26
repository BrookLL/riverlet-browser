package com.riverlet.browser.app

import androidx.lifecycle.ViewModelProvider


inline fun <reified VB : BaseViewModel> BaseActivity.createViewModel(): VB {
    return ViewModelProvider(this)[VB::class.java]
}

inline fun <reified VB : BaseViewModel> BaseFragment.createViewModel(): VB {
    return ViewModelProvider(this)[VB::class.java]
}


