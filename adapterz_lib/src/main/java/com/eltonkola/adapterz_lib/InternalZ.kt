package com.eltonkola.adapterz_lib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

//internal class, you should not care about this
open class BaseViewHolder<T : BaseDataItem>(view: View) : RecyclerView.ViewHolder(view)

open class BaseComposedViewHolder<T : BaseComposedDataItem>(view: View, val adapterz: AdapterZ) :
    BaseViewHolder<T>(view)


open class ViewRenderZ<T : BaseDataItem>(
    protected val layoutResource: Int,
    protected val doBind: (BaseViewHolder<T>, T) -> Any
) {

    open fun createViewHolder(parent: ViewGroup): BaseViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutResource, parent, false)
        return BaseViewHolder(view)
    }

    open fun bindTo(holder: BaseViewHolder<out BaseDataItem>, item: BaseDataItem) {
        doBind.invoke(holder as BaseViewHolder<T>, item as T)
    }

}

open class CompositeViewRenderZ<T : BaseComposedDataItem>(
    layoutResource: Int,
    doBind: (BaseViewHolder<T>, T) -> Any,
    private val recyclerResourceId: Int,
    private val setupRecycler: (RecyclerView) -> Any = {}
) : ViewRenderZ<T>(layoutResource, doBind) {

    var recycledViewPool: RecyclerView.RecycledViewPool? = null
    protected val renderz = mutableMapOf<Int, ViewRenderZ<out BaseDataItem>>()

    inline fun <reified T : BaseDataItem> addRenderer(rendered: ViewRenderZ<T>) {
        renderz[T::class.java.canonicalName.hashCode()] = rendered
    }

    override fun createViewHolder(parent: ViewGroup): BaseComposedViewHolder<T> {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(layoutResource, parent, false)
        val recycler = view.findViewById<RecyclerView>(recyclerResourceId)
        val childAdapter = AdapterZ(recycledViewPool, renderz)

        val viewHolder = BaseComposedViewHolder<T>(view, childAdapter)

        recycledViewPool?.let {
            recycler.setRecycledViewPool(it)
        }

        recycler.layoutManager = LinearLayoutManager(recycler.context, RecyclerView.HORIZONTAL, false)
        recycler.setHasFixedSize(true)

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recycler)

        recycler.isNestedScrollingEnabled = false
        recycler.adapter = childAdapter

        setupRecycler.invoke(recycler)
        return viewHolder
    }

    override fun bindTo(holder: BaseViewHolder<out BaseDataItem>, item: BaseDataItem) {
        super.bindTo(holder, item)
        with(holder as BaseComposedViewHolder<out BaseComposedDataItem>) {
            this.adapterz.submitList((item as BaseComposedDataItem).getChildren())
        }
    }

}
