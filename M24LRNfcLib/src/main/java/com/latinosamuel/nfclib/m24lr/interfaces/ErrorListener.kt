package com.latinosamuel.nfclib.m24lr.interfaces

import com.latinosamuel.nfclib.m24lr.enumerators.InterruptReason
import com.latinosamuel.nfclib.m24lr.enumerators.TerminationReason

interface ErrorListener {

    /** Terminated process listener **/
    fun onProcessTerminated(terminationReason: TerminationReason, reason: String){}

    /** Interrupted process listener **/
    fun  onProcessInterrupted(interruptReason: InterruptReason){}
}