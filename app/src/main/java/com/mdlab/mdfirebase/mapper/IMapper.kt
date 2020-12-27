package com.mdlab.mdfirebase.mapper

interface IMapper<DTO, Model>  {
    fun mapFromDTO(from: DTO?): Model?
    fun mapToDTO(from: Model?): DTO?
}