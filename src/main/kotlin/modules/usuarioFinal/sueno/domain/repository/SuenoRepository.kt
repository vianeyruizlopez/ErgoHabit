package com.alilopez.modules.usuarioFinal.sueno.domain.repository

import com.alilopez.modules.usuarioFinal.sueno.domain.model.HorarioSueno
import com.alilopez.modules.usuarioFinal.sueno.domain.model.ProgresoSueno
import java.time.LocalTime

interface SuenoRepository {
    fun guardarConfiguracionHorario(idUsuario: Int, horaDormir: LocalTime, horaDespertar: LocalTime)
    fun obtenerConfiguracion(idUsuario: Int): HorarioSueno?
    fun registrarProgresoSuenoDiario(idUsuario: Int, horasDormidas: Double, despertoATiempo: Boolean): Boolean
    fun obtenerProgresoHoy(idUsuario: Int): ProgresoSueno?
}