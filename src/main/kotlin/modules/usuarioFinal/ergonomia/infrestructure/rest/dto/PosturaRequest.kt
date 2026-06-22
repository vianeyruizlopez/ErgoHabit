package com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegistroPosturaRequest(
    val totalAlertas: Int
)