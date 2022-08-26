package com.riverlet.browser.utils

import android.content.Context
import android.content.SharedPreferences
import com.riverlet.browser.app.App
import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap


private val sp: SharedPreferences by lazy {
    App.getAppContext().getSharedPreferences("_record", Context.MODE_PRIVATE)
}

fun <T> getSP(key: String, defValue: Any? = null): T? {
    return (if (sp.all[key] == null) defValue else sp.all[key]) as? T
}

fun putSP(key: String, value: Any?) {
    with(sp.edit()) {
        when (value) {
            is Int -> putInt(key, value)
            is Float -> putFloat(key, value)
            is Long -> putLong(key, value)
            is Boolean -> putBoolean(key, value)
            is String -> putString(key, value)
            else -> throw IllegalArgumentException("SharedPreference can't be save this type")
        }
    }.apply()
}

fun putSP(key: String, value: Set<String>) {
    sp.edit().putStringSet(key, value).apply()
}

