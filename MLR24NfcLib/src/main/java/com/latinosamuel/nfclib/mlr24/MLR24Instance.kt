package com.latinosamuel.nfclib.mlr24

import android.app.Activity
import android.content.Context
import android.nfc.Tag
import com.latinosamuel.nfclib.mlr24.enumerators.TerminationReason
import com.latinosamuel.nfclib.mlr24.interfaces.CompletedListener
import com.latinosamuel.nfclib.mlr24.interfaces.ErrorListener
import com.latinosamuel.nfclib.mlr24.models.Result
import com.latinosamuel.nfclib.mlr24.utils.NfcUtils.disableReaderMode
import com.latinosamuel.nfclib.mlr24.utils.NfcvFunction
import java.lang.Exception
import java.lang.ref.WeakReference

class MLR24Instance(context: Context?) {
    private var context: WeakReference<Context?>? = null

    init {
        setContext(context)
    }

    fun setContext(context: Context?) {
        this.context = WeakReference(context)
    }

    /**
     * Read single block from st 25 chip
     */
    fun readSingleBlock(activity: Activity, tag: Tag?, blockNumber: Int, errorListener: ErrorListener?, completedListener: CompletedListener?) {
        try {
            val result: Result = NfcvFunction.readSingleBlock(tag, blockNumber)
            if (result.isError){
                errorListener?.onProcessTerminated(result.terminationReason!!, result.error!!)
            }else{
                completedListener?.onProcessCompletedReadSingleBlock(result.singleBlockNumber!!, result.singleBlockHexData!!)
            }
            disableReaderMode(activity)
        } catch (e: Exception) {
            e.printStackTrace()
            errorListener?.onProcessTerminated(TerminationReason.ERROR, e.message.toString())
            disableReaderMode(activity)
        }
    }

    /**
     * Write single block from st 25 chip
     */
    fun writeSingleBlock(activity: Activity, tag: Tag?, blockNumber: Int, data: String, errorListener: ErrorListener?, completedListener: CompletedListener?) {
        try {
            val result: Result = NfcvFunction.writeSingleBlock(tag, blockNumber, data)
            if (result.isError){
                errorListener?.onProcessTerminated(result.terminationReason!!, result.error!!)
            }else{
                completedListener?.onProcessCompletedWriteSingleBlock()
            }
            disableReaderMode(activity)
        } catch (e: Exception) {
            e.printStackTrace()
            errorListener?.onProcessTerminated(TerminationReason.ERROR, e.message.toString())
            disableReaderMode(activity)
        }
    }

    /**
     * Read multiple blocks from st 25 chip
     */
    fun readMultipleBlocks(activity: Activity, tag: Tag?, blockNumbers: ArrayList<Int>, errorListener: ErrorListener?, completedListener: CompletedListener?) {
        try {
            val result: Result = NfcvFunction.readMultipleBlocks(tag, blockNumbers)
            if (result.isError){
                errorListener?.onProcessTerminated(result.terminationReason!!, result.error!!)
            }else{
                completedListener?.onProcessCompletedReadMultipleBlock(result.multipleBlocks)
            }
            disableReaderMode(activity)
        } catch (e: Exception) {
            e.printStackTrace()
            errorListener?.onProcessTerminated(TerminationReason.ERROR, e.message.toString())
            disableReaderMode(activity)
        }
    }

    /**
     * Write multiple blocks from st 25 chip
     */
    fun writeMultipleBlocks(activity: Activity, tag: Tag?, data: HashMap<Int, String>, errorListener: ErrorListener?, completedListener: CompletedListener?) {
        try {
            val result: Result = NfcvFunction.writeMultipleBlocks(tag, data)
            if (result.isError){
                errorListener?.onProcessTerminated(result.terminationReason!!, result.error!!)
            }else{
                completedListener?.onProcessCompletedWriteMultipleBlock()
            }
            disableReaderMode(activity)
        } catch (e: Exception) {
            e.printStackTrace()
            errorListener?.onProcessTerminated(TerminationReason.ERROR, e.message.toString())
            disableReaderMode(activity)
        }
    }
}