package com.latinosamuel.nfclib.mlr24

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Handler
import android.os.Looper
import com.latinosamuel.nfclib.mlr24.enumerators.InterruptReason
import com.latinosamuel.nfclib.mlr24.enumerators.TerminationReason
import com.latinosamuel.nfclib.mlr24.enumerators.Process
import com.latinosamuel.nfclib.mlr24.interfaces.*
import com.latinosamuel.nfclib.mlr24.utils.NfcUtils
import java.lang.Exception

class MLR24Init() : MLR24Builder {

    fun init(context: Context, mProcess: Process, blockNumber: Int): MLR24Builder {
        process = mProcess
        readBlockNumber = blockNumber
        initialize(context)
        return MLR24Init()
    }

    fun init(context: Context, mProcess: Process, blockNumber: Int, data: String): MLR24Builder {
        process = mProcess
        readBlockNumber = blockNumber
        writeSingleBlockData = data
        initialize(context)
        return MLR24Init()
    }

    fun init(context: Context, mProcess: Process, blockNumbers: ArrayList<Int>): MLR24Builder {
        process = mProcess
        readBlockNumbers = blockNumbers
        initialize(context)
        return MLR24Init()
    }

    fun init(context: Context, mProcess: Process, data: HashMap<Int,String>): MLR24Builder {
        process = mProcess
        writeMultipleBlocksData = data
        initialize(context)
        return MLR24Init()
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
        private var instance: MLR24Instance? = null
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
            if (instance == null) {
                instance = MLR24Instance(context)
            } else {
                instance?.setContext(context)
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
                if (instance != null) {
                    instance?.readSingleBlock(activity, tag, readBlockNumber!!, errorListener, completedListener)
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
                if (instance != null) {
                    instance?.writeSingleBlock(activity, tag, readBlockNumber!!, writeSingleBlockData!!, errorListener, completedListener)
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
                if (instance != null) {
                    instance?.readMultipleBlocks(activity, tag, readBlockNumbers!!, errorListener, completedListener)
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
                if (instance != null) {
                    instance?.writeMultipleBlocks(activity, tag, writeMultipleBlocksData!!, errorListener, completedListener)
                    isProcessRun = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                errorListener?.onProcessTerminated(TerminationReason.ERROR, e.printStackTrace().toString())
                isProcessRun = false
            }
        }
    }

    override fun completedListener(listener: CompletedListener?): MLR24Builder {
        completedListener = listener!!
        return this
    }

    override fun errorListener(listener: ErrorListener?): MLR24Builder {
        errorListener = listener!!
        return this
    }

    override fun iTagListener(listener: ITagListener?): MLR24Builder {
        iTagListener = listener!!
        return this
    }

    override fun build(activity: Activity) {
        startTagDiscovered(activity)
    }
}