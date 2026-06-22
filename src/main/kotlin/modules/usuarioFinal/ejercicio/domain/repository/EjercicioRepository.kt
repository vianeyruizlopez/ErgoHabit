package com.alilopez.modules.usuarioFinal.ejercicio.domain.repository

import com.alilopez.modules.usuarioFinal.ejercicio.domain.model.ProgresoEjercicio
import java.math.BigDecimal
import java.time.LocalDate

interface EjercicioRepository {
    fun obtenerMetaKilometros(idUsuario: Int): BigDecimal
    fun actualizarMetaKilometros(idUsuario: Int, nuevaMeta: BigDecimal)
    fun registrarProgresoKm(idUsuario: Int, kmAgradados: BigDecimal, calorias: Int): Boolean
    fun obtenerProgresoHoy(idUsuario: Int): ProgresoEjercicio?
    fun calcularRachaDias(idUsuario: Int): Int
    fun obtenerHistorialSemanal(idUsuario: Int, desdeFecha: LocalDate): Map<LocalDate, Double>
}