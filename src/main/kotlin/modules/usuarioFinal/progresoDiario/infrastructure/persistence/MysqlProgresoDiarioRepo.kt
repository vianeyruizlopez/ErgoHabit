package com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.progresoDiario.domain.model.ProgresosDiarioDB
import com.alilopez.modules.usuarioFinal.progresoDiario.domain.repository.ProgresoDiarioRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate

class MysqlProgresoDiarioRepo : ProgresoDiarioRepository {
    override suspend fun obtenerDatosCrudosHoy(idUsuario: Int): ProgresosDiarioDB = transaction {
        val hoy = LocalDate.now()

        var metaAgua = 2450
        var metaEjercicio = 8.0

        exec("SELECT meta_agua, meta_ejercicio FROM configuracion_habitos WHERE id_usuario = $idUsuario") { rs ->
            if (rs.next()) {
                metaAgua = rs.getInt("meta_agua")
                metaEjercicio = rs.getDouble("meta_ejercicio")
            }
        }

        var aguaConsumida = 0
        exec("SELECT cantidad_consumida FROM progreso_agua WHERE id_usuario = $idUsuario AND fecha = '$hoy'") { rs ->
            if (rs.next()) aguaConsumida = rs.getInt("cantidad_consumida")
        }

        var horasSueno = 0.0
        exec("SELECT horas_dormidas FROM progreso_sueno WHERE id_usuario = $idUsuario AND DATE(fecha) = '$hoy'") { rs ->
            if (rs.next()) horasSueno = rs.getDouble("horas_dormidas")
        }

        var kmRecorridos = 0.0
        var caloriasQuemadas = 0
        exec("SELECT km_recorridos, calorias_quemadas FROM progreso_ejercicio WHERE id_usuario = $idUsuario AND DATE(fecha) = '$hoy'") { rs ->
            if (rs.next()) {
                kmRecorridos = rs.getDouble("km_recorridos")
                caloriasQuemadas = rs.getInt("calorias_quemadas")
            }
        }

        var alertasPostura = 0
        exec("SELECT total_alertas FROM historial_postura WHERE id_usuario = $idUsuario AND DATE(fecha) = '$hoy'") { rs ->
            if (rs.next()) alertasPostura = rs.getInt("total_alertas")
        }

        var diasRacha = 0
        exec("SELECT dias_consecutivos FROM racha_usuario WHERE id_usuario = $idUsuario") { rs ->
            if (rs.next()) diasRacha = rs.getInt("dias_consecutivos")
        }

        var comidasCompletadas = 0
        exec(
            """
            SELECT realizo_desayuno, realizo_comida, realizo_cena
            FROM progreso_nutricion
            WHERE id_usuario = $idUsuario AND fecha = '$hoy'
            """.trimIndent()
        ) { rs ->
            if (rs.next()) {
                if (rs.getBoolean("realizo_desayuno")) comidasCompletadas++
                if (rs.getBoolean("realizo_comida")) comidasCompletadas++
                if (rs.getBoolean("realizo_cena")) comidasCompletadas++
            }
        }

        ProgresosDiarioDB(
            metaAgua = metaAgua,
            metaEjercicio = metaEjercicio,
            aguaConsumida = aguaConsumida,
            horasSueno = horasSueno,
            kmRecorridos = kmRecorridos,
            caloriasQuemadas = caloriasQuemadas,
            alertasPostura = alertasPostura,
            diasRacha = diasRacha,
            comidasCompletadas = comidasCompletadas
        )
    }
}