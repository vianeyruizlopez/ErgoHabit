package com.alilopez.modules.usuarioFinal.agua.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object ProgresoAguaTable : Table("progreso_agua") {

    val idAgua = integer("id_agua").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val fecha = timestamp("fecha").clientDefault { Instant.now() }
    val cantidadConsumida = integer("cantidad_consumida").default(0)

    override val primaryKey = PrimaryKey(idAgua)

    init {
        uniqueIndex("uq_usuario_fecha_agua", idUsuario, fecha)
    }
}