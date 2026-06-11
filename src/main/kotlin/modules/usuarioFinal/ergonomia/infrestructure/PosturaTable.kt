package com.alilopez.modules.usuarioFinal.ergonomia.infrestructure

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object Postura : Table("historial_postura") {

    val idHistorial = integer("id_historial").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val fecha = timestamp("fecha").defaultExpression(CurrentTimestamp())
    val totalAlertas = integer("total_alertas").default(1)
    override val primaryKey = PrimaryKey(idHistorial)
    init {
        uniqueIndex("uq_usuario_fecha_postura", idUsuario, fecha)
    }
}