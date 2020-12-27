package com.mdlab.mdfirebase.db

import com.google.firebase.database.Query
import com.mdlab.mdfirebase.model.IDTO

interface IFirebaseOperations {
    fun deleteByKey(
            workingRef: Query,
            key: String?,
            onSuccessListener: () -> Unit
    ): DataState<String>

    /**
     * If key is null (new item) create new entry in the firebase
     */
    fun ensureKey(
            workingRef: Query,
            key: String?
    ): String

    fun storeToFirebase(
            workingRef: Query,
            theKey: String,
            dto: IDTO,
            onSuccessCallback: () -> Unit
    )
}