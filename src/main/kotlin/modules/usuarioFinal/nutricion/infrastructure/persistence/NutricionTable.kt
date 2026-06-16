package com.alilopez.modules.usuarioFinal.nutricion.infrastructure.persistence

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object NutricionTable : Table("progreso_nutricion") {
    val idNutricion = integer("id_nutricion").autoIncrement()
    val idUsuario = integer("id_usuario").references(
        UsuarioTable.idUsuario,
        onDelete = org.jetbrains.exposed.sql.ReferenceOption.CASCADE
    )
    val fecha = date("fecha").default(LocalDate.now())

    val realizoDesayuno = bool("realizo_desayuno").default(false)
    val realizoComida = bool("realizo_comida").default(false)
    val realizoCena = bool("realizo_cena").default(false)

    override val primaryKey = PrimaryKey(idNutricion)
    init {
        uniqueIndex("uq_usuario_fecha_nutricion", idUsuario, fecha)
    }
}