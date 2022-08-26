package com.riverlet.browser.ui.main.browser

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.riverlet.browser.R
import com.riverlet.browser.app.BaseFragment
import com.riverlet.browser.data.model.Website
import com.riverlet.browser.data.sp.CookieSP
import com.riverlet.browser.databinding.FragmentBrowserBinding
import com.riverlet.browser.ui.bookmark.BookmarksActivity
import com.riverlet.browser.ui.main.MainViewModel
import com.riverlet.browser.utils.setSimpleClickEffect
import com.riverlet.browser.view.MenuPopup

private const val TAG = "WebView"

class BrowserFragment : BaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): BrowserFragment {
            return BrowserFragment()
        }
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mBinding: FragmentBrowserBinding
    private lateinit var url: String


    private val cookieMap = hashMapOf<String, String>()
    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBrowserBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cookieMap.putAll(CookieSP.getCookie())
        val manager = CookieManager.getInstance()
        for (key in cookieMap.keys) {
            manager.setCookie(key, cookieMap[key])
        }

        launcher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val url = result.data?.getStringExtra("url")
                    url?.let {
                        this.url = it
                        loadUrl()
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        url = viewModel.getUrl()
        viewModel.url.observe(viewLifecycleOwner) {
            if (url != it) {
                url = it
                loadUrl()
            }
        }
        initWebWetting()
        mBinding.clTitle.setOnClickListener(this)
        mBinding.imageBack.setSimpleClickEffect()
        mBinding.imageBack.setOnClickListener(this)
        mBinding.imageNext.setSimpleClickEffect()
        mBinding.imageNext.setOnClickListener(this)
        mBinding.imageReload.setSimpleClickEffect()
        mBinding.imageReload.setOnClickListener(this)
        mBinding.imageHome.setSimpleClickEffect()
        mBinding.imageHome.setOnClickListener(this)
        mBinding.imageMenu.setSimpleClickEffect()
        mBinding.imageMenu.setOnClickListener(this)
        loadUrl()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.clTitle -> {
                viewModel.inputState.value = true
            }
            R.id.imageBack -> {
                if (mBinding.webView.canGoBack()) {
                    mBinding.webView.goBack()
                } else {
                    viewModel.setUrlInput("")
                }
            }
            R.id.imageNext -> {
                mBinding.webView.goForward()
            }
            R.id.imageReload -> {
                mBinding.webView.reload()
            }
            R.id.imageHome -> {
                viewModel.setUrlInput("")
            }
            R.id.imageMenu -> {
                showBottomMenu()
            }
            else -> {
            }
        }

    }

    private fun showBottomMenu() {
        MenuPopup(mBinding.root).setWebsite(
            Website(
                mBinding.webView.title ?: "",
                mBinding.webView.url ?: ""
            )
        ).setOnMenuItemClickListener(object : MenuPopup.OnMenuItemClickListener {
            override fun onMenuItemClick(menuId: Int) {
                if (menuId == MenuPopup.MenuId.MENU_BOOKMARKS) {
                    launcher.launch(Intent(activity, BookmarksActivity::class.java))
                }
            }

        }).show()
    }

    private fun initWebWetting() {
        with(mBinding.webView.settings) {
            javaScriptEnabled = true
            loadsImagesAutomatically = true //支持自动加载图片
            useWideViewPort = true //将图片调整到适合webview的大小
            loadWithOverviewMode = true // 缩放至屏幕的大小
            setSupportZoom(false) //支持缩放，默认为true。是下面那个的前提。
            builtInZoomControls = false //设置内置的缩放控件。若为false，则该WebView不可缩放
            displayZoomControls = false //隐藏原生的缩放控件
            allowFileAccess = true//设置可以访问文件
            javaScriptCanOpenWindowsAutomatically = true//支持通过JS打开新窗口
            defaultTextEncodingName = "utf-8"//设置编码格式
            domStorageEnabled = true
        }
        mBinding.webView.webViewClient = object : MyWebViewClient(mBinding.webView.context) {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                Log.d(TAG, "UrlLoading:" + request?.url.toString())
                if (request != null) {
                    request.url.host?.let { checkCookie(it) }
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        mBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                Log.d(TAG, "newProgress: $newProgress")
                if (newProgress in 1..99) {
                    mBinding.progressBar.visibility = View.VISIBLE
                    mBinding.progressBar.progress = newProgress
                } else {
                    mBinding.progressBar.visibility = View.GONE
                    mBinding.progressBar.progress = 0
                }
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                if (TextUtils.equals("首页", title)) {
                    mBinding.textTitle.text = ""
                } else {
                    mBinding.textTitle.text = title
                }

            }
        }
        mBinding.webView.addJavascriptInterface(this, "native")
    }

    private fun checkCookie(host: String) {
        val cookie = CookieManager.getInstance().getCookie(host)
        if (cookie != null) {
            Log.d(TAG, "cookie:$cookie")
            cookieMap[host] = cookie
            CookieSP.saveCookie(cookieMap)
        }
    }


    @JavascriptInterface
    fun inputUrl() {
        activity?.runOnUiThread {
            viewModel.inputState.value = true
        }
    }

    fun onBackPressed(): Boolean {
        return if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack()
            true
        } else {
            false
        }
    }

    private fun loadUrl() {
        Log.d(TAG, "LoadUrl:$url")
        if (url.isBlank()) {
            return
        }
        mBinding.webView.loadUrl(checkUrl(url))
    }

    private fun checkUrl(url: String): String {
        if (url.startsWith("http") || url.startsWith("https")) {
            return url
        }
        if (!URLUtil.isValidUrl(url) && !url.contains('.')) {
            return "http://www.baidu.com/s?wd=$url"
        }
        return "https://$url"
    }

}