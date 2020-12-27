package com.mdlab.mdfirebase.mapper

interface IMapper<Entity, Model>  {
    fun map(from: Entity?): Model?
}