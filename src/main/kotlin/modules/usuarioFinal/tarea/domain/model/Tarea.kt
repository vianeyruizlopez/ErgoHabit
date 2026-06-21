package com.alilopez.modules.usuarioFinal.tarea.domain.model

import java.time.Instant

data class TareaEnfoque(
    val idTarea: Int,
    val idUsuario: Int,
    val idEstado: Int,
    val titulo: String,
    val categoria: String,
    val duracionTarea: Int,
    val fechaCreacion: Instant,
    val duracionInicial: Int,
    val fechaInicioCronometro: Instant?
)
data class PausaActiva(
    val tipoAccion: String,
    val sugerenciaText: String,
    val motivacionText: String
)