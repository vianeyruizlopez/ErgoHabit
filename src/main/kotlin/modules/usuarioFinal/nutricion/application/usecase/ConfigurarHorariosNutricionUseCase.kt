package com.alilopez.modules.usuarioFinal.nutricion.application.usecase

import com.alilopez.modules.usuarioFinal.nutricion.domain.repository.NutricionRepository
import java.time.LocalTime

class ConfigurarHorariosNutricionUseCase(private val repository: NutricionRepository) {

    fun execute(idUsuario: Int, desayuno: LocalTime, comida: LocalTime, cena: LocalTime) {
        val inicioDesayuno = LocalTime.of(7, 0)
        val finDesayuno = LocalTime.of(10, 0)

        val inicioComida = LocalTime.of(14, 0) // 2:00 PM - 5:00 PM
        val finComida = LocalTime.of(17, 0)

        val inicioCena = LocalTime.of(20, 0)   // 8:00 PM - 9:00 PM
        val finCena = LocalTime.of(21, 0)

        if (desayuno.isBefore(inicioDesayuno) || desayuno.isAfter(finDesayuno)) {
            throw IllegalArgumentException("El desayuno debe configurarse entre las 07:00 AM y las 10:00 AM.")
        }

        if (comida.isBefore(inicioComida) || comida.isAfter(finComida)) {
            throw IllegalArgumentException("La comida debe configurarse entre las 02:00 PM y las 05:00 PM.")
        }

        if (cena.isBefore(inicioCena) || cena.isAfter(finCena)) {
            throw IllegalArgumentException("La cena debe configurarse entre las 08:00 PM y las 09:00 PM.")
        }

        if (comida.isBefore(desayuno)) {
            throw IllegalArgumentException("La hora de la comida no puede ser antes del desayuno.")
        }
        if (cena.isBefore(comida)) {
            throw IllegalArgumentException("La hora de la cena no puede ser antes de la comida.")
        }

        repository.guardarHorariosConfigurados(idUsuario, desayuno, comida, cena)
    }
}