package com.eltonkola.adapterz

import android.os.Bundle
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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
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
        /*
        adapter.addRenderer(ViewRenderZ<HeaderItem>(R.layout.row_header) { vh, model ->
            vh.itemView.title_header.text = model.title
            true
        })

        adapter.addRenderer(ViewRenderZ<KokaItem>(R.layout.row_koka) { vh, model ->
            vh.itemView.title_koka.text = model.title
            true
        })
        */

        adapter.addRenderer(headerRenderer())
        adapter.addRenderer(kokaRenderer())


        //one level deep composed binder
        /*
        adapter.addRenderer(CompositeViewRenderZ<AlfabetiItem>(
            R.layout.row_alfabeti_container,
            { vh, model ->
                vh.itemView.container_title.text = model.name
                Picasso.get().load(model.flagUrl).into(vh.itemView.container_icon)
                true
            },
            R.id.childRecycler,
            { recycler ->
                true
            }
        ).apply {
            addRenderer(ViewRenderZ<GermaItem>(R.layout.row_germa) { vh, model ->
                vh.itemView.title_germa.text = model.germa
                true
            })
        })
        */

        adapter.addRenderer(alfabetiItemRenderer().apply {
            addRenderer(germaItemRenderer())
        })


        //two level deep composed binder
        /*
        adapter.addRenderer(CompositeViewRenderZ<AlfabetiV2Item>(
            R.layout.row_alfabeti_container,
            { vh, model ->
                vh.itemView.container_title.text = model.name
                Picasso.get().load(model.flagUrl).into(vh.itemView.container_icon)
                true
            },
            R.id.childRecycler,
            { recycler ->
                true
            }
        ).apply {

            addRenderer(CompositeViewRenderZ<GermaFjaleItem>(
                R.layout.row_alfabeti_fjale,
                { vh, model ->
                    vh.itemView.container_title.text = model.germa
                    true
                },
                R.id.childRecyclerFjalet,
                { recycler ->
                    recycler.layoutManager = LinearLayoutManager(recycler.context, RecyclerView.VERTICAL, false)
                    recycler.setHasFixedSize(true)
                    true
                }
            ).apply {
                addRenderer(ViewRenderZ<FjalaItem>(R.layout.row_emri) { vh, model ->
                    vh.itemView.title_emri.text = model.fjala
                    true
                })
            })


        })
        */

        adapter.addRenderer(alfabetiV2ItemRenderer().apply {
            addRenderer(germaFjaleItemRenderer().apply {
                addRenderer(fjalaItemRenderer())
            })
        })

        //title_germa


        viewModel.dataList.observe(this, Observer { list ->
            adapter.submitList(list)
        })
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        fab.setOnClickListener { view ->
            viewModel.refreshData()
        }
    }

}

data class HeaderItem(val title: String) : BaseDataItem

data class KokaItem(val title: String) : BaseDataItem


// 1 level
data class GermaItem(val germa: String) : BaseDataItem

data class AlfabetiItem(val name: String, val flagUrl: String, val germat: List<GermaItem>) : BaseComposedDataItem {
    override fun getChildren(): List<GermaItem> {
        return germat
    }

}


//2 levels
data class FjalaItem(val fjala: String) : BaseDataItem

data class AlfabetiV2Item(val name: String, val flagUrl: String, val germatFjalet: List<GermaFjaleItem>) :
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
                    GermaFjaleItem("A", listOf("Alban", "Artan", "Arjan", "Azgan", "AAAA").map { FjalaItem(it) }),
                    GermaFjaleItem("B", listOf("Besa", "Besmira", "Besjan", "Blearta").map { FjalaItem(it) }),
                    GermaFjaleItem("C", listOf("Cen", "Cuf", "Car", "Cjap", "CCCC").map { FjalaItem(it) }),
                    GermaFjaleItem("D", listOf("Dora", "Dorina", "Dorjana", "Doreta").map { FjalaItem(it) }),
                    GermaFjaleItem("E", listOf("Elton", "Erion", "Elvis", "Ervin", "EEEEE").map { FjalaItem(it) }),
                    GermaFjaleItem("F", listOf("Flori", "Feridi", "Fani", "Funi").map { FjalaItem(it) }),
                    GermaFjaleItem("G", listOf("Goni", "Gimi", "Gani", "Gili", "GGG").map { FjalaItem(it) }),
                    GermaFjaleItem("H", listOf("Harli", "Hurli", "Harbi", "Heri").map { FjalaItem(it) })
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


        dataList.postValue(data)
    }

}
