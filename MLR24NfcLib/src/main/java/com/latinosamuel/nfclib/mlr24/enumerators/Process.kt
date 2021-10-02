package com.latinosamuel.nfclib.mlr24.enumerators

enum class Process {
    /** Read a single block of the NFC tag **/
    READ_SINGLE_BLOCK,
    /** Write a single block of the NFC tag **/
    WRITE_SINGLE_BLOCK,
    /** Read multiple blocks from the NFC tag **/
    READ_MULTIPLE_BLOCK,
    /** Write multiple blocks from the NFC tag **/
    WRITE_MULTIPLE_BLOCK
}