package com.alilopez.modules.autentificacion.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Registro(
    val id: Int? = null,
    val nombre: String,
    val primerApellido: String,
    val segundoApellido: String,
    val email: String,
    val contrasena: String,
    val idRol: Int,
    val nombreRol: String? = null
)