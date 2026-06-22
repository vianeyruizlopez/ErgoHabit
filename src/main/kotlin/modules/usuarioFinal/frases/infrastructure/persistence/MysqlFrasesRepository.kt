package com.alilopez.modules.usuarioFinal.frases.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.frases.domain.repository.FrasesRepository
import com.alilopez.modules.usuarioFinal.frasesMotivacionales.domain.model.Frases
import com.alilopez.modules.usuarioFinal.frasesMotivacionales.infrastructure.persistence.FrasesMotivacionalesTable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class MysqlFrasesRepository : FrasesRepository  {

    override fun obtenerFraseAleatoriaPorCategoria(categoria: String): Frases? = transaction {
        val frases = FrasesMotivacionalesTable
            .select { FrasesMotivacionalesTable.categoria eq categoria }
            .map {
                Frases(
                    idFrase = it[FrasesMotivacionalesTable.idFrase],
                    categoria = it[FrasesMotivacionalesTable.categoria],
                    texto = it[FrasesMotivacionalesTable.texto]
                )
            }

        if (frases.isNotEmpty()) frases.shuffled().first() else null
    }

    override fun obtenerTodasPorCategoria(categoria: String): List<Frases> = transaction {
        FrasesMotivacionalesTable
            .select { FrasesMotivacionalesTable.categoria eq categoria }
            .map {
                Frases(
                    idFrase = it[FrasesMotivacionalesTable.idFrase],
                    categoria = it[FrasesMotivacionalesTable.categoria],
                    texto = it[FrasesMotivacionalesTable.texto]
                )
            }
    }
}