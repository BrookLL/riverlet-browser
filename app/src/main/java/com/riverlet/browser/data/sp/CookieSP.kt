package com.riverlet.browser.data.sp

import com.riverlet.browser.utils.*
import java.lang.Exception

object CookieSP {
    private const val KEY = "cookies"
    fun saveCookie(cookieMap: Map<String, String>) {
        putSP(KEY, cookieMap.toJson())
    }

    fun getCookie(): Map<String, String> {
        val cookies = getSP<String>("cookies", "{}")
        val mapper = getJsonMapper()
        val type = mapper.typeFactory.constructParametricType(
            HashMap::class.java,
            String::class.java,
            String::class.java
        )
        return try {
            mapper.readValue(cookies, type)
        } catch (e: Exception) {
            e.printStackTrace()
            HashMap<String, String>()
        }
    }

}