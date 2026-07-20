package com.alilopez.modules.autentificacion.infrastructure.persistence

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object CodigoVerificacionTable : Table("codigos_verificacion") {
    val id         = integer("id").autoIncrement()
    val idUsuario  = integer("id_usuario")
    val codigo     = varchar("codigo", 6)
    val expiraEn   = datetime("expira_en")
    val usado      = bool("usado").default(false)

    override val primaryKey = PrimaryKey(id)
}
