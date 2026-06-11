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
            id = row[UsuarioTable.idUsuario],
            nombre = row[UsuarioTable.nombres],
            primerApellido = row[UsuarioTable.primerApellido],
            segundoApellido = row[UsuarioTable.segundoApellido],
            email = row[UsuarioTable.correo],
            contrasena = row[UsuarioTable.contrasenia],
            idRol = row[UsuarioTable.idRol],
            nombreRol = row.getOrNull(RolTable.nombreRol) ?: ""
        )
    }

    override suspend fun verPorEmail(email: String): Registro? = newSuspendedTransaction {
        Join(UsuarioTable, RolTable, JoinType.INNER, onColumn = UsuarioTable.idRol, otherColumn = RolTable.idRol)
            .select { UsuarioTable.correo eq email }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }

    override suspend fun registrar(registro: Registro): Registro? = newSuspendedTransaction {
        val nuevoId = UsuarioTable.insert {
            it[nombres] = registro.nombre
            it[primerApellido] = registro.primerApellido
            it[segundoApellido] = registro.segundoApellido
            it[correo] = registro.email
            it[contrasenia] = registro.contrasena
            it[idRol] = registro.idRol
            it[peso] = null
            it[estatura] = null
        }[UsuarioTable.idUsuario]

        Join(UsuarioTable, RolTable, JoinType.INNER, onColumn = UsuarioTable.idRol, otherColumn = RolTable.idRol)
            .select { UsuarioTable.idUsuario eq nuevoId }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }
}