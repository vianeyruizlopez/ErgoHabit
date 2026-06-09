package com.alilopez.modules.catalogosRol.infrastructure.persistence

import org.jetbrains.exposed.sql.Table
object RolTable : Table("rol") {
    val id = integer("idRol").autoIncrement()
    val nombre = varchar("nombreRol", 50)

    override val primaryKey = PrimaryKey(id)
}