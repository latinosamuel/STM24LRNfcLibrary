package com.latinosamuel.nfclib.mlr24.utils

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.provider.Settings
import com.latinosamuel.nfclib.mlr24.MLR24Init

object NfcUtils {

    /**
     * Method to open NFC settings
     */
    fun openNfcSettings(activity: Activity) {
        val nfcSettingsIntent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Intent(Settings.Panel.ACTION_NFC)
        } else {
            Intent(Settings.ACTION_NFC_SETTINGS)
        }
        nfcSettingsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.applicationContext.startActivity(nfcSettingsIntent)
    }

    /**
     * NFC disable reader mode
     */
    fun disableReaderMode(activity: Activity?) {
        if (activity != null && !activity.isDestroyed){
            NfcAdapter.getDefaultAdapter(activity)?.disableReaderMode(activity)
            NfcAdapter.getDefaultAdapter(activity)?.disableForegroundDispatch(activity)
            MLR24Init().removeListeners()
        }
    }
}