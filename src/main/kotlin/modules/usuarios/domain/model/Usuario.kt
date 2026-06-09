package com.alilopez.modules.usuarios.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int? = null,
    val nombre: String? = null,
    val primerApellido: String? = null,
    val segundoApellido: String? = null,
    val email: String? = null,
    val contrasena: String? = null,
    val idRol: Int? = null,
    val nombreRol: String? = null
)