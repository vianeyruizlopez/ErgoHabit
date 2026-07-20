package com.alilopez.modules.autentificacion.infrastructure.persistence

import com.alilopez.modules.autentificacion.domain.model.CodigoVerificacion
import com.alilopez.modules.autentificacion.domain.repository.CodigoVerificacionRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime
import java.time.ZoneId


class MysqlCodigoVerificacionRepository : CodigoVerificacionRepository {

    override suspend fun guardarCodigo(idUsuario: Int, codigo: String) = newSuspendedTransaction {
        val expiraEn = LocalDateTime.now(ZoneId.of("America/Mexico_City")).plusMinutes(10)
        CodigoVerificacionTable.insert {
            it[this.idUsuario] = idUsuario
            it[this.codigo]    = codigo
            it[this.expiraEn]  = expiraEn
            it[this.usado]     = false
        }
        Unit
    }

    override suspend fun obtenerCodigoActivo(idUsuario: Int): CodigoVerificacion? = newSuspendedTransaction {
        CodigoVerificacionTable
            .select {
                (CodigoVerificacionTable.idUsuario eq idUsuario) and
                (CodigoVerificacionTable.usado eq false)
            }
            .orderBy(CodigoVerificacionTable.id to SortOrder.DESC)
            .limit(1)
            .map {
                CodigoVerificacion(
                    id       = it[CodigoVerificacionTable.id],
                    idUsuario = it[CodigoVerificacionTable.idUsuario],
                    codigo   = it[CodigoVerificacionTable.codigo],
                    expiraEn = it[CodigoVerificacionTable.expiraEn],
                    usado    = it[CodigoVerificacionTable.usado]
                )
            }
            .singleOrNull()
    }

    override suspend fun marcarComoUsado(idCodigo: Int) = newSuspendedTransaction {
        CodigoVerificacionTable.update({ CodigoVerificacionTable.id eq idCodigo }) {
            it[usado] = true
        }
        Unit
    }

    override suspend fun eliminarCodigosAnteriores(idUsuario: Int) = newSuspendedTransaction {
        CodigoVerificacionTable.deleteWhere {
            CodigoVerificacionTable.idUsuario eq idUsuario
        }
        Unit
    }
}
