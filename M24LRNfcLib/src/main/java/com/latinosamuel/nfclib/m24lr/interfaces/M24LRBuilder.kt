package com.latinosamuel.nfclib.m24lr.interfaces

import android.app.Activity

interface M24LRBuilder {

    fun completedListener(listener: CompletedListener?): M24LRBuilder

    fun errorListener(listener: ErrorListener?): M24LRBuilder

    fun iTagListener(listener: ITagListener?): M24LRBuilder

    fun build(activity: Activity)
}