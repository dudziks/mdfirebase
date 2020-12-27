package com.mdlab.mdfirebase.db

import com.google.firebase.database.Query
import com.mdlab.mdfirebase.mapper.FirebaseMapper
import com.mdlab.mdfirebase.model.IDTO

interface IFirebaseDAO<DTO, Model>: IFirebaseOperations {
    val mapper: FirebaseMapper<DTO, Model>
    fun addListener(firebaseCallback: IFirebaseDatabaseRepositoryCallback<Model>)
    fun removeListener()
}