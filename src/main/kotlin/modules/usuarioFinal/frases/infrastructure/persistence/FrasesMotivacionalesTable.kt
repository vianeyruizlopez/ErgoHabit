package com.alilopez.modules.usuarioFinal.frasesMotivacionales.infrastructure.persistence

import org.jetbrains.exposed.sql.Table

object FrasesMotivacionalesTable : Table("frases_motivacionales") {
    val idFrase = integer("id_frase").autoIncrement()
    val categoria = varchar("categoria", 30)
    val texto = varchar("texto", 300)

    override val primaryKey = PrimaryKey(idFrase)
}