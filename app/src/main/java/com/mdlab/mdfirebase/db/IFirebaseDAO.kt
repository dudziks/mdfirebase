package com.mdlab.mdfirebase.db

import com.google.firebase.database.Query
import com.mdlab.mdfirebase.mapper.FirebaseMapper
import com.mdlab.mdfirebase.model.IDTO

interface IFirebaseDAO<DTO, Model> { // : IFirebaseOperations {
    val mapper: FirebaseMapper<DTO, Model>
    val workingReference: Query
    fun addListener(firebaseCallback: IFirebaseDatabaseRepositoryCallback<Model>)
    fun removeListener()

    fun deleteByKey(key: String?, onSuccessListener: () -> Unit): DataState<String>
    /** If key is null (new item) create new entry in the firebase */
    fun ensureKey(key: String?): String
    fun storeToFirebase(theKey: String, dto: IDTO, onSuccessCallback: () -> Unit)
}