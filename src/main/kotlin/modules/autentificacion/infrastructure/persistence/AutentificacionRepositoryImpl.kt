package com.alilopez.modules.autentificacion.infrastructure.persistence

import com.alilopez.modules.autentificacion.domain.model.Registro
import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class AutentificacionRepositoryImpl : AutentificacionRepository {

    private fun toDomainConRol(row: ResultRow): Registro {
        return Registro(
            id = row[UsuarioTable.id],
            nombre = row[UsuarioTable.nombre],
            primerApellido = row[UsuarioTable.primerApellido],
            segundoApellido = row[UsuarioTable.segundoApellido],
            email = row[UsuarioTable.email],
            contrasena = row[UsuarioTable.contrasena] ?: "",
            idRol = row[UsuarioTable.idRol],
            nombreRol = row.getOrNull(RolTable.nombre) ?: ""
        )
    }

    override suspend fun verPorEmail(email: String): Registro? = newSuspendedTransaction {
        Join(UsuarioTable, RolTable, JoinType.INNER, onColumn = UsuarioTable.idRol, otherColumn = RolTable.id)
            .select { UsuarioTable.email eq email }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }

    override suspend fun registrar(registro: Registro): Registro? = newSuspendedTransaction {
        val nuevoId = UsuarioTable.insert {
            it[nombre] = registro.nombre
            it[primerApellido] = registro.primerApellido
            it[segundoApellido] = registro.segundoApellido
            it[email] = registro.email
            it[contrasena] = registro.contrasena
            it[idRol] = registro.idRol
        }[UsuarioTable.id]

        Join(UsuarioTable, RolTable, JoinType.INNER, onColumn = UsuarioTable.idRol, otherColumn = RolTable.id)
            .select { UsuarioTable.id eq nuevoId }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }
}