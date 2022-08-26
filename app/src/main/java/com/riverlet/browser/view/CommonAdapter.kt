package com.riverlet.browser.view

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @auther BG378653
 * @date 2020/12/2 14:20
 * @desc
 */
public abstract class CommonAdapter<T, VB : ViewBinding>() :
    RecyclerView.Adapter<CommonViewHolder<VB>>() {
    private var dataList: MutableList<T> = mutableListOf()

    constructor(dataList: List<T>) : this() {
        this.dataList.clear()
        this.dataList.addAll(dataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<VB> {
        return CommonViewHolder<VB>(getViewBinding(parent))
    }

    abstract fun getViewBinding(parent: ViewGroup): VB

    override fun getItemCount(): Int {
        return dataList.size
    }


    open fun clear() {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setDataList(dataList: List<T>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        notifyDataSetChanged()
    }

    fun getDataList(): List<T> {
        return dataList
    }
    fun getData(position: Int): T? {
        if(position>=dataList.size||position<0){
            return null
        }
        return  dataList[position]
    }

    override fun onBindViewHolder(holder: CommonViewHolder<VB>, position: Int) {
        onBindData(holder.mBinding, position, dataList[position])
    }

    abstract fun onBindData(binding: VB, position: Int, data: T);

}

class CommonViewHolder<VB : ViewBinding>(var mBinding: VB) :
    RecyclerView.ViewHolder(mBinding.root) {

}