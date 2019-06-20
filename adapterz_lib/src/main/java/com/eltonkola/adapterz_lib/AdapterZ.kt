package com.eltonkola.adapterz_lib

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


class AdapterZ(
    private var recycledViewPool: RecyclerView.RecycledViewPool? = null,
    protected val renderz: MutableMap<Int, ViewRenderZ<out BaseDataItem>> = mutableMapOf()
) :
    ListAdapter<BaseDataItem, BaseViewHolder<out BaseDataItem>>(DIFF_CALLBACK) {


    inline fun <reified T : BaseDataItem> addRenderer(rendered: ViewRenderZ<T>) {
        renderz[T::class.java.canonicalName.hashCode()] = rendered
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemViewType()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<out BaseDataItem> {
        val renderer = renderz[viewType]
        recycledViewPool?.let {
            if (renderer is CompositeViewRenderZ) {
                renderer.recycledViewPool = it
            }
        }
        return renderer!!.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<out BaseDataItem>, position: Int) {
        val renderer = renderz[getItemViewType(position)]
        renderer?.bindTo(holder, getItem(position))
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
