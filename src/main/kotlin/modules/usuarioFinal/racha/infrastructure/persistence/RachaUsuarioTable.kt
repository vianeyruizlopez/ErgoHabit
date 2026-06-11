package com.alilopez.modules.usuarioFinal.racha.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object RachaUsuario : Table(racha_usuario){
    val id_racha = integer("id_racha")
    val id = integer("id_usuario")

}