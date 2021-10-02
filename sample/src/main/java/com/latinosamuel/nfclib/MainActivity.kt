package com.latinosamuel.nfclib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.latinosamuel.nfclib.mlr24.MLR24Init
import com.latinosamuel.nfclib.mlr24.enumerators.InterruptReason
import java.util.*
import kotlin.collections.HashMap
import  com.latinosamuel.nfclib.mlr24.enumerators.Process
import com.latinosamuel.nfclib.mlr24.enumerators.TerminationReason
import com.latinosamuel.nfclib.mlr24.interfaces.CompletedListener
import com.latinosamuel.nfclib.mlr24.interfaces.ErrorListener
import com.latinosamuel.nfclib.mlr24.interfaces.ITagListener
import com.latinosamuel.nfclib.mlr24.utils.NfcUtils

class MainActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)

        val readSingleBlockBtn = findViewById<Button>(R.id.readSingleBlockBtn)
        val textView = findViewById<TextView>(R.id.textView)

        /** Read single block **/
        readSingleBlockBtn.setOnClickListener {

            // Block number to read
            val blockNumber = 0
            MLR24Init().init(this@MainActivity, Process.READ_SINGLE_BLOCK, blockNumber)
                .completedListener(object : CompletedListener {
                    override fun onProcessCompletedReadSingleBlock(blockNumber: Int, hexData: String) {
                        runOnUiThread {
                            val text = getString(R.string.read_single_block_success, blockNumber.toString(), hexData)
                            textView.text = text
                            Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }
                }).errorListener(object : ErrorListener {
                    override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
                        runOnUiThread {
                            textView.text = reason
                            Toast.makeText(this@MainActivity, reason, Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }

                    override fun onProcessInterrupted(interruptReason: InterruptReason) {
                        runOnUiThread {
                            if (InterruptReason.NFC_NOT_ENABLE == interruptReason){
                                NfcUtils.openNfcSettings(this@MainActivity)
                            }else{
                                textView.text = interruptReason.toString()
                                Toast.makeText(this@MainActivity, interruptReason.toString(), Toast.LENGTH_LONG).show()
                            }
                            showProgressBar(false)
                        }
                    }
                }).iTagListener(object : ITagListener {
                    override fun onTagFound() {
                        runOnUiThread {
                            showProgressBar(true)
                        }
                    }
                }).build(this@MainActivity)
        }

        /** Write Single Block **/
        val writeSingleBlockBtn = findViewById<Button>(R.id.writeSingleBlockBtn)
        writeSingleBlockBtn.setOnClickListener {

            //Block number to write
            val blockNumber = 0
            //Data to write in the respective block in hexadecimal
            val data = "01506201"

            MLR24Init().init(this@MainActivity, Process.WRITE_SINGLE_BLOCK, blockNumber, data)
                .completedListener(object : CompletedListener{
                    override fun onProcessCompletedWriteSingleBlock() {
                        runOnUiThread {
                            textView.text = getString(R.string.message_success)
                            Toast.makeText(this@MainActivity, getString(R.string.message_success), Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }
                }).errorListener(object :  ErrorListener{
                    override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
                        runOnUiThread {
                            textView.text = reason
                            Toast.makeText(this@MainActivity, reason, Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }
                    override fun onProcessInterrupted(interruptReason: InterruptReason) {
                        runOnUiThread {
                            if (InterruptReason.NFC_NOT_ENABLE == interruptReason){
                                NfcUtils.openNfcSettings(this@MainActivity)
                            }else{
                                textView.text = interruptReason.toString()
                                Toast.makeText(this@MainActivity, interruptReason.toString(), Toast.LENGTH_LONG).show()
                            }
                            showProgressBar(false)
                        }
                    }
                }).iTagListener(object : ITagListener{
                    override fun onTagFound() {
                        runOnUiThread {
                            showProgressBar(true)
                        }
                    }
                }).build(this@MainActivity)
        }

        /** Read multiple blocks **/
        val readMultipleBlocksBtn = findViewById<Button>(R.id.readMultipleBlocksBtn)

        //List with the number of blocks you want to read
        val blockNumberList = arrayListOf(0,1,2,3,4)

        readMultipleBlocksBtn.setOnClickListener {
            MLR24Init().init(this@MainActivity, Process.READ_MULTIPLE_BLOCK, blockNumberList)
                .completedListener(object : CompletedListener{
                    override fun onProcessCompletedReadMultipleBlock(response: HashMap<Int, String>) {
                        runOnUiThread {
                            textView.text = response.toString()
                            Toast.makeText(this@MainActivity, response.toString(), Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }
                }).errorListener(object : ErrorListener{
                    override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
                        runOnUiThread {
                            textView.text = reason
                            Toast.makeText(this@MainActivity, reason, Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }
                    override fun onProcessInterrupted(interruptReason: InterruptReason) {
                        runOnUiThread {
                            if (InterruptReason.NFC_NOT_ENABLE == interruptReason){
                                NfcUtils.openNfcSettings(this@MainActivity)
                            }else{
                                textView.text = interruptReason.toString()
                                Toast.makeText(this@MainActivity, interruptReason.toString(), Toast.LENGTH_LONG).show()
                            }
                            showProgressBar(false)
                        }
                    }
                }).iTagListener(object : ITagListener{
                    override fun onTagFound() {
                        runOnUiThread {
                            showProgressBar(true)
                        }
                    }
                }).build(this@MainActivity)
        }

        /** Write multiple blocks **/
        val writeFeatureFileIDriverBtn = findViewById<Button>(R.id.writeMultipleBlocksBtn)
        writeFeatureFileIDriverBtn.setOnClickListener {

            //Hashmap example with block number and date in hex
            val map = HashMap<Int,String>()
            map[0]= "01506201"
            map[1]= "FFFFFFFF"
            map[2]= "FFF0FFFF"
            map[3]= "FFFFFFFF"
            map[4]= "FFF00003"

            MLR24Init().init(this@MainActivity, Process.WRITE_MULTIPLE_BLOCK, map)
                .completedListener(object : CompletedListener{
                    override fun onProcessCompletedWriteMultipleBlock() {
                        runOnUiThread {
                            textView.text = getString(R.string.message_success)
                            Toast.makeText(this@MainActivity, getString(R.string.message_success), Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }
                }).errorListener(object :  ErrorListener{
                    override fun onProcessTerminated(terminationReason: TerminationReason, reason: String) {
                        runOnUiThread {
                            textView.text = reason
                            Toast.makeText(this@MainActivity, reason, Toast.LENGTH_LONG).show()
                            showProgressBar(false)
                        }
                    }
                    override fun onProcessInterrupted(interruptReason: InterruptReason) {
                        runOnUiThread {
                            if (InterruptReason.NFC_NOT_ENABLE == interruptReason){
                                NfcUtils.openNfcSettings(this@MainActivity)
                            }else{
                                textView.text = interruptReason.toString()
                                Toast.makeText(this@MainActivity, interruptReason.toString(), Toast.LENGTH_LONG).show()
                            }
                            showProgressBar(false)
                        }
                    }
                }).iTagListener(object : ITagListener{
                    override fun onTagFound() {
                        runOnUiThread {
                            showProgressBar(true)
                        }
                    }
                }).build(this@MainActivity)
        }

        /** Cancel Process **/
        val cancelProcessBtn = findViewById<Button>(R.id.cancelProcessBtn)
        cancelProcessBtn.setOnClickListener {
            MLR24Init().cancelProcess(this@MainActivity)
        }
    }

    private fun showProgressBar(isShow: Boolean){
        progressBar.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}