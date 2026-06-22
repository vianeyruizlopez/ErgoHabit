package com.alilopez.modules.usuarioFinal.agua.domain.repository

import com.alilopez.modules.usuarioFinal.agua.domain.model.DashboardAgua
import java.time.LocalDate

interface AguaRepository {
    fun obtenerDashboard(idUsuario: Int): DashboardAgua
    fun registrarToma(idUsuario: Int, cantidadMl: Int): Boolean
    fun actualizarMetaManual(idUsuario: Int, nuevaMetaMl: Int): Boolean
    fun guardarMetaInteligente(idUsuario: Int, metaCalculada: Int, pesoKg: Double, estaturaCm: Double): Boolean
    fun obtenerHistorialSemanal(idUsuario: Int, desdeFecha: LocalDate): Map<LocalDate, Int>
}