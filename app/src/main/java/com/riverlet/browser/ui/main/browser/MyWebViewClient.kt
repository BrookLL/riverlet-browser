package com.riverlet.browser.ui.main.browser

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AlertDialog

private const val TAG = "WebView"

open class MyWebViewClient(val context: Context) : WebViewClient() {
    private val ignoreSchemeList = arrayListOf<String>() //忽略协议列表
    private val dialogSchemeList = arrayListOf<String>() //忽略协议列表

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        super.onLoadResource(view, url)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        Log.d(TAG, "onReceivedError:${error?.description} code(${error?.errorCode})")
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        Log.d(TAG, "UrlLoading:" + request?.url.toString())
        if (request != null) {
            val scheme = request.url.scheme
            val uri = request.url.toString()
            if (scheme != null && scheme != "https" && scheme != "http") {
                if (ignoreSchemeList.contains(uri)&&dialogSchemeList.contains(uri)) {
                    return true
                }
                showHandleDialog(uri)
                return true
            }

        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    private fun showHandleDialog(uri: String) {
        dialogSchemeList.add(uri)
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle("提醒")
        dialog.setMessage("是否打开App浏览？")
        dialog.setNegativeButton("取消") { dialog, which ->
            dialogSchemeList.remove(uri)
            dialog.dismiss()
        }
        dialog.setNeutralButton("不再提示") { dialog, which ->
            ignoreSchemeList.add(uri)
            dialogSchemeList.remove(uri)
            dialog.dismiss()
        }
        dialog.setPositiveButton("确定") { dialog, which ->
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            try {
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
            dialog.dismiss()
            dialogSchemeList.remove(uri)
        }
        dialog.show()
    }
}