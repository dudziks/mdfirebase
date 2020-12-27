package com.mdlab.mdfirebase.mapper



import com.google.firebase.database.DataSnapshot
import java.lang.reflect.ParameterizedType

abstract class FirebaseMapper<DTO, Model> : IMapper<DTO, Model> {

    private val entityClass: Class<DTO>
        get() {
            val superclass = javaClass.genericSuperclass as ParameterizedType
            return superclass.actualTypeArguments[0] as Class<DTO>
        }

    fun map(dataSnapshot: DataSnapshot?): Model? {
        val entity = dataSnapshot!!.getValue(entityClass)
        return mapFromDTO(entity)
    }
}