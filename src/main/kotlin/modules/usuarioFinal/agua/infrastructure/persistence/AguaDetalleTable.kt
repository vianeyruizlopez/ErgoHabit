package com.alilopez.modules.usuarioFinal.agua.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object AguaDetalleTable : Table("agua_detalle") {
    val idDetalle = integer("id_detalle").autoIncrement()
    val idUsuario = integer("id_usuario").references(UsuarioTable.idUsuario, onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE)
    val cantidadMl = integer("cantidad_ml")
    val fechaHora = datetime("fecha_hora").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(idDetalle)
}