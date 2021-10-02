package com.latinosamuel.nfclib.m24lr.interfaces

import android.app.Activity

interface M24lrBuilder {

    fun completedListener(listener: CompletedListener?): M24lrBuilder

    fun errorListener(listener: ErrorListener?): M24lrBuilder

    fun iTagListener(listener: ITagListener?): M24lrBuilder

    fun build(activity: Activity)
}