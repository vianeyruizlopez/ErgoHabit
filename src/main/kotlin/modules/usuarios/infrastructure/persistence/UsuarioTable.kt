package com.alilopez.modules.usuarios.infrastructure.persistence

import org.jetbrains.exposed.sql.Table
object UsuarioTable : Table("usuario") {
    val id = integer("id").autoIncrement()
    val nombre = varchar("nombre", 50)
    val primerApellido = varchar("primer_apellido", 50)
    val segundoApellido = varchar("segundo_apellido", 50)
    val email = varchar("correo", 100)
    val contrasena = varchar("contrasena", 255).nullable()

    val idRol = integer("idRol")

    override val primaryKey = PrimaryKey(id)
}