package com.eltonkola.adapterz

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.eltonkola.adapterz_lib.BaseComposedDataItem
import com.eltonkola.adapterz_lib.BaseDataItem
import com.eltonkola.adapterz_lib.getIdz
import kotlin.reflect.KClass

class VkAdapter(
    private var renders: MutableMap<Int, VkRenderer<*>> = mutableMapOf(),
    private var recycledViewPool: RecyclerView.RecycledViewPool? = null
) :
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


open class VkCompositeRenderer<T : BaseDataItem>(
    layout: Int,
    rendererFactory: () -> VhViewHolder<T>,
    tClass: KClass<T>,
    val recyclerResourceId: Int,
    private val setupRecycler: (RecyclerView) -> Any = {},
    private var renders: MutableMap<Int, VkRenderer<*>> = mutableMapOf(),
    private var recycledViewPool: RecyclerView.RecycledViewPool? = null,
    private val snap: Boolean = false
) : VkRenderer<T>(layout, rendererFactory, tClass) {


    fun addRenderer(rendered: VkRenderer<out BaseDataItem>) {
        renders[rendered.type] = rendered
    }

    private val childAdapter = VkAdapter(renders, recycledViewPool)

    override fun createViewHolder(parent: ViewGroup): VkViewHolder<out BaseDataItem> {
        val holder = super.createViewHolder(parent)

        val recycler = holder.view.findViewById<RecyclerView>(recyclerResourceId)

        recycledViewPool?.let {
            recycler.setRecycledViewPool(it)
        }
        recycler.layoutManager =
            LinearLayoutManager(recycler.context, RecyclerView.HORIZONTAL, false)
        recycler.setHasFixedSize(true)

        if (snap) {
            val snapHelper = LinearSnapHelper()
            snapHelper.attachToRecyclerView(recycler)
        }
        recycler.isNestedScrollingEnabled = false
        recycler.adapter = childAdapter

        setupRecycler.invoke(recycler)

        return holder
    }

    override fun bindTo(holder: VkViewHolder<out BaseDataItem>, item: BaseDataItem) {
        super.bindTo(holder, item)
        childAdapter.submitList((item as BaseComposedDataItem).getChildren())
    }
}

open class VkRenderer<T : BaseDataItem>(
    private val layout: Int,
    private val rendererFactory: () -> VhViewHolder<T>,
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

    open fun bindTo(holder: VkViewHolder<out BaseDataItem>, item: BaseDataItem) {
        holder.doBind(item)
    }

    val type = tClass.getIdz()

}

class VkViewHolder<T : BaseDataItem>(val view: View, val vr: VhViewHolder<T>) :
    RecyclerView.ViewHolder(view) {

    init {
        vr.initUi(view)
    }

    fun doBind(item: BaseDataItem) {
        item as T
        vr.doBind(item)
    }
}

interface VhViewHolder<T> {
    fun initUi(view: View)
    fun doBind(item: T)
}
