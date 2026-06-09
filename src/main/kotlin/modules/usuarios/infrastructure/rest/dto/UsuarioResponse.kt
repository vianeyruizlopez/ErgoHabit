package com.alilopez.modules.usuarios.infrastructure.rest.dto

import com.alilopez.modules.usuarios.domain.model.Usuario
import kotlinx.serialization.Serializable

@Serializable
data class UsuarioResponse(
    val id: Int,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val email: String,
    val contrasena: String?,
    val idRol: Int,
    val nombreRol: String
)

fun Usuario.toResponse() = UsuarioResponse(
    id = this.id ?: 0,
    nombre = this.nombre ?: "",
    primerApellido = this.primerApellido ?: "",
    segundoApellido = this.segundoApellido ?: "",
    email = this.email ?: "",
    contrasena = null,
    idRol = this.idRol ?: 0,
    nombreRol = this.nombreRol ?: "Sin Rol"
)