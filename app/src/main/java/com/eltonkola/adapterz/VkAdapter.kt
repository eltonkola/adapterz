package com.eltonkola.adapterz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eltonkola.adapterz_lib.BaseDataItem
import com.eltonkola.adapterz_lib.getIdz
import kotlin.reflect.KClass

class VkAdapter(private var renders: MutableMap<Int, VkRenderer<*>> = mutableMapOf()) :
    ListAdapter<BaseDataItem, VkViewHolder<out BaseDataItem>>(DIFF_CALLBACK) {

    fun addRenderer(rendered: VkRenderer<out BaseDataItem>) {
        renders[rendered.type] = rendered
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VkViewHolder<*> {
        return renders[viewType]!!.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: VkViewHolder<out BaseDataItem>, position: Int) {
        renders[getItemViewType(position)]?.bindTo(holder, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemViewType()
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BaseDataItem>() {
            override fun areItemsTheSame(oldItem: BaseDataItem, newItem: BaseDataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: BaseDataItem, newItem: BaseDataItem): Boolean {
                return oldItem.hashCodeZ() == newItem.hashCodeZ()
            }
        }
    }

}


open class VkRenderer<T : BaseDataItem>(
    private val layout: Int,
    private val rendererFactory: () -> VhRenderer<T>,
    tClass: KClass<T>
) {

    lateinit var layoutInflater: LayoutInflater

    open fun createViewHolder(parent: ViewGroup): VkViewHolder<out BaseDataItem> {
        if (!::layoutInflater.isInitialized) {
            layoutInflater = LayoutInflater.from(parent.context)
        }

        val view = layoutInflater.inflate(layout, parent, false)
        return VkViewHolder(view, rendererFactory.invoke())
    }

    fun bindTo(holder: VkViewHolder<out BaseDataItem>, item: BaseDataItem) {
        holder.doBind(item)
    }

    val type = tClass.getIdz()

}

class VkViewHolder<T : BaseDataItem>(view: View, val vr: VhRenderer<T>) :
    RecyclerView.ViewHolder(view) {

    init {
        vr.initUi(view)
    }

    fun doBind(item: BaseDataItem) {
        item as T
        vr.doBind(item)
    }
}

open class VhRenderer<T>() {

    open fun initUi(view: View) {

    }

    open fun doBind(item: T) {

    }
}
