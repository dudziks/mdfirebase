package com.mdlab.mdfirebase.repository

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mdlab.mdfirebase.mapper.FirebaseMapper

class BaseChildEventListener<Model, Entity>(
    val mapper: FirebaseMapper<Entity, Model>,
    val callback: FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Model>
) : ChildEventListener, ValueEventListener {
    override fun onDataChange(p0: DataSnapshot) {
        callback.onDataChange(p0)
    }

    override fun onCancelled(p0: DatabaseError) {
        callback.onError(p0.toException())
    }

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        if(p0.exists()) {
            val c = mapper.map(p0)
            if(c!=null) {
                callback.onChanged(c)
            }
        }
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        if(p0.exists()) {
            val c = mapper.map(p0)
            if(c!=null) {
                callback.onChanged(c)
            }
        }
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        if(p0.exists()) {
            val c = mapper.map(p0)
            if(c!=null) {
                callback.onRemoved(c)
            }
        }
    }


    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        if(p0.exists()) {
            val c = mapper.map(p0)
            if(c!=null) {
                callback.onAdded(c)
            }
        }
    }
    companion object {
        const val TAG = "BChldEvntLstnr"
    }
}