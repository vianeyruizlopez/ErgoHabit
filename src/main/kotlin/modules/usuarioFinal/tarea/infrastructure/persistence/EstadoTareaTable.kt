package com.alilopez.modules.usuarioFinal.tarea.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object EstadoTareaTable : Table("estados_tareas") {
    val idEstado = integer("id_estado").autoIncrement()
    val nombreEstado = varchar("nombre_estado", 20).uniqueIndex()

    override val primaryKey = PrimaryKey(idEstado)
}
object CategoriasTareasTable : Table("categorias_tareas") {
    val idCategoria = integer("id_categoria").autoIncrement()
    val nombreCategoria = varchar("nombre_categoria", 50)

    override val primaryKey = PrimaryKey(idCategoria)
}