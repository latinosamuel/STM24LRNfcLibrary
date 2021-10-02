package com.latinosamuel.nfclib.m24lr.utils

import android.nfc.Tag
import android.nfc.tech.NfcV
import android.util.Log
import com.latinosamuel.nfclib.m24lr.enumerators.TerminationReason
import java.io.IOException
import java.lang.Exception
import com.latinosamuel.nfclib.m24lr.models.Result

object NfcvUtils {

    var TAG = "NfcvFunction"

    /**
     * Allows you to fetch all hexadecimal values associated with each block
     */
    fun readHexBlocksData(tag: Tag?, blocksNumber: ArrayList<Int>): Result {

        var result = Result()
        if (tag == null) {
            return result
        }

        val hexDataValues: HashMap<Int,String> = HashMap()
        for (key in blocksNumber){
            val tech = NfcV.get(tag)
            if (tech != null) {
                try {
                    tech.connect()
                    val isConnected = tech.isConnected
                    if (isConnected){
                        try {
                            val response = tech.transceive(getReadSingleBlockCommand(key))
                            val data = ConverterUtils.getHexString(response)
                            hexDataValues[key] = data.substring(2, 10)
                        }catch (e: Exception){
                            e.printStackTrace()
                            return Result(true, TerminationReason.ERROR, e.message)
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    return Result(true, TerminationReason.ERROR, e.message)
                } finally {
                    try {
                        tech.close()
                    } catch (e: IOException) {
                        result = Result(true, TerminationReason.ERROR, e.message)
                        break
                    }
                }
            }
        }

        if (!result.isError){
            result = Result(false, hexDataValues)
        }
        return result
    }

    /**
     * Allows writing each block of a map composed of the
     * block number and the message in hexadecimal
     */
    fun writeHexBlocksDataFromMap(tag: Tag?, hexBlockMapData: HashMap<Int,String>): Result {
        var result = Result()

        if (tag == null) {
            return result
        }

        for ((hexBlockMapDataKey, hexBlockMapDataValue) in hexBlockMapData) {
            val tech = NfcV.get(tag)
            if (tech != null) {
                try {
                    tech.connect()
                    val isConnected = tech.isConnected
                    if (isConnected){
                        val cmd = writeSingleBlockCommand(ConverterUtils.hexStringToByteArray(hexBlockMapDataValue), hexBlockMapDataKey)
                        val response = tech.transceive(cmd)
                        if (response[0] == 0.toByte()) {
                            Log.d(TAG, "Writing data success to block " + hexBlockMapDataKey
                                    + " [" + ConverterUtils.getHexString(cmd) + "]")
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    return Result(true, TerminationReason.ERROR, e.message)
                } finally {
                    try {
                        tech.close()
                    } catch (e: IOException) {
                        result = Result(true, TerminationReason.ERROR, e.message)
                        break
                    }
                }
            }
        }
        return result
    }

    /**
     * Method with the command to read a single block
     */
    private fun getReadSingleBlockCommand(block: Int): ByteArray {
        return byteArrayOf(
            0x0A, // Flags
            0x20, // Read single block
            (block and 0x0ff).toByte(), // Address of starting block (first 8bit)
            0x00 // Address (second 8bit)
        )
    }

    /**
     * Method with the command to write a single block
     */
    private fun writeSingleBlockCommand(data: ByteArray, block: Int): ByteArray {
        return byteArrayOf(
            0x0A.toByte(),  // FLAGS
            0x21.toByte(),  // WRITE SINGLE COMMAND
            (block and 0x0ff).toByte(),  // OFFSET
            0x00, // Address (second 8bit)
            data[0], // block to write 1st byte
            data[1], // block to write 2nd byte
            data[2], // block to write 3rd byte
            data[3]  // block to write 4th byte
        )
    }
}