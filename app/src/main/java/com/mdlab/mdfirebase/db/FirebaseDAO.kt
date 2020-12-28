package com.mdlab.mdfirebase.db

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.mdlab.mdfirebase.mapper.FirebaseMapper
import com.mdlab.mdfirebase.model.IDTO
import kotlin.system.exitProcess

class FirebaseDAO<DTO, Model> private constructor(
        private val fbUser: FirebaseUser?,
        override val mapper: FirebaseMapper<DTO, Model>,
        private val rootNodePath: IRootNodePath,
        private val iMapDatabaseReference: IMapDatabaseReference,
        private val iFirebaseOperations: IFirebaseOperations
) : IFirebaseDAO<DTO, Model> {

    data class Builder <DTO, Model>(
        var fbUser: FirebaseUser? = null,
        var mapper: FirebaseMapper<DTO, Model>? = null,
        var rootNodePath: IRootNodePath = IRootNodePath { "users/${it.uid}"},
        var iMapDatabaseReference: IMapDatabaseReference = IMapDatabaseReference { it }, // default - no mapping
        var iFirebaseOperations: IFirebaseOperations = FirebaseOperations()
    ) {
        fun fbUser(fbUser: FirebaseUser?) = apply { this.fbUser = fbUser}
        fun mapper(mapper: FirebaseMapper<DTO, Model>) = apply { this.mapper = mapper}
        fun rootNodePath(rootNodePath: IRootNodePath) = apply { this.rootNodePath = rootNodePath }
        fun iMapDatabaseReference( iMapDatabaseReference:  IMapDatabaseReference) = apply { this.iMapDatabaseReference = iMapDatabaseReference }
        fun iFirebaseOperations(iFirebaseOperations: IFirebaseOperations) = apply { this.iFirebaseOperations = iFirebaseOperations }
        fun build() =   FirebaseDAO(fbUser, mapper!!, rootNodePath!!, iMapDatabaseReference!!, iFirebaseOperations)
    }

    companion object { val TAG = "FBRepo" }

    lateinit var firebaseCallback: IFirebaseDatabaseRepositoryCallback<Model>
    lateinit var listener: ChildEventListener<Model, DTO>

    private val databaseRefQuery: Query by lazy {
        fbUser?.let{
            Firebase.database.reference.child(rootNodePath.get(it))
        } ?:  exitProcess(1) // todo !!!!! it means user is not logged in
    }

    override val workingReference: Query get() = iMapDatabaseReference.invoke( databaseRefQuery.ref)

    override fun addListener(firebaseCallback: IFirebaseDatabaseRepositoryCallback<Model>) {
        this.firebaseCallback = firebaseCallback
        listener = ChildEventListener(mapper, firebaseCallback)
        workingReference.addChildEventListener(listener)
    }

    override fun removeListener() {
        workingReference.removeEventListener(listener as com.google.firebase.database.ChildEventListener)
        workingReference.removeEventListener(listener as ValueEventListener)
    }

    override fun deleteByKey(key: String?, onSuccessListener: () -> Unit): DataState<String> =
            iFirebaseOperations.deleteByKey(workingReference, key, onSuccessListener)

    override fun ensureKey(key: String?): String =
            iFirebaseOperations.ensureKey(workingReference, key)

    override fun storeToFirebase(theKey: String, dto: IDTO, onSuccessCallback: () -> Unit) =
            iFirebaseOperations.storeToFirebase(workingReference, theKey, dto, onSuccessCallback)
}
