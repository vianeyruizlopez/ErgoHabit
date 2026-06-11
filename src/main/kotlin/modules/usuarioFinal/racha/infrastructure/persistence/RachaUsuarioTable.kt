package com.alilopez.modules.usuarioFinal.racha.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object RachaUsuarioTable : Table("racha_usuario") {

    val idRacha = integer("id_racha").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val diasConsecutivos = integer("dias_consecutivos").default(0)
    val ultimaActividad = timestamp("ultima_actividad").defaultExpression(CurrentTimestamp())

    override val primaryKey = PrimaryKey(idRacha)
}