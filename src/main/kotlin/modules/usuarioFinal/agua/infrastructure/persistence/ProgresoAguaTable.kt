package com.alilopez.modules.usuarioFinal.agua.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object ProgresoAguaTable : Table("progreso_agua") {

    val idAgua = integer("id_agua").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val fecha = date("fecha").clientDefault { LocalDate.now() }
    val cantidadConsumida = integer("cantidad_consumida").default(0)
    val estatura = double("estatura").default(0.0)
    val peso = double("peso").default(0.0)

    override val primaryKey = PrimaryKey(idAgua)

    init {
        uniqueIndex("uq_usuario_fecha_agua", idUsuario, fecha)
    }
}