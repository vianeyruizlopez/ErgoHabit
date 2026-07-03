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
        val cumplioAgua = aguaConsumida >= metaAgua
        val cumplioSueno = horasSueno >= 7.0
        val cumplioEjercicio = (metaEjercicio > 0 && kmRecorridos >= metaEjercicio)
        val cumplioPostura = alertasPostura <= 30
        val cumplioNutricion = comidasCompletadas >= 3

        var habitosGanadosHoy = 0
        if (cumplioAgua) habitosGanadosHoy++
        if (cumplioSueno) habitosGanadosHoy++
        if (cumplioEjercicio) habitosGanadosHoy++
        if (cumplioPostura) habitosGanadosHoy++
        if (cumplioNutricion) habitosGanadosHoy++

        val elDiaCuentaParaRacha = habitosGanadosHoy >= 2

        var diasRacha = 0
        var requiereActualizarBD = false
        var nuevoValorBD = 0
        var tieneFilaRacha = false

        exec("SELECT dias_consecutivos, ultima_actividad FROM racha_usuario WHERE id_usuario = $idUsuario") { rs ->
            if (rs.next()) {
                tieneFilaRacha = true
                val diasBaseDatos = rs.getInt("dias_consecutivos")
                val ultimaActividadTimestamp = rs.getTimestamp("ultima_actividad")?.toInstant()
                val ultimaFechaBD = ultimaActividadTimestamp?.atZone(java.time.ZoneId.systemDefault())?.toLocalDate()
                val ayer = hoy.minusDays(1)

                if (elDiaCuentaParaRacha) {
                    when (ultimaFechaBD) {
                        hoy -> {
                            diasRacha = if (diasBaseDatos == 0) 1 else diasBaseDatos

                            if (diasBaseDatos == 0) {
                                requiereActualizarBD = true
                                nuevoValorBD = 1
                            }
                        }
                        ayer -> {
                            diasRacha = diasBaseDatos + 1
                            requiereActualizarBD = true
                            nuevoValorBD = diasRacha
                        }
                        else -> {
                            diasRacha = 1
                            requiereActualizarBD = true
                            nuevoValorBD = 1
                        }
                    }
                } else {
                    if (ultimaFechaBD == hoy || ultimaFechaBD == ayer) {
                        diasRacha = diasBaseDatos
                    } else {
                        diasRacha = 0
                        requiereActualizarBD = (diasBaseDatos != 0)
                        nuevoValorBD = 0
                    }
                }
            }
        }

        try {
            if (!tieneFilaRacha) {
                val valorInicial = if (elDiaCuentaParaRacha) 1 else 0
                exec("INSERT INTO racha_usuario (id_usuario, dias_consecutivos, ultima_actividad) VALUES ($idUsuario, $valorInicial, NOW())")
                diasRacha = valorInicial
            } else if (requiereActualizarBD) {
                exec("UPDATE racha_usuario SET dias_consecutivos = $nuevoValorBD, ultima_actividad = NOW() WHERE id_usuario = $idUsuario")
            }
        } catch (e: Exception) {
            println("Persistencia de racha controlada de forma segura: ${e.message}")
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