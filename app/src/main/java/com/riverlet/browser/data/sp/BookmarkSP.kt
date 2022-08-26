package com.riverlet.browser.data.sp

import android.webkit.URLUtil
import com.riverlet.browser.data.model.BookMark
import com.riverlet.browser.utils.*
import java.util.*

object BookmarkSP {
    private const val KEY = "bookmarks"

    fun getAll(): MutableList<BookMark> {
        val bookmarks = getSP<String>(KEY)
        return (bookmarks?.toObjectCollection(MutableList::class.java, BookMark::class.java)
            ?: mutableListOf<BookMark>()) as MutableList<BookMark>
    }

    fun put(bookmark: BookMark) {
        if (checkBookMark(bookmark)) {
            val bookmarks = getAll()
            bookmarks.add(bookmark)
            putSP(KEY, bookmarks.toJson())
        }
    }

    private fun checkBookMark(bookmark: BookMark): Boolean {
        return URLUtil.isNetworkUrl(bookmark.url)
    }

    fun delete(bookmark: BookMark) {
        val bookmarks = getAll()
        bookmarks.remove(bookmark)
        putSP(KEY, bookmarks.toJson())
    }

    fun contains(bookmark: BookMark): Boolean {
        val bookmarks = getAll()
        return bookmarks.contains(bookmark)
    }
}