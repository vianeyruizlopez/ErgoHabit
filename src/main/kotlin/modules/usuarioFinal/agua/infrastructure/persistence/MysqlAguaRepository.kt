package com.alilopez.modules.usuarioFinal.agua.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.ConfiguracionHabitos.infrastructure.persistence.ConfiguracionHabitos
import com.alilopez.modules.usuarioFinal.agua.domain.model.DashboardAgua
import com.alilopez.modules.usuarioFinal.agua.domain.model.TomaCronologica
import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository
import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MysqlAguaRepository : AguaRepository {

    override fun obtenerDashboard(idUsuario: Int): DashboardAgua = transaction {
        val zona = ZoneId.of("America/Mexico_City")
        val hoy = LocalDate.now(zona)

        val metaConfigurada = ConfiguracionHabitos
            .select { ConfiguracionHabitos.idUsuario eq idUsuario }
            .map { it[ConfiguracionHabitos.metaAgua] }
            .singleOrNull() ?: 2450


        val progresoRow = ProgresoAguaTable
            .select { (ProgresoAguaTable.idUsuario eq idUsuario) and (ProgresoAguaTable.fecha eq hoy) }
            .singleOrNull()

        val consumidoHoy = progresoRow?.get(ProgresoAguaTable.cantidadConsumida) ?: 0

        val usuarioRow = UsuarioTable
            .select { UsuarioTable.idUsuario eq idUsuario }
            .singleOrNull()

        val peso = usuarioRow?.get(UsuarioTable.peso)?.toDouble() ?: 0.0
        val estatura = usuarioRow?.get(UsuarioTable.estatura)?.toDouble() ?: 0.0

        val inicioHoy = hoy.atStartOfDay()
        val finHoy = hoy.atTime(23, 59, 59)
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val historial = AguaDetalleTable
            .select { (AguaDetalleTable.idUsuario eq idUsuario) and (AguaDetalleTable.fechaHora.between(inicioHoy, finHoy)) }
            .orderBy(AguaDetalleTable.fechaHora to SortOrder.DESC)
            .map {
                TomaCronologica(
                    id = it[AguaDetalleTable.idDetalle],
                    cantidadMl = it[AguaDetalleTable.cantidadMl],
                    hora = it[AguaDetalleTable.fechaHora].format(formatter)
                )
            }

        val porcentaje = if (metaConfigurada > 0) ((consumidoHoy * 100) / metaConfigurada) else 0
        val vasos = consumidoHoy / 250
        val restante = (metaConfigurada - consumidoHoy).coerceAtLeast(0)

        DashboardAgua(metaConfigurada, consumidoHoy, porcentaje, vasos, restante, estatura, peso, historial)
    }

    override fun registrarToma(idUsuario: Int, cantidadMl: Int): Boolean = transaction {
        val zona = ZoneId.of("America/Mexico_City")
        val hoy = LocalDate.now(zona)
        AguaDetalleTable.insert {
            it[this.idUsuario] = idUsuario
            it[this.cantidadMl] = cantidadMl
            it[this.fechaHora] = LocalDateTime.now(zona)
        }
        val filaExistente = ProgresoAguaTable
            .select { (ProgresoAguaTable.idUsuario eq idUsuario) and (ProgresoAguaTable.fecha eq hoy) }
            .singleOrNull()

        if (filaExistente != null) {
            val anteriorConsumo = filaExistente[ProgresoAguaTable.cantidadConsumida]
            ProgresoAguaTable.update({ (ProgresoAguaTable.idUsuario eq idUsuario) and (ProgresoAguaTable.fecha eq hoy) }) {
                it[this.cantidadConsumida] = anteriorConsumo + cantidadMl
            }
        } else {
            ProgresoAguaTable.insert {
                it[this.idUsuario] = idUsuario
                it[this.fecha] = hoy
                it[this.cantidadConsumida] = cantidadMl
            }
        }
        true
    }

    override fun actualizarMetaManual(idUsuario: Int, nuevaMetaMl: Int): Boolean = transaction {
        val registroExistente = ConfiguracionHabitos
            .select { ConfiguracionHabitos.idUsuario eq idUsuario }
            .singleOrNull()

        if (registroExistente != null) {
            ConfiguracionHabitos.update({ ConfiguracionHabitos.idUsuario eq idUsuario }) {
                it[this.metaAgua] = nuevaMetaMl
            }
        } else {
            ConfiguracionHabitos.insert {
                it[this.idUsuario] = idUsuario
                it[this.metaAgua] = nuevaMetaMl
            }
        }
        true
    }



    override fun guardarMetaInteligente(idUsuario: Int, metaCalculada: Int, pesoKg: Double, estaturaCm: Double): Boolean = transaction {
        val hoy = LocalDate.now(ZoneId.of("America/Mexico_City"))

        val registroConfigExistente = ConfiguracionHabitos
            .select { ConfiguracionHabitos.idUsuario eq idUsuario }
            .singleOrNull()

        if (registroConfigExistente != null) {
            ConfiguracionHabitos.update({ ConfiguracionHabitos.idUsuario eq idUsuario }) {
                it[this.metaAgua] = metaCalculada
            }
        } else {
            ConfiguracionHabitos.insert {
                it[this.idUsuario] = idUsuario
                it[this.metaAgua] = metaCalculada
                it[this.horaDormir] = java.time.LocalTime.MIDNIGHT
                it[this.horaDespertar] = java.time.LocalTime.MIDNIGHT
                it[this.metaEjercicio] = java.math.BigDecimal.ZERO
            }
        }

        UsuarioTable.update({ UsuarioTable.idUsuario eq idUsuario }) {
            it[this.peso] = java.math.BigDecimal.valueOf(pesoKg)
            it[this.estatura] = java.math.BigDecimal.valueOf(estaturaCm)
        }

        val filaExistente = ProgresoAguaTable
            .select { (ProgresoAguaTable.idUsuario eq idUsuario) and (ProgresoAguaTable.fecha eq hoy) }
            .singleOrNull()

        if (filaExistente == null) {
            ProgresoAguaTable.insert {
                it[this.idUsuario] = idUsuario
                it[this.fecha] = hoy
                it[this.cantidadConsumida] = 0
            }
        }

        true
    }
    override fun obtenerHistorialSemanal(idUsuario: Int, desdeFecha: LocalDate): Map<LocalDate, Int> = transaction {
        val mapa = mutableMapOf<LocalDate, Int>()
        exec("SELECT DATE(fecha) as fecha_dia, SUM(cantidad_consumida) as total_ml FROM progreso_agua WHERE id_usuario = $idUsuario AND fecha >= '$desdeFecha' GROUP BY fecha_dia") { rs ->
            while (rs.next()) {
                val fechaDb = rs.getDate("fecha_dia").toLocalDate()
                val totalMl = rs.getInt("total_ml")
                mapa[fechaDb] = totalMl
            }
        }
        mapa
    }
}