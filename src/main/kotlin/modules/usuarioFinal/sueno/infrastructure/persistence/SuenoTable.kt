package com.alilopez.modules.usuarioFinal.sueno.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate
import java.math.BigDecimal

object SuenoTable : Table("progreso_sueno") {
    val idSueno = integer("id_sueno").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val fecha = date("fecha").default(LocalDate.now())
    val horasDormidas = decimal("horas_dormidas", 4, 2).default(BigDecimal.ZERO)
    val despertoATiempo = bool("desperto_a_tiempo").default(false)

    override val primaryKey = PrimaryKey(idSueno)
    init {
        uniqueIndex("uq_usuario_fecha_sueno", idUsuario, fecha)
    }
}