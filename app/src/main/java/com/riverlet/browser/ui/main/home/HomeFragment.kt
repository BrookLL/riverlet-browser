package com.riverlet.browser.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.riverlet.browser.app.BaseFragment
import com.riverlet.browser.app.createViewModel
import com.riverlet.browser.databinding.FragmentHomeBinding
import com.riverlet.browser.ui.main.MainViewModel

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mBinding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.llInput.setOnClickListener {
            viewModel.inputState.value = true
        }
    }
}