package com.riverlet.browser.view

import android.app.Activity
import android.os.Looper
import android.view.*
import android.widget.PopupWindow
import com.riverlet.browser.R
import com.riverlet.browser.data.model.BookMark
import com.riverlet.browser.data.model.Website
import com.riverlet.browser.data.sp.BookmarkSP
import com.riverlet.browser.databinding.MenuPopupBinding
import com.riverlet.browser.utils.setSimpleClickEffect

class MenuPopup(private val baseView: View) : PopupWindow(), View.OnClickListener {

    object MenuId {
        const val MENU_MARK_BOOKMARK = 1
        const val MENU_BOOKMARKS = 2
    }

    interface OnMenuItemClickListener {
        fun onMenuItemClick(menuId: Int)
    }

    private var currentWebsite: Website? = null
    private val mBinding = MenuPopupBinding.inflate(LayoutInflater.from(baseView.context))
    private var mOnMenuItemClickListener: OnMenuItemClickListener? = null

    init {
        this.contentView = mBinding.root
        this.width = WindowManager.LayoutParams.MATCH_PARENT
        mBinding.root.measure(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        this.height = mBinding.root.measuredHeight
        this.isOutsideTouchable = true
        animationStyle = R.style.BottomAnimation

        mBinding.textMarkBookmark.setSimpleClickEffect()
        mBinding.textMarkBookmark.setOnClickListener(this)
        mBinding.textBookmarks.setSimpleClickEffect()
        mBinding.textBookmarks.setOnClickListener(this)
        this.setOnDismissListener {
            setWindowAlpha(1.0f)
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.textMarkBookmark -> {
                currentWebsite?.let {
                    val bookMark = BookMark(it.title, it.url)
                    if (BookmarkSP.contains(bookMark)) {
                        BookmarkSP.delete(bookMark)
                    } else {
                        BookmarkSP.put(bookMark)
                    }
                }
                mOnMenuItemClickListener?.onMenuItemClick(MenuId.MENU_MARK_BOOKMARK)
            }
            R.id.textBookmarks -> {
                mOnMenuItemClickListener?.onMenuItemClick(MenuId.MENU_BOOKMARKS)
            }
            else -> {
            }
        }
        dismiss()
    }

    fun setOnMenuItemClickListener(listener: OnMenuItemClickListener): MenuPopup {
        this.mOnMenuItemClickListener = listener
        return this
    }

    fun show() {
        prepareShow()
        setWindowAlpha(0.5f)
        super.showAsDropDown(baseView, 0, 0, Gravity.BOTTOM)
    }

    private fun prepareShow() {
        currentWebsite?.let {
            if (BookmarkSP.contains(BookMark(it.title, it.url))) {
                mBinding.textMarkBookmark.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    R.drawable.ic_delete_bookmark,
                    0,
                    0
                )
                mBinding.textMarkBookmark.text = "删除书签"
            }
        }
    }

    private fun setWindowAlpha(alpha: Float) {
        if (baseView.context is Activity) {
            val window = (baseView.context as Activity).window
            window.attributes = window.attributes.also { it.alpha = alpha }
        }
    }

    fun setWebsite(website: Website): MenuPopup {
        this.currentWebsite = website
        return this
    }
}