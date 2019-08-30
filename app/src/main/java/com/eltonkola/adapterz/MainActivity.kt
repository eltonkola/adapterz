package com.eltonkola.adapterz

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonkola.adapterz_lib.AdapterZ
import com.eltonkola.adapterz_lib.BaseComposedDataItem
import com.eltonkola.adapterz_lib.BaseDataItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.row_alfabeti_container.view.*
import kotlinx.android.synthetic.main.row_germa.view.*
import kotlinx.android.synthetic.main.row_header.view.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)

        val pool = RecyclerView.RecycledViewPool()

        val adapter = AdapterZ(pool)

        //simple binders
        adapter.addRenderer(headerRenderer())
        adapter.addRenderer(kokaRenderer())


        //one level deep composed binder
        adapter.addRenderer(alfabetiItemRenderer().apply {
            addRenderer(germaItemRenderer())
        })


        //two level deep composed binder

        adapter.addRenderer(alfabetiV2ItemRenderer().apply {
            addRenderer(germaFjaleItemRenderer().apply {
                addRenderer(fjalaItemRenderer())
            })
        })

        //title_germa
        val vkAdapter = VkAdapter()

        vkAdapter.addRenderer(VkRenderer(R.layout.row_header, {
            object : VhViewHolder<HeaderItem> {

                lateinit var title_header: TextView

                override fun initUi(view: View) {
                    title_header = view.title_header
                }

                override fun doBind(item: HeaderItem) {
                    title_header.text = item.title
                }
            }
        }, HeaderItem::class))


        val germaVh = VkRenderer(R.layout.row_germa, {
            object : VhViewHolder<GermaItem> {

                lateinit var title_germa: TextView

                override fun initUi(view: View) {
                    title_germa = view.title_germa
                }

                override fun doBind(item: GermaItem) {
                    title_germa.text = item.germa
                }
            }
        }, GermaItem::class)

        vkAdapter.addRenderer(
            VkCompositeRenderer(R.layout.row_alfabeti_container, {
                object : VhViewHolder<AlfabetiItem> {

                    lateinit var container_title: TextView
                    lateinit var container_icon: ImageView

                    override fun initUi(view: View) {
                        container_title = view.container_title
                        container_icon = view.container_icon
                    }

                    override fun doBind(item: AlfabetiItem) {
                        container_title.text = item.name
                        Picasso.get().load(item.flagUrl).into(container_icon)
                    }
                }
            }, AlfabetiItem::class, R.id.childRecycler).apply { addRenderer(germaVh) })


                    recyclerView . adapter = vkAdapter


                    viewModel.dataList.observe(this, Observer { list ->
                        adapter.submitList(list)
                        vkAdapter.submitList(list)

                    })
                    recyclerView . layoutManager = LinearLayoutManager (this,
            RecyclerView.VERTICAL,
            false
        )
        recyclerView.setHasFixedSize(true)
//        recyclerView.adapter = adapter


        fab.setOnClickListener { view ->
            viewModel.refreshData()
        }
    }
}

data class HeaderItem(val title: String) : BaseDataItem

data class KokaItem(val title: String) : BaseDataItem


// 1 level
data class GermaItem(val germa: String) : BaseDataItem

data class AlfabetiItem(val name: String, val flagUrl: String, val germat: List<GermaItem>) :
    BaseComposedDataItem {
    override fun getChildren(): List<GermaItem> {
        return germat
    }

}


//2 levels
data class FjalaItem(val fjala: String) : BaseDataItem

data class AlfabetiV2Item(
    val name: String,
    val flagUrl: String,
    val germatFjalet: List<GermaFjaleItem>
) :
    BaseComposedDataItem {
    override fun getChildren(): List<GermaFjaleItem> {
        return germatFjalet
    }

}

data class GermaFjaleItem(val germa: String, val fjalet: List<FjalaItem>) : BaseComposedDataItem {
    override fun getChildren(): List<FjalaItem> {
        return fjalet
    }

}


//vm

class DemoViewModel : ViewModel() {

    val dataList = MutableLiveData<List<BaseDataItem>>()

    init {
        refreshData()
    }

    fun refreshData() {

        val data = mutableListOf<BaseDataItem>()

        data.add(
            AlfabetiItem("Shqip",
                "https://raw.githubusercontent.com/gosquared/flags/master/flags/flags/shiny/64/Albania.png",
                listOf(
                    "A", "B", "C", "Ç", "D", "Dh", "E", "Ë", "F", "G", "Gj",
                    "H", "I", "J", "K", "L", "Ll", "M", "N", "Nj", "O", "P", "Q",
                    "R", "Rr", "S", "Sh", "T", "Th", "U", "V", "X", "Xh", "Y", "Z", "Zh"
                ).map { GermaItem(it) })
        )


        data.add(
            AlfabetiV2Item(
                "Emra Shqip",
                "https://raw.githubusercontent.com/gosquared/flags/master/flags/flags/shiny/64/Albania.png",

                listOf(
                    GermaFjaleItem(
                        "A",
                        listOf("Alban", "Artan", "Arjan", "Azgan", "AAAA").map { FjalaItem(it) }),
                    GermaFjaleItem(
                        "B",
                        listOf("Besa", "Besmira", "Besjan", "Blearta").map { FjalaItem(it) }),
                    GermaFjaleItem(
                        "C",
                        listOf("Cen", "Cuf", "Car", "Cjap", "CCCC").map { FjalaItem(it) }),
                    GermaFjaleItem(
                        "D",
                        listOf("Dora", "Dorina", "Dorjana", "Doreta").map { FjalaItem(it) }),
                    GermaFjaleItem(
                        "E",
                        listOf("Elton", "Erion", "Elvis", "Ervin", "EEEEE").map { FjalaItem(it) }),
                    GermaFjaleItem(
                        "F",
                        listOf("Flori", "Feridi", "Fani", "Funi").map { FjalaItem(it) }),
                    GermaFjaleItem(
                        "G",
                        listOf("Goni", "Gimi", "Gani", "Gili", "GGG").map { FjalaItem(it) }),
                    GermaFjaleItem(
                        "H",
                        listOf("Harli", "Hurli", "Harbi", "Heri").map { FjalaItem(it) })
                )

            )
        )


        for (i in 0..500) {
            if (Random.nextBoolean()) {
                data.add(HeaderItem("AA $i"))
            } else {
                data.add(KokaItem("ku ku ku $i"))
            }
        }


        data.clear()

        data.add(
            AlfabetiItem("Shqip",
                "https://raw.githubusercontent.com/gosquared/flags/master/flags/flags/shiny/64/Albania.png",
                listOf(
                    "A", "B", "C", "Ç", "D", "Dh", "E", "Ë", "F", "G", "Gj",
                    "H", "I", "J", "K", "L", "Ll", "M", "N", "Nj", "O", "P", "Q",
                    "R", "Rr", "S", "Sh", "T", "Th", "U", "V", "X", "Xh", "Y", "Z", "Zh"
                ).map { GermaItem(it) })
        )


        for (i in 0..100) {
            data.add(HeaderItem("Element $i"))
        }



        dataList.postValue(data)
    }

}
