package com.latinosamuel.nfclib.mlr24.interfaces

import android.app.Activity

interface MLR24Builder {

    fun completedListener(listener: CompletedListener?): MLR24Builder

    fun errorListener(listener: ErrorListener?): MLR24Builder

    fun iTagListener(listener: ITagListener?): MLR24Builder

    fun build(activity: Activity)
}