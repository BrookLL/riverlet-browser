package com.riverlet.browser.ui.bookmark

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.riverlet.browser.R
import com.riverlet.browser.data.model.BookMark
import com.riverlet.browser.data.sp.BookmarkSP
import com.riverlet.browser.data.sp.InputRecordSP
import com.riverlet.browser.databinding.ActivityBookmarksBinding
import com.riverlet.browser.databinding.ItemBookmarkBinding
import com.riverlet.browser.databinding.ItemRecordBinding
import com.riverlet.browser.view.CommonAdapter

class BookmarksActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityBookmarksBinding
    private lateinit var bookmarkList: MutableList<BookMark>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBookmarksBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
    }

    private fun initView() {
        mBinding.list.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )
        mBinding.list.adapter = mAdapter
        bookmarkList = BookmarkSP.getAll()
        refresh()
    }

    private fun refresh() {
        mAdapter.setDataList(bookmarkList)
    }

    private val mAdapter = object : CommonAdapter<BookMark, ItemBookmarkBinding>() {
        override fun getViewBinding(parent: ViewGroup) =
            ItemBookmarkBinding.inflate(layoutInflater, parent, false)

        override fun onBindData(binding: ItemBookmarkBinding, position: Int, data: BookMark) {
            binding.textTitle.text = data.title
            binding.textUrl.text = data.url
            binding.imageCopy.setOnClickListener {
                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, data.url))
                Toast.makeText(this@BookmarksActivity, "已复制", Toast.LENGTH_SHORT).show()
            }
            binding.imageDelete.setOnClickListener {
                bookmarkList.remove(data)
                BookmarkSP.delete(data)
                refresh()
            }
            binding.root.setOnClickListener {
                setResult(RESULT_OK, Intent().run {
                    putExtra("url", data.url)
                    this
                })
                finish()
            }
        }

    }
}