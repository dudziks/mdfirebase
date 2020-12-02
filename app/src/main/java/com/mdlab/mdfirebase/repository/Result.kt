package com.mdlab.mdfirebase.repository

sealed class Result {
    object Ok : Result() {}
    object None : Result() {}
    data class Error(val err: String = "", val errCode: Int = 1) : Result() {}
}