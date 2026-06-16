package com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class SuenoRequest(
    val horaDespertar: String,
    val horaDormir: String
)
