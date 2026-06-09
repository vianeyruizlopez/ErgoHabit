package com.alilopez.modules.usuarios.infrastructure.rest.dto

import com.alilopez.modules.usuarios.domain.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioRequests(
    val id: Int?,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val email: String,
    val contrasena: String?,
    val idRol: Int
){
    fun toDomain()= Usuario(
        id = id ?: 0,
        nombre = nombre,
        primerApellido = primerApellido,
        segundoApellido = segundoApellido,
        email = email,
        contrasena = contrasena,
        idRol= idRol
    )
}