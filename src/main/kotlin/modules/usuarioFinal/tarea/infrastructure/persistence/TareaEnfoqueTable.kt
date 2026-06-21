package com.alilopez.modules.usuarioFinal.tarea.infrastructure.persistence
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
object TareaEnfoqueTable : Table("tareas_enfoque") {
    val idTarea = integer("id_tarea").autoIncrement()
    val idUsuario = integer("id_usuario")
    val idEstado = integer("id_estado")
    val idCategoria = integer("id_categoria")
    val titulo = varchar("titulo", 150)
    val duracionTarea = integer("duracion_tarea")
    val duracionInicial = integer("duracion_inicial")
    val fechaCreacion = timestamp("fecha_creacion")
    val fechaInicioCronometro = timestamp("fecha_inicio_cronometro").nullable()

    override val primaryKey = PrimaryKey(idTarea)
}

object PausaTareaTable : Table("pausas_activas_catalogo") {
    val idPausa = integer("id_pausa").autoIncrement()
    val accionFisica = varchar("accion_fisica", 250)

    override val primaryKey = PrimaryKey(idPausa)
}