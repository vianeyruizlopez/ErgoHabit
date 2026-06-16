package com.alilopez.modules.usuarioFinal.nutricion.application.usecase

import com.alilopez.modules.usuarioFinal.nutricion.domain.repository.NutricionRepository
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.dto.NutricionDashboardResponse
import java.time.format.DateTimeFormatter

class VerDashboardNutricionUseCase(private val repository: NutricionRepository) {

    fun execute(idUsuario: Int): NutricionDashboardResponse {
        val config = repository.obtenerHorariosConfigurados(idUsuario)

        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        val horaDesayunoStr = config?.horaDesayuno?.format(formatter) ?: "--:--"
        val horaComidaStr = config?.horaComida?.format(formatter) ?: "--:--"
        val horaCenaStr = config?.horaCena?.format(formatter) ?: "--:--"

        val progreso = repository.obtenerProgresoComidasHoy(idUsuario)
        val desayunoCheck = progreso?.realizoDesayuno ?: false
        val comidaCheck = progreso?.realizoComida ?: false
        val cenaCheck = progreso?.realizoCena ?: false

        var comidasContadas = 0
        if (desayunoCheck) comidasContadas++
        if (comidaCheck) comidasContadas++
        if (cenaCheck) comidasContadas++

        val porcentaje = ((comidasContadas / 3.0) * 100).toInt()

        val faltantes = 3 - comidasContadas
        val mensajeFaltante = when (faltantes) {
            0 -> "¡Meta cumplida de hoy!"
            1 -> "Te faltan 1 comida"
            else -> "Te faltan $faltantes comidas"
        }

        return NutricionDashboardResponse(
            comidasCompletadasText = "$comidasContadas / 3 comidas",
            porcentajeCumplimiento = porcentaje,
            mensajeFaltanteText = mensajeFaltante,
            horaDesayunoConfigurada = horaDesayunoStr,
            horaComidaConfigurada = horaComidaStr,
            horaCenaConfigurada = horaCenaStr,
            chequeoDesayuno = desayunoCheck,
            chequeoComida = comidaCheck,
            chequeoCena = cenaCheck
        )
    }
}