package com.eltonkola.adapterz_lib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

//internal class, you should not care about this
open class BaseViewHolder<T : BaseDataItem>(view: View, val holder: ViewHolderz<T>) : RecyclerView.ViewHolder(view) {
    init {
        holder.initViewHolder(view)
    }
}

open class BaseComposedViewHolder<T : BaseComposedDataItem>(view: View, holder: ViewHolderz<T>, val adapter: AdapterZ) :
    BaseViewHolder<T>(view, holder) {

    init {
        holder.initViewHolder(view)
    }

}


abstract class ViewHolderz<T : BaseDataItem>(val layoutResource: Int, val tClass: KClass<T>) {

    open fun initViewHolder(view: View) {

    }

    open fun doBind(model: T) {

    }

    fun getTypez(): Int {
        ">>> BaseDataItem canonicalName: = ${tClass.java.canonicalName}".logz()
        ">>> ViewRenderZ hashCode: = ${tClass.getIdz()}".logz()
        return tClass.getIdz()
    }
}

open class ViewRenderZ<T : BaseDataItem>(val viewHolder: ViewHolderz<T>) {

    open fun createViewHolder(parent: ViewGroup): BaseViewHolder<out BaseDataItem> {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewHolder.layoutResource, parent, false)

        return BaseViewHolder(view, viewHolder)
    }

    open fun bindTo(holder: BaseViewHolder<out BaseDataItem>, item: BaseDataItem) {
        (holder as BaseViewHolder<T>).holder.doBind(item as T)
    }

    val typez = viewHolder.getTypez()

}


open class CompositeViewRenderZ<T : BaseComposedDataItem>(
    viewHolder: ViewHolderz<T>,
    private val recyclerResourceId: Int,
    private val setupRecycler: (RecyclerView) -> Any = {}
) : ViewRenderZ<T>(viewHolder) {

    var recycledViewPool: RecyclerView.RecycledViewPool? = null

    private val renderz = mutableMapOf<Int, ViewRenderZ<out BaseDataItem>>()

//    inline fun <reified T : BaseDataItem> addRenderer(rendered: ViewRenderZ<T>) {
//        renderz[T::class.java.canonicalName.hashCode()] = rendered
//
//    }

    fun addRenderer(rendered: ViewRenderZ<out BaseDataItem>) {
        renderz[rendered.typez] = rendered
    }


    override fun createViewHolder(parent: ViewGroup): BaseComposedViewHolder<T> {

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewHolder.layoutResource, parent, false)
        val recycler = view.findViewById<RecyclerView>(recyclerResourceId)
        val childAdapter = AdapterZ(recycledViewPool, renderz)

//        childAdapter.renderz


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

        return BaseComposedViewHolder(view, viewHolder, childAdapter)
    }

    override fun bindTo(holder: BaseViewHolder<out BaseDataItem>, item: BaseDataItem) {
        super.bindTo(holder, item)
        (holder as BaseComposedViewHolder<T>).adapter.submitList((item as BaseComposedDataItem).getChildren())
    }

}

