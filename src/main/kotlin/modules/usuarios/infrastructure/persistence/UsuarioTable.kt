package com.alilopez.modules.usuarios.infrastructure.persistence

import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable
import org.jetbrains.exposed.sql.Table

object UsuarioTable : Table("usuarios") {

    val idUsuario = integer("id_usuario").autoIncrement()
    val idRol = integer("id_rol").references(RolTable.idRol)
    val nombres = varchar("nombres", 100)
    val primerApellido = varchar("primer_apellido", 100)
    val segundoApellido = varchar("segundo_apellido", 100).nullable()
    val correo = varchar("correo", 150).uniqueIndex()
    val contrasenia = varchar("contrasenia", 255)
    val peso = decimal("peso", 5, 2).nullable()
    val estatura = decimal("estatura", 3, 2).nullable()


    override val primaryKey = PrimaryKey(idUsuario)
}