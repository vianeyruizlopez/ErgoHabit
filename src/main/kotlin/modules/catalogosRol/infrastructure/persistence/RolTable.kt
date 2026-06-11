package com.alilopez.modules.catalogosRol.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object RolTable : Table("roles") {

    val idRol = integer("id_rol").autoIncrement()
    val nombreRol = varchar("nombre_rol", 50)

    override val primaryKey = PrimaryKey(idRol)
}