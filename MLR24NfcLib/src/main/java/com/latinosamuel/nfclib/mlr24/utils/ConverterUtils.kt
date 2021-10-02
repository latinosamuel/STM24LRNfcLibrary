package com.latinosamuel.nfclib.mlr24.utils

import java.lang.Exception

object ConverterUtils {

    private val hexArray = "0123456789ABCDEF".toCharArray()

    /**
     * Method to convert hex string to byte array
     */
    fun hexStringToByteArray(s: String): ByteArray {
        val len = s.length
        val data = ByteArray(len / 2)
        var i = 0
        while (i < len) {
            data[i / 2] = ((Character.digit(s[i], 16) shl 4) + Character
                .digit(s[i + 1], 16)).toByte()
            i += 2
        }
        return data
    }

    /**
     * Method to convert byte array to string
     */
    @Throws(Exception::class)
    fun getHexString(bytesData: ByteArray): String {
        val hexChars = CharArray(bytesData.size * 2)
        for (j in bytesData.indices) {
            val v: Int = bytesData[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v ushr 4]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }
}