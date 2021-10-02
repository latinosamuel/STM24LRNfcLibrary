package com.latinosamuel.nfclib.mlr24.interfaces

import com.latinosamuel.nfclib.mlr24.enumerators.InterruptReason
import com.latinosamuel.nfclib.mlr24.enumerators.TerminationReason

interface ErrorListener {

    /** Terminated process listener **/
    fun onProcessTerminated(terminationReason: TerminationReason, reason: String){}

    /** Interrupted process listener **/
    fun  onProcessInterrupted(interruptReason: InterruptReason){}
}