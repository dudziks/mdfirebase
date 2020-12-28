package com.mdlab.mdfirebase.db

import android.util.Log
import com.google.firebase.database.Query
import com.mdlab.mdfirebase.model.IDTO

class FirebaseOperations : IFirebaseOperations{

    override fun deleteByKey(workingRef: Query, key: String?, onSuccessListener: () -> Unit): DataState<String> {
        return if (!key.isNullOrBlank()) {
            workingRef.ref.child(key).removeValue()
                    .addOnSuccessListener {
                        onSuccessListener()
                    }
                    .addOnFailureListener { Log.d(FirebaseDAO.TAG, "remove failed") }
            DataState.Loading
        } else {
            DataState.Error(Exception("Key is null or blank"))
        }
    }

    override fun ensureKey(workingRef: Query, key: String?): String {
        var theKey = key
        if (theKey.isNullOrEmpty()) {
            workingRef
                    .ref.push().key?.run {theKey = this  } ?: throw Exception("Cannot create key")
        }
        return theKey!!
    }

    override fun storeToFirebase(workingRef: Query, theKey: String, dto: IDTO, onSuccessCallback: () -> Unit) {
        val childUpdates = HashMap<String, Any>()
        theKey.let {
            childUpdates[it] = dto.toMap()
            workingRef.ref
                    .updateChildren(childUpdates)
                    .addOnSuccessListener {
                        onSuccessCallback()
                        Log.d(FirebaseDAO.TAG, "write successful")
                    }
                    .addOnFailureListener { Log.d(FirebaseDAO.TAG, "write failed") }
        }
    }
}