package com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.ConfiguracionHabitos.infrastructure.persistence.ConfiguracionHabitos
import com.alilopez.modules.usuarioFinal.ejercicio.domain.model.ProgresoEjercicio
import com.alilopez.modules.usuarioFinal.ejercicio.domain.repository.EjercicioRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.math.BigDecimal
import java.time.LocalDate

class MysqlEjercicioRepository : EjercicioRepository {

    override fun obtenerMetaKilometros(idUsuario: Int): BigDecimal = transaction {
        val meta = ConfiguracionHabitos
            .select { ConfiguracionHabitos.idUsuario eq idUsuario }
            .map { it[ConfiguracionHabitos.metaEjercicio] }
            .singleOrNull()

        when {
            meta == null || meta <= BigDecimal.ZERO -> BigDecimal("8.0")
            else -> meta
        }
    }

    override fun obtenerHistorialSemanal(idUsuario: Int, desdeFecha: LocalDate): Map<LocalDate, Double> = transaction {
        val mapa = mutableMapOf<LocalDate, Double>()

        val query = """
        SELECT DATE(fecha) as fecha_dia, SUM(km_recorridos) as total_km 
        FROM progreso_ejercicio 
        WHERE id_usuario = $idUsuario AND fecha >= '$desdeFecha' 
        GROUP BY fecha_dia
    """.trimIndent()

        exec(query) { rs ->
            while (rs.next()) {
                val fechaDb = rs.getDate("fecha_dia").toLocalDate()
                val totalKm = rs.getDouble("total_km")
                mapa[fechaDb] = totalKm
            }
        }
        mapa
    }

    override fun actualizarMetaKilometros(idUsuario: Int, nuevaMeta: BigDecimal) {
        transaction {
            val existe = ConfiguracionHabitos.select { ConfiguracionHabitos.idUsuario eq idUsuario }.singleOrNull()
            if (existe != null) {
                ConfiguracionHabitos.update({ ConfiguracionHabitos.idUsuario eq idUsuario }) {
                    it[this.metaEjercicio] = nuevaMeta
                }
            } else {
                ConfiguracionHabitos.insert {
                    it[this.idUsuario] = idUsuario
                    it[this.metaEjercicio] = nuevaMeta
                }
            }
        }
    }

    override fun registrarProgresoKm(idUsuario: Int, kmAgradados: BigDecimal, calorias: Int): Boolean = transaction {
        val hoy = LocalDate.now()
        val registroExistente = EjercicioTable
            .select { (EjercicioTable.idUsuario eq idUsuario) and (EjercicioTable.fecha eq hoy) }
            .singleOrNull()

        if (registroExistente != null) {
            val kmActuales = registroExistente[EjercicioTable.kmRecorridos]
            val caloriasActuales = registroExistente[EjercicioTable.caloriasQuemadas]

            EjercicioTable.update({ (EjercicioTable.idUsuario eq idUsuario) and (EjercicioTable.fecha eq hoy) }) {
                it[this.kmRecorridos] = kmActuales.add(kmAgradados)
                it[this.caloriasQuemadas] = caloriasActuales + calorias
            }
        } else {
            EjercicioTable.insert {
                it[this.idUsuario] = idUsuario
                it[this.fecha] = hoy
                it[this.kmRecorridos] = kmAgradados
                it[this.caloriasQuemadas] = calorias
            }
        }
        true
    }

    override fun obtenerProgresoHoy(idUsuario: Int): ProgresoEjercicio? = transaction {
        val hoy = LocalDate.now()
        EjercicioTable
            .select { (EjercicioTable.idUsuario eq idUsuario) and (EjercicioTable.fecha eq hoy) }
            .map {
                ProgresoEjercicio(
                    kmRecorridos = it[EjercicioTable.kmRecorridos],
                    caloriasQuemadas = it[EjercicioTable.caloriasQuemadas],
                    fecha = it[EjercicioTable.fecha]
                )
            }
            .singleOrNull()
    }

    override fun calcularRachaDias(idUsuario: Int): Int = transaction {
        var racha = 0
        var fechaEvaluar = LocalDate.now()

        val hoyRegistro = EjercicioTable.select { (EjercicioTable.idUsuario eq idUsuario) and (EjercicioTable.fecha eq fechaEvaluar) }.singleOrNull()
        if (hoyRegistro == null) {
            fechaEvaluar = fechaEvaluar.minusDays(1)
        }

        while (true) {
            val registro = EjercicioTable
                .select { (EjercicioTable.idUsuario eq idUsuario) and (EjercicioTable.fecha eq fechaEvaluar) }
                .singleOrNull()

            if (registro != null && registro[EjercicioTable.kmRecorridos] > BigDecimal.ZERO) {
                racha++
                fechaEvaluar = fechaEvaluar.minusDays(1)
            } else {
                break
            }
        }
        racha
    }
}