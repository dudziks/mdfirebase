package com.mdlab.mdfirebase.repository

import android.util.Log
import com.mdlab.mdfirebase.mapper.FirebaseMapper

abstract class MDFBRepo<Entity, Model>(mapper: FirebaseMapper<Entity, Model>) :
    FirebaseDatabaseRepository<Entity, Model>(mapper) {

    fun deleteByKey(key: String?, cbu: () -> Unit): Result {
        return if (!key.isNullOrBlank()) {
            mapDatabaseReference(getDatabaseReference()).ref.child(key).removeValue()
                .addOnSuccessListener {
                    cbu()
                    Log.d(TAG, "remove successful")
                }
                .addOnFailureListener { Log.d(TAG, "remove failed") }
            Result.Ok
        } else {
            Result.Error("Key is null or blank")
        }
    }

    /**
     * If key is null (new item) create new entry in the firebase
     */
    fun ensureKey(key: String?): String {
        var theKey = key
        if (theKey.isNullOrEmpty()) {
            this.mapDatabaseReference(getDatabaseReference())
                .ref.push().key?.run {theKey = this  } ?: throw Exception("Cannot create key")
        }
        return theKey!!
    }

    fun storeToFirebase(
            theKey: String,
            values: Map<String, Any?>,
            onSuccessCallback: () -> Unit
    ) {
        val childUpdates = HashMap<String, Any>()
        theKey.let {
            childUpdates[it] = values
            mapDatabaseReference(getDatabaseReference()).ref
                .updateChildren(childUpdates)
                .addOnSuccessListener {
                    onSuccessCallback()
                    Log.d(TAG, "write successful")
                }
                .addOnFailureListener { Log.d(TAG, "write failed") }
        }
    }
}