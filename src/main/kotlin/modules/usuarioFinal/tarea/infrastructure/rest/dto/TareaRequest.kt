package com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.dto

import kotlinx.serialization.Serializable

@Serializable
data class TareaRequest(
    val titulo: String,
    val categoria: String,
    val duracionTarea: Int
)

@Serializable
data class TiempoExtraRequest(
    val minutosExtra: Int
)