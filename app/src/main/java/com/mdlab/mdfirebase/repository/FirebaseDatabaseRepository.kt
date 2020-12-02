package com.mdlab.mdfirebase.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mdlab.mdfirebase.mapper.FirebaseMapper
import kotlin.system.exitProcess

abstract class FirebaseDatabaseRepository<Entity, Model>(private var mapper: FirebaseMapper<Entity, Model>) {

    lateinit var firebaseCallback: FirebaseDatabaseRepositoryCallback<Model>
    lateinit var listener: BaseChildEventListener<Model, Entity>

    /**
     * Implement this function to return root path of your data
     */
    abstract fun getRootNode(): String

    /**
     * Override this, if you need other root structure than /users/$userId/
     */
    open fun getRootNodeQuery(fbUser: FirebaseUser): Query =
        FirebaseDatabase.getInstance().reference.child(USERS).child(fbUser.uid).child(getRootNode())

    /**
     * Override this function, if you want to use query on the data.
     */
    open fun mapDatabaseReference(dbRef: DatabaseReference): Query = dbRef

    private val databaseRefQuery: Query by lazy {
        val fbUser = FirebaseAuth.getInstance().currentUser
        if (fbUser == null) {
            exitProcess(1) // todo !!!!! it means user is not logged in
        } else {
            getRootNodeQuery(fbUser)
        }
    }

    fun getDatabaseReference(): DatabaseReference = databaseRefQuery.ref

    fun getMapper(): FirebaseMapper<Entity, Model> = mapper

    fun addListener(firebaseCallback: FirebaseDatabaseRepositoryCallback<Model>, bAddValEvLsnr: Boolean = false) {
        this.firebaseCallback = firebaseCallback
        listener = BaseChildEventListener(mapper, firebaseCallback)
        if (bAddValEvLsnr)
            workingReference().addValueEventListener(listener)
        workingReference().addChildEventListener(listener)
    }

    fun removeListener() {
        workingReference().removeEventListener(listener as ChildEventListener)
        workingReference().removeEventListener(listener as ValueEventListener)
    }


    fun deleteByKey(key: String?, cbu: () -> Unit): Result {
        return if (!key.isNullOrBlank()) {
            workingReference().ref.child(key).removeValue()
                    .addOnSuccessListener {
                        cbu()
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
            workingReference()
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
            workingReference().ref
                    .updateChildren(childUpdates)
                    .addOnSuccessListener {
                        onSuccessCallback()
                        Log.d(TAG, "write successful")
                    }
                    .addOnFailureListener { Log.d(TAG, "write failed") }
        }
    }

    fun workingReference() = mapDatabaseReference(getDatabaseReference())


    interface FirebaseDatabaseRepositoryCallback<T> {
        fun onAdded(result: List<T>)
        fun onAdded(result: T)
        fun onError(e: Exception)
        fun onChanged(result: T)
        fun onRemoved(result: T)
        fun onDataChange(result: DataSnapshot)
    }

    companion object {
        const val TAG = "FBRepo"
        const val USERS = "users"
    }
}