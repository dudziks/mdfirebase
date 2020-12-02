package com.mdlab.mdfirebase.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mdlab.mdfirebase.mapper.FirebaseMapper
import kotlin.system.exitProcess

abstract class FirebaseDatabaseRepository<Entity, Model>(private var mapper: FirebaseMapper<Entity, Model>) {

    lateinit var firebaseCallback: FirebaseDatabaseRepositoryCallback<Model>
    lateinit var listener: BaseChildEventListener<Model, Entity>
    abstract fun getRootNode(): String
    open fun mapDatabaseReference(dbRef: DatabaseReference): Query = dbRef

    private val databaseRefQuery: Query by lazy {
        val fbUser = FirebaseAuth.getInstance().currentUser
        if (fbUser == null) {
            exitProcess(1) // todo !!!!! it means user is not logged in
        } else {
            FirebaseDatabase.getInstance().reference.child(USERS).child(fbUser.uid)
                .child(getRootNode())
        }
    }

    fun getDatabaseReference(): DatabaseReference {
        return this.databaseRefQuery.ref
    }

    fun getMapper(): FirebaseMapper<Entity, Model> {
        return mapper
    }

    fun addListener(firebaseCallback: FirebaseDatabaseRepositoryCallback<Model>, bAddValEvLsnr: Boolean = false) {
        this.firebaseCallback = firebaseCallback
        listener = BaseChildEventListener(mapper, firebaseCallback)
        if (bAddValEvLsnr)
            mapDatabaseReference(getDatabaseReference()).addValueEventListener(listener)
        mapDatabaseReference(getDatabaseReference()).addChildEventListener(listener)
    }

    fun removeListener() {
        mapDatabaseReference(getDatabaseReference()).removeEventListener(listener as ChildEventListener)
        mapDatabaseReference(getDatabaseReference()).removeEventListener(listener as ValueEventListener)
    }

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