package com.alilopez.modules.usuarioFinal.ergonomia.domain.repository

import com.alilopez.modules.usuarioFinal.ergonomia.domain.model.Postura
import java.time.LocalDate

interface PosturaRepository {
    suspend fun buscarPorFecha(idUsuario: Int, fecha: LocalDate): Postura?
    suspend fun insertar(historial: Postura): Boolean
    suspend fun actualizarAlertas(idHistorial: Int, nuevasAlertas: Int): Boolean
    suspend fun obtenerHistorialUsuario(idUsuario: Int): List<Postura>
}