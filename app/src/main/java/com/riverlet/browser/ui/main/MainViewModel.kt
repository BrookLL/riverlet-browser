package com.riverlet.browser.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.riverlet.browser.app.BaseViewModel
import com.riverlet.browser.data.sp.InputRecordSP

class MainViewModel(application: Application) : BaseViewModel(application) {

    var url = MutableLiveData<String>()
    val inputState = MutableLiveData<Boolean>()

    fun setUrlInput(url: String) {
        this.url.value = url
        inputState.value = false
        if (url.isBlank()) {
            InputRecordSP.putUrl(url)
        }
    }

    fun getUrl(): String {
        return url.value?:""
    }
}