package com.alilopez.modules.usuarios.infrastructure.rest.dto

import com.alilopez.modules.usuarios.domain.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioRequests(
    val id: Int? = null,
    val nombre: String? = null,
    val primerApellido: String? = null,
    val segundoApellido: String? = null,
    val email: String? = null,
    val idRol: Int? = null,
    val peso: Double? = null,
    val estatura: Double? = null
){
    fun toDomain() = Usuario(
        id = id ?: 0,
        nombre = nombre,
        primerApellido = primerApellido,
        segundoApellido = segundoApellido,
        email = email,
        contrasena = null,
        idRol = idRol,
        peso = peso,
        estatura = estatura
    )
}