package com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.timestamp

object EjercicioTable : Table("progreso_ejercicio") {
    val idEjercicio = integer("id_ejercicio").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val fecha = timestamp("fecha").defaultExpression(CurrentTimestamp())
    val kmRecorridos = decimal("km_recorridos", 5, 2).default(java.math.BigDecimal.ZERO)
    val caloriasQuemadas = integer("calorias_quemadas").default(0)

    override val primaryKey = PrimaryKey(idEjercicio)
    init {
        uniqueIndex("uq_usuario_fecha_ejercicio", idUsuario, fecha)
    }
}