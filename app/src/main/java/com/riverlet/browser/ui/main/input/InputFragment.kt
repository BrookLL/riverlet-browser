package com.riverlet.browser.ui.main.input

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.riverlet.browser.R
import com.riverlet.browser.data.sp.InputRecordSP
import com.riverlet.browser.databinding.FragmentInputBinding
import com.riverlet.browser.databinding.ItemRecordBinding
import com.riverlet.browser.ui.main.MainViewModel
import com.riverlet.browser.utils.setSimpleClickEffect
import com.riverlet.browser.view.CommonAdapter
import java.util.*

private const val TAG = "InputFragment"

class InputFragment : AppCompatDialogFragment() {

    companion object {
        fun newInstance() = InputFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mBinding: FragmentInputBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentInputBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        setStyle(STYLE_NO_FRAME, R.style.Dialog_FullScreen)
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window?.let {
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.editInputUrl.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                showWeb()
            }
            false
        }
        mBinding.editInputUrl.addTextChangedListener {
            mBinding.imgClear.isVisible = it?.length != 0
        }
        mBinding.imgClear.setSimpleClickEffect()
        mBinding.imgClear.setOnClickListener {
            mBinding.editInputUrl.setText("")
        }
        mBinding.textLoad.setSimpleClickEffect()
        mBinding.textLoad.setOnClickListener {
            showWeb()
        }
        mBinding.list.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL,false)
        mBinding.list.adapter = mAdapter
        refreshRecord()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.inputState.value = false
    }


    private fun refreshRecord() {
        mAdapter.setDataList(InputRecordSP.getAllUrl())
    }

    private fun showWeb() {
        val url = mBinding.editInputUrl.text.toString().trim()
        if (url.isNotBlank()) {
            viewModel.setUrlInput(url)

            refreshRecord()
        }
    }

    private val mAdapter = object : CommonAdapter<String, ItemRecordBinding>() {
        override fun getViewBinding(parent: ViewGroup) =
            ItemRecordBinding.inflate(LayoutInflater.from(requireActivity()), parent, false)

        override fun onBindData(binding: ItemRecordBinding, position: Int, data: String) {
            binding.textUrl.text = data
            binding.imageCopy.setOnClickListener {
                val clipboardManager =
                    activity!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText(null, data))
                Toast.makeText(activity, "已复制", Toast.LENGTH_SHORT).show()
            }
            binding.imageDelete.setOnClickListener {
                InputRecordSP.deleteUrl(data)
                refreshRecord()
            }
            binding.root.setOnClickListener {
                mBinding.editInputUrl.setText(data)
                showWeb()
            }
        }

    }

    fun show(manager: FragmentManager) {
        super.show(manager, TAG)
    }

    fun showNow(manager: FragmentManager) {
        super.showNow(manager, TAG)
    }


    override fun dismiss() {
        if (isVisible) {
            super.dismissAllowingStateLoss()
        }
    }

}