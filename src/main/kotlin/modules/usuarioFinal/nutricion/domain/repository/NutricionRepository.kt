package com.alilopez.modules.usuarioFinal.nutricion.domain.repository

import com.alilopez.modules.usuarioFinal.nutricion.domain.model.HorarioNutricion
import com.alilopez.modules.usuarioFinal.nutricion.domain.model.ProgresoNutricion
import java.time.LocalTime

interface NutricionRepository {
    fun guardarHorariosConfigurados(idUsuario: Int, desayuno: LocalTime, comida: LocalTime, cena: LocalTime)
    fun obtenerHorariosConfigurados(idUsuario: Int): HorarioNutricion?
    fun registrarConsumoComida(idUsuario: Int, tipoComida: String, estado: Boolean): Boolean
    fun obtenerProgresoComidasHoy(idUsuario: Int): ProgresoNutricion?
}