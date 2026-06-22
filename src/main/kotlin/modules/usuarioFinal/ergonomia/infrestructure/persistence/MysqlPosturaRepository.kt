package com.alilopez.modules.usuarioFinal.ergonomia.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.ergonomia.domain.model.Postura
import com.alilopez.modules.usuarioFinal.ergonomia.domain.repository.PosturaRepository
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.persistence.PosturaTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDate

class MysqlPosturaRepository : PosturaRepository {

    override suspend fun buscarPorFecha(idUsuario: Int, fecha: LocalDate): Postura? = newSuspendedTransaction {
        PosturaTable
            .select {
                (PosturaTable.idUsuario eq idUsuario) and
                        (PosturaTable.fecha.date() eq fecha)
            }
            .map { toDomain(it) }
            .singleOrNull()
    }

    override suspend fun insertar(historial: Postura): Boolean = newSuspendedTransaction {
        PosturaTable.insert {
            it[this.idUsuario] = historial.idUsuario
            it[this.totalAlertas] = historial.totalAlertas
        }
        true
    }

    override suspend fun actualizarAlertas(idHistorial: Int, nuevasAlertas: Int): Boolean = newSuspendedTransaction {
        PosturaTable.update({ PosturaTable.idHistorial eq idHistorial }) {
            it[totalAlertas] = nuevasAlertas
        } > 0
    }

    override suspend fun obtenerHistorialUsuario(idUsuario: Int): List<Postura> = newSuspendedTransaction {
        PosturaTable
            .select { PosturaTable.idUsuario eq idUsuario }
            .orderBy(PosturaTable.fecha to SortOrder.DESC)
            .map { toDomain(it) }
    }

    private fun toDomain(row: ResultRow): Postura = Postura(
        idHistorial = row[PosturaTable.idHistorial],
        idUsuario = row[PosturaTable.idUsuario],
        fecha = row[PosturaTable.fecha],
        totalAlertas = row[PosturaTable.totalAlertas]
    )
}