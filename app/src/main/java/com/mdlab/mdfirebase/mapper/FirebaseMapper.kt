package com.mdlab.mdfirebase.mapper


import com.google.firebase.database.DataSnapshot

import java.lang.reflect.ParameterizedType
import java.util.ArrayList

abstract class FirebaseMapper<Entity, Model> : IMapper<Entity, Model> {

    private val entityClass: Class<Entity>
        get() {
            val superclass = javaClass.genericSuperclass as ParameterizedType
            return superclass.actualTypeArguments[0] as Class<Entity>
        }

    fun map(dataSnapshot: DataSnapshot?): Model? {
        val entity = dataSnapshot!!.getValue(entityClass)
        return map(entity)
    }
}