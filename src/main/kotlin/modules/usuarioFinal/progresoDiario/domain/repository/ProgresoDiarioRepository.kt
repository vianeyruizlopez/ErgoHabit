package com.alilopez.modules.usuarioFinal.progresoDiario.domain.repository

import com.alilopez.modules.usuarioFinal.progresoDiario.domain.model.ProgresosDiarioDB

interface ProgresoDiarioRepository {
    suspend fun obtenerDatosCrudosHoy(idUsuario: Int): ProgresosDiarioDB
}