package com.alilopez.modules.usuarioFinal.ergonomia.application.usecase

import com.alilopez.modules.usuarioFinal.ergonomia.domain.model.Postura
import com.alilopez.modules.usuarioFinal.ergonomia.domain.repository.PosturaRepository
import java.time.LocalDate
import java.time.ZoneId

class RegistrarPosturaUseCase(private val repository: PosturaRepository) {

    suspend fun ejecutar(idUsuario: Int, alertasNuevas: Int): Boolean {
        if (alertasNuevas < 0) {
            throw IllegalArgumentException("El conteo de alertas no puede ser negativo.")
        }

        val hoy = LocalDate.now(ZoneId.of("America/Mexico_City"))
        val registroExistente = repository.buscarPorFecha(idUsuario, hoy)

        return if (registroExistente != null) {
            val nuevoTotal = registroExistente.totalAlertas + alertasNuevas
            repository.actualizarAlertas(registroExistente.idHistorial!!, nuevoTotal)
        } else {
            val nuevoRegistro = Postura(
                idUsuario = idUsuario,
                totalAlertas = alertasNuevas
            )
            repository.insertar(nuevoRegistro)
        }
    }
}