package com.eltonkola.adapterz

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonkola.adapterz_lib.CompositeViewRenderZ
import com.eltonkola.adapterz_lib.ViewHolderz
import com.eltonkola.adapterz_lib.ViewRenderZ
import com.eltonkola.adapterz_lib.logz
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_alfabeti_container.view.*
import kotlinx.android.synthetic.main.row_emri.view.*
import kotlinx.android.synthetic.main.row_germa.view.*
import kotlinx.android.synthetic.main.row_header.view.*
import kotlinx.android.synthetic.main.row_koka.view.*

//1
fun headerRenderer(): ViewRenderZ<HeaderItem> {
    return ViewRenderZ (HeaderItem::class){object : ViewHolderz<HeaderItem>(R.layout.row_header, HeaderItem::class) {

        lateinit var title_header: TextView

        override fun initViewHolder(view: View) {
            title_header = view.title_header
        }

        override fun doBind(model: HeaderItem) {
            title_header.text = model.title
            "DoBind >>>>>>>>>>>>>>>>>>>> ${model.title}".logz()
        }
    }}
}

fun kokaRenderer(): ViewRenderZ<KokaItem> {
    return ViewRenderZ (KokaItem::class){object : ViewHolderz<KokaItem>(R.layout.row_koka, KokaItem::class) {

        lateinit var title_koka: TextView

        override fun initViewHolder(view: View) {
            title_koka = view.title_koka
        }

        override fun doBind(model: KokaItem) {
            title_koka.text = model.title
        }
    }}
}


//2
fun alfabetiItemRenderer(): CompositeViewRenderZ<AlfabetiItem> {

    return CompositeViewRenderZ(AlfabetiItem::class,
        {object :
            ViewHolderz<AlfabetiItem>(R.layout.row_alfabeti_container, AlfabetiItem::class) {

            lateinit var container_title: TextView
            lateinit var container_icon: ImageView

            override fun initViewHolder(view: View) {
                container_title = view.container_title
                container_icon = view.container_icon
            }

            override fun doBind(model: AlfabetiItem) {
                container_title.text = model.name
                Picasso.get().load(model.flagUrl).into(container_icon)
            }
        }},
        R.id.childRecycler
    ) {

    }

}

fun germaItemRenderer(): ViewRenderZ<GermaItem> {

    return ViewRenderZ (GermaItem::class){object : ViewHolderz<GermaItem>(R.layout.row_germa, GermaItem::class) {

        lateinit var title_germa: TextView

        override fun initViewHolder(view: View) {
            title_germa = view.title_germa
        }

        override fun doBind(model: GermaItem) {
            title_germa.text = model.germa
        }
    }}
}


////3
fun alfabetiV2ItemRenderer(): CompositeViewRenderZ<AlfabetiV2Item> {

    return CompositeViewRenderZ(AlfabetiV2Item::class, {
        object :
            ViewHolderz<AlfabetiV2Item>(R.layout.row_alfabeti_container, AlfabetiV2Item::class) {

            lateinit var container_title: TextView
            lateinit var container_icon: ImageView

            override fun initViewHolder(view: View) {
                container_title = view.container_title
                container_icon = view.container_icon
            }

            override fun doBind(model: AlfabetiV2Item) {
                container_title.text = model.name
                Picasso.get().load(model.flagUrl).into(container_icon)
            }
        }},
        R.id.childRecycler
    ) { recycler ->

    }
}


fun germaFjaleItemRenderer(): CompositeViewRenderZ<GermaFjaleItem> {

    return CompositeViewRenderZ(GermaFjaleItem::class,{
        object :
            ViewHolderz<GermaFjaleItem>(R.layout.row_alfabeti_fjale, GermaFjaleItem::class) {

            lateinit var container_title: TextView

            override fun initViewHolder(view: View) {
                container_title = view.container_title
            }

            override fun doBind(model: GermaFjaleItem) {
                container_title.text = model.germa
            }
        }},
        R.id.childRecyclerFjalet
    ) { recycler ->
        recycler.layoutManager = LinearLayoutManager(recycler.context, RecyclerView.VERTICAL, false)
        recycler.setHasFixedSize(true)
    }
}


fun fjalaItemRenderer(): ViewRenderZ<FjalaItem> {

    return ViewRenderZ(FjalaItem::class) {object : ViewHolderz<FjalaItem>(R.layout.row_emri, FjalaItem::class) {

        lateinit var title_emri: TextView

        override fun initViewHolder(view: View) {
            title_emri = view.title_emri
        }

        override fun doBind(model: FjalaItem) {
            title_emri.text = model.fjala
        }
    }}

}
