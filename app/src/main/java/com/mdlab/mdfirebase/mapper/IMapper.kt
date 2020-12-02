package com.mdlab.mdfirebase.mapper

interface IMapper<From, To> {
    fun map(from: From?): To?
}