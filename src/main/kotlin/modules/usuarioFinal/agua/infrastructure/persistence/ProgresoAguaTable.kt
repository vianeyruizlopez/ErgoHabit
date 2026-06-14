package com.alilopez.modules.usuarioFinal.agua.infrastructure.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object ProgresoAguaTable : Table("progreso_agua") {
    val idAgua = integer("id_agua").autoIncrement()
    val idUsuario = integer("id_usuario")
    val fecha = date("fecha")
    val cantidadConsumida = integer("cantidad_consumida").default(0)

    override val primaryKey = PrimaryKey(idAgua)
}