package com.eltonkola.adapterz_lib

import android.util.Log
import kotlin.reflect.KClass

fun String.logz() {
    Log.v("adapterz_lib >>> ", this)
}


fun KClass<out BaseDataItem>.getIdz(): Int {
//    ">>> BaseDataItem canonicalName: = ${this.java.canonicalName}".logz()
//    ">>> BaseDataItem getItemViewType: = ${this.java.canonicalName.hashCode()}".logz()
//    return this.java.canonicalName.hashCode()
    return this.hashCode()
}