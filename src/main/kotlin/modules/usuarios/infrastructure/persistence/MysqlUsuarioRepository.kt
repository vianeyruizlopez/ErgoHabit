package com.alilopez.modules.usuarios.infrastructure.persistence

import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable
import com.alilopez.modules.usuarios.CloudinaryService
import com.alilopez.modules.usuarios.domain.model.Usuario
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.io.InputStream

class MysqlUsuarioRepository(private val cloudinaryService: CloudinaryService
) : UsuarioRepository {

    private fun UsuariosConRoles() = UsuarioTable.join(
        RolTable,
        joinType = JoinType.INNER,
        onColumn = UsuarioTable.idRol,
        otherColumn = RolTable.idRol
    )

    override suspend fun verTodos(): List<Usuario> = newSuspendedTransaction {
        UsuariosConRoles()
            .selectAll()
            .map { toDomainConRol(it) }
    }

    override suspend fun verPorId(id: Int): Usuario? = newSuspendedTransaction {
        UsuariosConRoles()
            .select { UsuarioTable.idUsuario eq id }
            .map { toDomainConRol(it) }
            .singleOrNull()
    }

    override suspend fun actualizar(id: Int, usuario: Usuario): Usuario? = newSuspendedTransaction {
        val filasAfectadas = UsuarioTable.update({ UsuarioTable.idUsuario eq id }) {
            it[nombres] = usuario.nombre ?: ""
            it[primerApellido] = usuario.primerApellido ?: ""
            it[segundoApellido] = usuario.segundoApellido
            it[correo] = usuario.email ?: ""
            it[peso] = usuario.peso?.toBigDecimal()
            it[estatura] = usuario.estatura?.toBigDecimal()
        }
        if (filasAfectadas > 0) verPorId(id) else null
    }

    override suspend fun actualizarFotoPerfil(idUsuario: Int, imagenStream: InputStream): String? = newSuspendedTransaction {
        val urlSegura = cloudinaryService.uploadImage(imagenStream) ?: return@newSuspendedTransaction null

        val filasAfectadas = UsuarioTable.update({ UsuarioTable.idUsuario eq idUsuario }) {
            it[this.fotoUrl] = urlSegura
        }

        if (filasAfectadas > 0) urlSegura else null
    }

    override suspend fun eliminar(id: Int): Boolean = newSuspendedTransaction {
        UsuarioTable.deleteWhere { UsuarioTable.idUsuario eq id } > 0
    }

    private fun toDomainConRol(row: ResultRow): Usuario = Usuario(
        id = row[UsuarioTable.idUsuario],
        nombre = row[UsuarioTable.nombres],
        primerApellido = row[UsuarioTable.primerApellido],
        segundoApellido = row[UsuarioTable.segundoApellido],
        email = row[UsuarioTable.correo],
        contrasena = row[UsuarioTable.contrasenia],
        idRol = row[UsuarioTable.idRol],
        nombreRol = row[RolTable.nombreRol],
        peso = row[UsuarioTable.peso]?.toDouble(),
        estatura = row[UsuarioTable.estatura]?.toDouble(),
        fotoUrl = row[UsuarioTable.fotoUrl]
    )
}