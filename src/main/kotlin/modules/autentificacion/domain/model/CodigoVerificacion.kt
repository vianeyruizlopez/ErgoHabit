package com.alilopez.modules.autentificacion.domain.model

import java.time.LocalDateTime


data class CodigoVerificacion(
    val id: Int? = null,
    val idUsuario: Int,
    val codigo: String,
    val expiraEn: LocalDateTime,
    val usado: Boolean = false
)
