package com.mdlab.mdfirebase.db

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mdlab.mdfirebase.mapper.FirebaseMapper

class ChildEventListener<Model, Entity>(
    private val mapper: FirebaseMapper<Entity, Model>,
    private val callback: IFirebaseDatabaseRepositoryCallback<Model>
) : ChildEventListener, ValueEventListener {
    override fun onDataChange(p0: DataSnapshot) =
        callback.onDataChange(p0)

    override fun onCancelled(p0: DatabaseError) =
        callback.onError(p0.toException())

    override fun onChildMoved(p0: DataSnapshot, p1: String?) {
        if (p0.exists()) {
            mapper.map(p0)?.let { callback.onChanged(it) }
        }
    }

    override fun onChildChanged(p0: DataSnapshot, p1: String?) {
        if (p0.exists()) {
            mapper.map(p0)?.let { callback.onChanged(it) }
        }
    }

    override fun onChildRemoved(p0: DataSnapshot) {
        if (p0.exists()) {
            mapper.map(p0)?.let { callback.onRemoved(it) }
        }
    }

    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
        if (p0.exists()) {
            mapper.map(p0)?.let { callback.onAdded(it) }
        }
    }
}
