package com.eltonkola.adapterz

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eltonkola.adapterz_lib.CompositeViewRenderZ
import com.eltonkola.adapterz_lib.ViewRenderZ
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_alfabeti_container.view.*
import kotlinx.android.synthetic.main.row_alfabeti_fjale.view.container_title
import kotlinx.android.synthetic.main.row_emri.view.*
import kotlinx.android.synthetic.main.row_germa.view.*
import kotlinx.android.synthetic.main.row_header.view.*
import kotlinx.android.synthetic.main.row_koka.view.*

//1
fun headerRenderer(): ViewRenderZ<HeaderItem> {
    return ViewRenderZ(R.layout.row_header) { vh, model ->
        vh.itemView.title_header.text = model.title
        true
    }
}

fun kokaRenderer(): ViewRenderZ<KokaItem> {
    return ViewRenderZ(R.layout.row_koka) { vh, model ->
        vh.itemView.title_koka.text = model.title
        true
    }
}


//2
fun alfabetiItemRenderer(): CompositeViewRenderZ<AlfabetiItem> {
    return CompositeViewRenderZ(
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
    )
}

fun germaItemRenderer(): ViewRenderZ<GermaItem> {
    return ViewRenderZ(R.layout.row_germa) { vh, model ->
        vh.itemView.title_germa.text = model.germa
        true
    }
}


//3
fun alfabetiV2ItemRenderer(): CompositeViewRenderZ<AlfabetiV2Item> {
    return CompositeViewRenderZ(
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
    )
}


fun germaFjaleItemRenderer(): CompositeViewRenderZ<GermaFjaleItem> {
    return CompositeViewRenderZ(
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
    )
}

fun fjalaItemRenderer(): ViewRenderZ<FjalaItem> {
    return ViewRenderZ(R.layout.row_emri) { vh, model ->
        vh.itemView.title_emri.text = model.fjala
        true
    }
}
