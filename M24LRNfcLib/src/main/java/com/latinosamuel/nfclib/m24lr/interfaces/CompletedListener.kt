package com.latinosamuel.nfclib.m24lr.interfaces

interface CompletedListener {

    /** Read single block listener terminated **/
    fun onProcessCompletedReadSingleBlock(blockNumber: Int, hexData: String){}

    /** Write single block listener terminated **/
    fun onProcessCompletedWriteSingleBlock(){}

    /** Read multiple block listener terminated **/
    fun onProcessCompletedReadMultipleBlock(response: HashMap<Int, String>){}

    /** Write multiple block listener terminated **/
    fun onProcessCompletedWriteMultipleBlock(){}
}