package com.latinosamuel.nfclib.m24lr

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Handler
import android.os.Looper
import com.latinosamuel.nfclib.m24lr.enumerators.InterruptReason
import com.latinosamuel.nfclib.m24lr.enumerators.TerminationReason
import com.latinosamuel.nfclib.m24lr.enumerators.Process
import com.latinosamuel.nfclib.m24lr.interfaces.*
import com.latinosamuel.nfclib.m24lr.utils.NfcUtils
import java.lang.Exception

class M24LRInit() : M24LRBuilder {

    fun init(context: Context, mProcess: Process, blockNumber: Int): M24LRBuilder {
        process = mProcess
        readBlockNumber = blockNumber
        initialize(context)
        return M24LRInit()
    }

    fun init(context: Context, mProcess: Process, blockNumber: Int, data: String): M24LRBuilder {
        process = mProcess
        readBlockNumber = blockNumber
        writeSingleBlockData = data
        initialize(context)
        return M24LRInit()
    }

    fun init(context: Context, mProcess: Process, blockNumbers: ArrayList<Int>): M24LRBuilder {
        process = mProcess
        readBlockNumbers = blockNumbers
        initialize(context)
        return M24LRInit()
    }

    fun init(context: Context, mProcess: Process, data: HashMap<Int,String>): M24LRBuilder {
        process = mProcess
        writeMultipleBlocksData = data
        initialize(context)
        return M24LRInit()
    }

    fun cancelProcess(activity: Activity){
        NfcUtils.disableReaderMode(activity)
    }

    fun removeListeners(){
        completedListener = null
        errorListener = null
        iTagListener = null
    }

    companion object {
        private var LRInstance: M24LRInstance? = null
        private var tag: Tag? = null
        private var process: Process? = null
        private var isProcessRun = false
        private var completedListener: CompletedListener? = null
        private var errorListener: ErrorListener? = null
        private var iTagListener: ITagListener? = null
        private var readBlockNumber: Int? = null
        private var writeSingleBlockData: String? = null
        private var readBlockNumbers: ArrayList<Int>? = null
        private var writeMultipleBlocksData: HashMap<Int,String>? = null

        private fun initialize(context: Context) {
            if (LRInstance == null) {
                LRInstance = M24LRInstance(context)
            } else {
                LRInstance?.setContext(context)
            }
        }

        /**
         * Method that discovers the Tag and lets you know if the Tag has been removed from the NFC board
         */
        private fun startTagDiscovered(activity: Activity) {
            val nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
            if (nfcAdapter != null && nfcAdapter.isEnabled) {
                nfcAdapter.enableReaderMode(activity,
                    { tag ->
                        if (!isProcessRun){
                            isProcessRun = true
                            iTagListener?.onTagFound()
                            when(process){
                                Process.READ_SINGLE_BLOCK -> {
                                    onStartReadSingleBlock(activity, tag)
                                }

                                Process.WRITE_SINGLE_BLOCK -> {
                                    onStartWriteSingleBlock(activity, tag)
                                }

                                Process.READ_MULTIPLE_BLOCK -> {
                                    onStartReadMultipleBlocks(activity, tag)
                                }

                                Process.WRITE_MULTIPLE_BLOCK -> {
                                    onStartWriteMultipleBlocks(activity, tag)
                                }
                            }
                        }
                        nfcAdapter.ignore(tag, 1000, {
                        }, Handler(Looper.getMainLooper()))
                    }, NfcAdapter.FLAG_READER_NFC_V or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
            } else {
                isProcessRun = false
                errorListener?.onProcessInterrupted(InterruptReason.NFC_NOT_ENABLE)
                NfcUtils.disableReaderMode(activity)
            }
        }

        /**
         *  Method that allows reading single block in st25 chip from the discovered Tag
         */
        private fun onStartReadSingleBlock(activity: Activity, tag: Tag?) {
            Companion.tag = tag
            onStartReadSingleBlock(activity)
        }

        /**
         * Read single blocks in st25 chip
         */
        private fun onStartReadSingleBlock(activity: Activity) {
            try {
                if (LRInstance != null) {
                    LRInstance?.readSingleBlock(activity, tag, readBlockNumber!!, errorListener, completedListener)
                    isProcessRun = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorListener?.onProcessTerminated(TerminationReason.ERROR, e.printStackTrace().toString())
                isProcessRun = false
            }
        }

        /**
         *  Method that allows writing single block in st25 chip from the discovered Tag
         */
        private fun onStartWriteSingleBlock(activity: Activity, tag: Tag?) {
            Companion.tag = tag
            onStartWriteSingleBlock(activity)
        }

        /**
         * Write single blocks in st25 chip
         */
        private fun onStartWriteSingleBlock(activity: Activity) {
            try {
                if (LRInstance != null) {
                    LRInstance?.writeSingleBlock(activity, tag, readBlockNumber!!, writeSingleBlockData!!, errorListener, completedListener)
                    isProcessRun = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorListener?.onProcessTerminated(TerminationReason.ERROR, e.printStackTrace().toString())
                isProcessRun = false
            }
        }

        /**
         *  Method that allows reading blocks in st25 chip from the discovered Tag
         */
        private fun onStartReadMultipleBlocks(activity: Activity, tag: Tag?) {
            Companion.tag = tag
            readMultipleBlocks(activity)
        }

        /**
         * Read multiple blocks in st25 chip
         */
        private fun readMultipleBlocks(activity: Activity) {
            try {
                if (LRInstance != null) {
                    LRInstance?.readMultipleBlocks(activity, tag, readBlockNumbers!!, errorListener, completedListener)
                    isProcessRun = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorListener?.onProcessTerminated(TerminationReason.ERROR, e.printStackTrace().toString())
                isProcessRun = false
            }
        }

        /**
         *  Method that allows writing blocks in st25 chip from the discovered Tag
         */
        private fun onStartWriteMultipleBlocks(activity: Activity, tag: Tag?) {
            Companion.tag = tag
            writeMultipleBlocks(activity)
        }

        /**
         * Write multiple blocks in st25 chip
         */
        private fun writeMultipleBlocks(activity: Activity) {
            try {
                if (LRInstance != null) {
                    LRInstance?.writeMultipleBlocks(activity, tag, writeMultipleBlocksData!!, errorListener, completedListener)
                    isProcessRun = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorListener?.onProcessTerminated(TerminationReason.ERROR, e.printStackTrace().toString())
                isProcessRun = false
            }
        }
    }

    override fun completedListener(listener: CompletedListener?): M24LRBuilder {
        completedListener = listener!!
        return this
    }

    override fun errorListener(listener: ErrorListener?): M24LRBuilder {
        errorListener = listener!!
        return this
    }

    override fun iTagListener(listener: ITagListener?): M24LRBuilder {
        iTagListener = listener!!
        return this
    }

    override fun build(activity: Activity) {
        startTagDiscovered(activity)
    }
}