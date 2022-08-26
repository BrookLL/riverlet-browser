package com.riverlet.browser.data.sp

import com.riverlet.browser.utils.*
import java.lang.Exception
import java.net.HttpCookie
import java.util.*

object InputRecordSP {
    private const val KEY = "input_record"
    fun putUrl(url: String) {
        val array = getAllUrl()
        if (!array.contains(url)) {
            array.add(url)
            putSP(KEY, array.toJson())
        }
    }

    fun deleteUrl(url: String) {
        val array = getAllUrl()
        array.remove(url)
        putSP(KEY, array.toJson())
    }

    fun getAllUrl(): MutableList<String> {
        val urls = getSP<String>(KEY)
        return (urls?.toObjectCollection(MutableList::class.java, String::class.java)
            ?: mutableListOf<String>()) as MutableList<String>
    }

}