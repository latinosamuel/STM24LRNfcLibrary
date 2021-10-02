package com.latinosamuel.nfclib.mlr24.utils

import android.nfc.Tag
import com.latinosamuel.nfclib.mlr24.models.Result
import java.util.ArrayList
import java.util.HashMap


object NfcvFunction {

    fun readSingleBlock(tag: Tag?, blockNumber: Int): Result {
        var result = Result()

        if (tag == null) {
            return result
        }

        val readSingleBlockResponse = NfcvUtils.readHexBlocksData(tag, arrayListOf(blockNumber))
        if (readSingleBlockResponse.isError) return readSingleBlockResponse

        val singleBlockData = readSingleBlockResponse.multipleBlocks.entries.first()
        result = Result(false, singleBlockData.key, singleBlockData.value)
        return result
    }

    fun writeSingleBlock(tag: Tag?, blockNumber: Int, data: String): Result {
        val result = Result()

        if (tag == null) {
            return result
        }

        val writeSingleBlockResponse =
            NfcvUtils.writeHexBlocksDataFromMap(tag, hashMapOf(blockNumber to data))
        if (writeSingleBlockResponse.isError) return writeSingleBlockResponse

        return result
    }

    fun readMultipleBlocks(tag: Tag?, blockNumbers: ArrayList<Int>): Result {
        var result = Result()

        if (tag == null) {
            return result
        }

        val readMultipleBlocksResponse = NfcvUtils.readHexBlocksData(tag, blockNumbers)
        if (readMultipleBlocksResponse.isError) return readMultipleBlocksResponse

        result = Result(false, readMultipleBlocksResponse.multipleBlocks)
        return result
    }

    fun writeMultipleBlocks(tag: Tag?, data: HashMap<Int, String>): Result {
        val result = Result()

        if (tag == null) {
            return result
        }

        val writeMultipleBlocksResponse = NfcvUtils.writeHexBlocksDataFromMap(tag, data)
        if (writeMultipleBlocksResponse.isError) return writeMultipleBlocksResponse

        return result
    }
}