package com.riverlet.browser.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.ViewModelProvider
import com.riverlet.browser.R
import com.riverlet.browser.app.BaseActivity
import com.riverlet.browser.databinding.ActivityMainBinding
import com.riverlet.browser.ui.main.browser.BrowserFragment
import com.riverlet.browser.ui.main.home.HomeFragment
import com.riverlet.browser.ui.main.input.InputFragment


class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var mBinding: ActivityMainBinding
    private val inputFragment: InputFragment = InputFragment.newInstance()
    private val browserFragment: BrowserFragment = BrowserFragment.newInstance()
    private val homeFragment: HomeFragment = HomeFragment.newInstance()
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        supportFragmentManager.beginTransaction()
            .add(R.id.container, homeFragment)
            .commitNow()
        supportFragmentManager.beginTransaction().show(homeFragment)
        viewModel.inputState.observe(this) {
            if (it) {
                inputFragment.show(supportFragmentManager)
            } else {
                inputFragment.dismiss()
                url = viewModel.getUrl()
                if (url.isNotBlank()) {
                    if (!browserFragment.isAdded) {
                        supportFragmentManager.beginTransaction()
                            .add(R.id.container, browserFragment)
                            .commitNow()
                    }
                    supportFragmentManager.beginTransaction().show(browserFragment).commitNow()
                } else {
                    supportFragmentManager.beginTransaction().hide(browserFragment).commitNow()
                    supportFragmentManager.beginTransaction().show(homeFragment).commitNow()
                }
            }
        }

        if (savedInstanceState != null) {
            url = savedInstanceState.getString(url, "")
        }
    }

    override fun onBackPressed() {
        if (browserFragment.onBackPressed()) {
            return
        }
        if (!homeFragment.isVisible){
            supportFragmentManager.beginTransaction().hide(browserFragment).commitNow()
            supportFragmentManager.beginTransaction().show(homeFragment).commitNow()
            return
        }
        val home = Intent(Intent.ACTION_MAIN)
        home.addCategory(Intent.CATEGORY_HOME)
        startActivity(home)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putString("url", url)
        super.onSaveInstanceState(outState, outPersistentState)
    }
}
