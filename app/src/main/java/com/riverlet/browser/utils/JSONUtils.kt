package com.riverlet.browser.utils

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import java.lang.Exception
import java.lang.reflect.Type

private val objectMapper: ObjectMapper by lazy { ObjectMapper() }

fun <T> String.toObject(clz: Class<T>): T? {
    try {
        return objectMapper.readValue(this, clz)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun <T> String.toObjectCollection(collectionClass: Class<T>, vararg parameterClasses: Class<*>): T? {
    try {
        val type =
            objectMapper.typeFactory.constructParametricType(collectionClass, *parameterClasses)
        return objectMapper.readValue(this, type)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


fun Any.toJson(): String? {
    try {
        return objectMapper.writeValueAsString(this)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

fun getJsonMapper() = objectMapper