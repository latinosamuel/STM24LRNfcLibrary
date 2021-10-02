package com.latinosamuel.nfclib.mlr24.models

import com.latinosamuel.nfclib.mlr24.enumerators.TerminationReason

class Result() {

    var isError: Boolean = false
    var terminationReason: TerminationReason? = null
    var error: String? = null
    var multipleBlocks: HashMap<Int, String> = HashMap()
    var singleBlockNumber: Int? = null
    var singleBlockHexData: String? = null

    constructor(isError: Boolean, terminationReason: TerminationReason?, error: String?) : this() {
        this.isError = isError
        this.terminationReason = terminationReason
        this.error = error
    }

    constructor(isError: Boolean, singleBlockNumber: Int, singleBlockHexData: String) : this() {
        this.isError = isError
        this.singleBlockNumber = singleBlockNumber
        this.singleBlockHexData = singleBlockHexData
    }

    constructor(isError: Boolean, multipleBlocks: HashMap<Int, String>) : this() {
        this.isError = isError
        this.multipleBlocks = multipleBlocks
    }
}