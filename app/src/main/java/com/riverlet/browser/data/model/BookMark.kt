package com.riverlet.browser.data.model

import android.text.TextUtils

data class BookMark(
    var title: String,
    var url: String
) {
    constructor() : this("","")
    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return true
        }
        if (other is BookMark) {
            return TextUtils.equals(other.url, url)
        }
        return super.equals(other)
    }
}
