package com.alilopez.modules.usuarioFinal.tarea.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object TareaEnfoqueTable : Table("tareas_enfoque") {

    val idTarea = integer("id_tarea").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val idEstado = integer("id_estado")
        .references(EstadoTareaTable.idEstado)
        .default(1)
    val titulo = varchar("titulo", 150)
    val duracionTarea = integer("duracion_tarea")
    val fechaCreacion = timestamp("fecha_creacion").defaultExpression(CurrentTimestamp())

    override val primaryKey = PrimaryKey(idTarea)
}