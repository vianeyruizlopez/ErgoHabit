package com.alilopez.modules.usuarioFinal.tarea.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.frasesMotivacionales.infrastructure.persistence.FrasesMotivacionalesTable
import com.alilopez.modules.usuarioFinal.tarea.domain.model.TareaEnfoque
import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.Instant

class MysqlTareaRepository : TareaRepository {

    private fun toModel(row: ResultRow): TareaEnfoque {
        return TareaEnfoque(
            idTarea = row[TareaEnfoqueTable.idTarea],
            idUsuario = row[TareaEnfoqueTable.idUsuario],
            idEstado = row[TareaEnfoqueTable.idEstado],
            titulo = row[TareaEnfoqueTable.titulo],
            categoria = row[CategoriasTareasTable.nombreCategoria],
            duracionTarea = row[TareaEnfoqueTable.duracionTarea],
            fechaCreacion = row[TareaEnfoqueTable.fechaCreacion],
            duracionInicial = row[TareaEnfoqueTable.duracionInicial],
            fechaInicioCronometro = row[TareaEnfoqueTable.fechaInicioCronometro]
        )
    }

    override fun crearTarea(idUsuario: Int, titulo: String, categoria: String, duracion: Int): TareaEnfoque = transaction {
        val categoriaExistente = CategoriasTareasTable
            .select { CategoriasTareasTable.nombreCategoria eq categoria }
            .singleOrNull()

        val idCat = if (categoriaExistente != null) {
            categoriaExistente[CategoriasTareasTable.idCategoria]
        } else {
            CategoriasTareasTable.insert {
                it[this.nombreCategoria] = categoria
            } get CategoriasTareasTable.idCategoria
        }

        val idInsercion = TareaEnfoqueTable.insert {
            it[this.idUsuario] = idUsuario
            it[this.idCategoria] = idCat
            it[this.titulo] = titulo
            it[this.duracionTarea] = duracion
            it[this.duracionInicial] = duracion
            it[this.idEstado] = 1
            it[this.fechaCreacion] = Instant.now()
            it[this.fechaInicioCronometro] = null
        } get TareaEnfoqueTable.idTarea

        val filaInsertada = TareaEnfoqueTable.join(
            CategoriasTareasTable,
            joinType = JoinType.INNER,
            onColumn = TareaEnfoqueTable.idCategoria,
            otherColumn = CategoriasTareasTable.idCategoria
        )
            .select { TareaEnfoqueTable.idTarea eq idInsercion }
            .single()

        toModel(filaInsertada)
    }

    override fun obtenerTareasPendientes(idUsuario: Int): List<TareaEnfoque> = transaction {
        TareaEnfoqueTable.join(
            CategoriasTareasTable,
            joinType = JoinType.INNER,
            onColumn = TareaEnfoqueTable.idCategoria,
            otherColumn = CategoriasTareasTable.idCategoria
        )
            .select {
                (TareaEnfoqueTable.idUsuario eq idUsuario) and
                        (TareaEnfoqueTable.idEstado eq 1)
            }
            .orderBy(TareaEnfoqueTable.idTarea to SortOrder.DESC)
            .map { toModel(it) }
    }

    override fun obtenerTareasCompletadasHoy(idUsuario: Int): List<TareaEnfoque> = transaction {
        val inicioHoy = LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()
        val finHoy = LocalDate.now().plusDays(1).atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()

        TareaEnfoqueTable.join(
            CategoriasTareasTable,
            joinType = JoinType.INNER,
            onColumn = TareaEnfoqueTable.idCategoria,
            otherColumn = CategoriasTareasTable.idCategoria
        )
            .select {
                (TareaEnfoqueTable.idUsuario eq idUsuario) and
                        (TareaEnfoqueTable.idEstado eq 2) and
                        (TareaEnfoqueTable.fechaCreacion.between(inicioHoy, finHoy))
            }
            .map { toModel(it) }
    }

    override fun cambiarEstadoTarea(idTarea: Int, idUsuario: Int, nuevoEstado: Int): Boolean = transaction {
        TareaEnfoqueTable.update({ (TareaEnfoqueTable.idTarea eq idTarea) and (TareaEnfoqueTable.idUsuario eq idUsuario) }) {
            it[this.idEstado] = nuevoEstado
        } > 0
    }

    override fun agregarMinutosTarea(idTarea: Int, idUsuario: Int, minutosExtra: Int): Boolean = transaction {
        val tareaActual = buscarPorId(idTarea, idUsuario) ?: return@transaction false

        TareaEnfoqueTable.update({ (TareaEnfoqueTable.idTarea eq idTarea) and (TareaEnfoqueTable.idUsuario eq idUsuario) }) {
            it[this.duracionTarea] = tareaActual.duracionTarea + minutosExtra
        } > 0
    }

    override fun buscarPorId(idTarea: Int, idUsuario: Int): TareaEnfoque? = transaction {
        TareaEnfoqueTable.join(
            CategoriasTareasTable,
            joinType = JoinType.INNER,
            onColumn = TareaEnfoqueTable.idCategoria,
            otherColumn = CategoriasTareasTable.idCategoria
        )
            .select { (TareaEnfoqueTable.idTarea eq idTarea) and (TareaEnfoqueTable.idUsuario eq idUsuario) }
            .map { toModel(it) }
            .singleOrNull()
    }

    override fun obtenerFraseAleatoria(categoria: String): String = transaction {
        FrasesMotivacionalesTable.select { FrasesMotivacionalesTable.categoria eq categoria }
            .orderBy(Random() to SortOrder.ASC)
            .limit(1)
            .map { it[FrasesMotivacionalesTable.texto] }
            .singleOrNull() ?: "¡Sigue adelante con determinación!"
    }

    override fun obtenerPausaFisicaAleatoria(): String {
        return try {
            transaction {
                PausaTareaTable.selectAll()
                    .orderBy(Random() to SortOrder.ASC)
                    .limit(1)
                    .map { it[PausaTareaTable.accionFisica] }
                    .singleOrNull() ?: "Tómate un minuto para estirar tus hombros."
            }
        } catch (e: Exception) {
            "Tómate un minuto para estirar tus hombros."
        }
    }

    override fun iniciarCronometroEnBaseDatos(idTarea: Int, idUsuario: Int): Boolean = transaction {
        TareaEnfoqueTable.update({ (TareaEnfoqueTable.idTarea eq idTarea) and (TareaEnfoqueTable.idUsuario eq idUsuario) }) {
            it[this.idEstado] = 1
            it[this.fechaInicioCronometro] = Instant.now()
        } > 0
    }

    override fun guardarPausaEnBaseDatos(idTarea: Int, idUsuario: Int, nuevoTiempoRestante: Int): Boolean = transaction {
        val tareaActual = buscarPorId(idTarea, idUsuario)
        TareaEnfoqueTable.update({
            (TareaEnfoqueTable.idTarea eq idTarea) and (TareaEnfoqueTable.idUsuario eq idUsuario)
        }) {
            it[this.idEstado] = 1
            it[this.duracionTarea] = nuevoTiempoRestante
            if (nuevoTiempoRestante == 0) {
                it[this.fechaInicioCronometro] = tareaActual?.fechaInicioCronometro
            } else {
                it[this.fechaInicioCronometro] = null
            }
        } > 0
    }

    override fun eliminarTarea(idTarea: Int, idUsuario: Int): Boolean = transaction {
        TareaEnfoqueTable.deleteWhere {
            (TareaEnfoqueTable.idTarea eq idTarea) and (TareaEnfoqueTable.idUsuario eq idUsuario)
        } > 0
    }
}