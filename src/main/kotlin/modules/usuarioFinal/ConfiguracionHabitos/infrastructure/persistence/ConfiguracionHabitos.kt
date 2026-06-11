package com.alilopez.modules.usuarioFinal.ConfiguracionHabitos.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.time
import java.math.BigDecimal
import java.time.LocalTime

object ConfiguracionHabitos : Table("configuracion_habitos") {

    val idHabito = integer("id_habito").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val metaAgua = integer("meta_agua").default(2450)
    val horaDormir = time("hora_dormir").default(LocalTime.MIDNIGHT)
    val horaDespertar = time("hora_despertar").default(LocalTime.MIDNIGHT)
    val metaEjercicio = decimal("meta_ejercicio", 5, 2).default(BigDecimal.ZERO)
    val horaDesayuno = time("hora_desayuno").nullable()
    val horaComida = time("hora_comida").nullable()
    val horaCena = time("hora_cena").nullable()

    override val primaryKey = PrimaryKey(idHabito)
    init {
        uniqueIndex("uq_usuario_configuracion", idUsuario)
    }
}