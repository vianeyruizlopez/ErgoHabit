package com.alilopez.modules.usuarioFinal.tarea.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object EstadoTareaTable : Table("estados_tareas") {
    val idEstado = integer("id_estado").autoIncrement()
    val nombreEstado = varchar("nombre_estado", 20).uniqueIndex()

    override val primaryKey = PrimaryKey(idEstado)
}