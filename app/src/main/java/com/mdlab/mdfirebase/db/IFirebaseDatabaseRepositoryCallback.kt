package com.mdlab.mdfirebase.db

import com.google.firebase.database.DataSnapshot

interface IFirebaseDatabaseRepositoryCallback<T> {
    fun onAdded(result: List<T>)
    fun onAdded(result: T)
    fun onError(e: Exception)
    fun onChanged(result: T)
    fun onRemoved(result: T)
    fun onDataChange(result: DataSnapshot)
}