package com.alilopez.modules.usuarioFinal.nutricion.application.usecase

import com.alilopez.modules.usuarioFinal.nutricion.domain.repository.NutricionRepository
import java.time.LocalTime

class ConfigurarHorariosNutricionUseCase(private val repository: NutricionRepository) {

    fun execute(idUsuario: Int, desayuno: LocalTime, comida: LocalTime, cena: LocalTime) {
        if (comida.isBefore(desayuno)) {
            throw IllegalArgumentException("La hora de la comida no puede ser antes del desayuno.")
        }
        if (cena.isBefore(comida)) {
            throw IllegalArgumentException("La hora de la cena no puede ser antes de la comida.")
        }

        repository.guardarHorariosConfigurados(idUsuario, desayuno, comida, cena)
    }
}