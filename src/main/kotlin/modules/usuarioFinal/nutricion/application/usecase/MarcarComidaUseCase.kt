package com.alilopez.modules.usuarioFinal.nutricion.application.usecase

import com.alilopez.modules.usuarioFinal.nutricion.domain.repository.NutricionRepository

class MarcarComidaUseCase(private val repository: NutricionRepository) {

    fun execute(idUsuario: Int, tipoComida: String, estado: Boolean): Boolean {
        val tipoFormateado = tipoComida.uppercase().trim()

        if (tipoFormateado != "DESAYUNO" && tipoFormateado != "COMIDA" && tipoFormateado != "CENA") {
            throw IllegalArgumentException("Tipo de comida inválido. Usa: DESAYUNO, COMIDA o CENA.")
        }

        return repository.registrarConsumoComida(idUsuario, tipoFormateado, estado)
    }
}