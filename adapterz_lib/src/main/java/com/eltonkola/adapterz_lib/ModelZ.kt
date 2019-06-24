package com.eltonkola.adapterz_lib

interface BaseDataItem {

    fun getItemViewType(): Int {
        return this::class.getIdz()
    }

    open fun hashCodeZ(): Int {
        return this.hashCode()
    }
}

interface BaseComposedDataItem : BaseDataItem {
    fun getChildren(): List<BaseDataItem>
}
