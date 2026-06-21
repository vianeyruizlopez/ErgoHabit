package com.alilopez.modules.usuarioFinal.tarea.domain.repository

import com.alilopez.modules.usuarioFinal.tarea.domain.model.TareaEnfoque

interface TareaRepository {
    fun crearTarea(idUsuario: Int, titulo: String, categoria: String, duracion: Int): TareaEnfoque
    fun obtenerTareasPendientes(idUsuario: Int): List<TareaEnfoque>
    fun obtenerTareasCompletadasHoy(idUsuario: Int): List<TareaEnfoque>
    fun cambiarEstadoTarea(idTarea: Int, idUsuario: Int, nuevoEstado: Int): Boolean
    fun agregarMinutosTarea(idTarea: Int, idUsuario: Int, minutosExtra: Int): Boolean
    fun buscarPorId(idTarea: Int, idUsuario: Int): TareaEnfoque?
    fun obtenerFraseAleatoria(categoria: String): String
    fun obtenerPausaFisicaAleatoria(): String
    fun iniciarCronometroEnBaseDatos(idTarea: Int, idUsuario: Int): Boolean
    fun guardarPausaEnBaseDatos(idTarea: Int, idUsuario: Int, nuevoTiempoRestante: Int): Boolean
    fun eliminarTarea(idTarea: Int, idUsuario: Int): Boolean
}