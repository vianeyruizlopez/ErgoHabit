package com.alilopez.modules.usuarioFinal.nutricion.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.ConfiguracionHabitos.infrastructure.persistence.ConfiguracionHabitos
import com.alilopez.modules.usuarioFinal.nutricion.domain.model.HorarioNutricion
import com.alilopez.modules.usuarioFinal.nutricion.domain.model.ProgresoNutricion
import com.alilopez.modules.usuarioFinal.nutricion.domain.repository.NutricionRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalTime
import java.math.BigDecimal

class MysqlNutricionRepository : NutricionRepository {

    override fun guardarHorariosConfigurados(idUsuario: Int, desayuno: LocalTime, comida: LocalTime, cena: LocalTime) {
        transaction {
            val registroExistente = ConfiguracionHabitos
                .select { ConfiguracionHabitos.idUsuario eq idUsuario }
                .singleOrNull()

            if (registroExistente != null) {
                ConfiguracionHabitos.update({ ConfiguracionHabitos.idUsuario eq idUsuario }) {
                    it[this.horaDesayuno] = desayuno
                    it[this.horaComida] = comida
                    it[this.horaCena] = cena
                }
            } else {
                ConfiguracionHabitos.insert {
                    it[this.idUsuario] = idUsuario
                    it[this.horaDesayuno] = desayuno
                    it[this.horaComida] = comida
                    it[this.horaCena] = cena
                    it[this.metaAgua] = 2450
                    it[this.metaEjercicio] = BigDecimal.ZERO
                    it[this.horaDormir] = LocalTime.MIDNIGHT
                    it[this.horaDespertar] = LocalTime.MIDNIGHT
                }
            }
        }
    }

    override fun obtenerHorariosConfigurados(idUsuario: Int): HorarioNutricion? = transaction {
        ConfiguracionHabitos
            .select { ConfiguracionHabitos.idUsuario eq idUsuario }
            .map {
                HorarioNutricion(
                    horaDesayuno = it[ConfiguracionHabitos.horaDesayuno],
                    horaComida = it[ConfiguracionHabitos.horaComida],
                    horaCena = it[ConfiguracionHabitos.horaCena]
                )
            }
            .singleOrNull()
    }

    override fun registrarConsumoComida(idUsuario: Int, tipoComida: String, estado: Boolean): Boolean = transaction {
        val hoy = LocalDate.now()

        val registroExistente = NutricionTable
            .select { (NutricionTable.idUsuario eq idUsuario) and (NutricionTable.fecha eq hoy) }
            .singleOrNull()

        if (registroExistente != null) {
            NutricionTable.update({ (NutricionTable.idUsuario eq idUsuario) and (NutricionTable.fecha eq hoy) }) {
                when (tipoComida) {
                    "DESAYUNO" -> it[this.realizoDesayuno] = estado
                    "COMIDA" -> it[this.realizoComida] = estado
                    "CENA" -> it[this.realizoCena] = estado
                }
            }
        } else {
            NutricionTable.insert {
                it[this.idUsuario] = idUsuario
                it[this.fecha] = hoy
                it[this.realizoDesayuno] = if (tipoComida == "DESAYUNO") estado else false
                it[this.realizoComida] = if (tipoComida == "COMIDA") estado else false
                it[this.realizoCena] = if (tipoComida == "CENA") estado else false
            }
        }
        true
    }

    override fun obtenerProgresoComidasHoy(idUsuario: Int): ProgresoNutricion? = transaction {
        val hoy = LocalDate.now()

        NutricionTable
            .select { (NutricionTable.idUsuario eq idUsuario) and (NutricionTable.fecha eq hoy) }
            .map {
                ProgresoNutricion(
                    realizoDesayuno = it[NutricionTable.realizoDesayuno],
                    realizoComida = it[NutricionTable.realizoComida],
                    realizoCena = it[NutricionTable.realizoCena]
                )
            }
            .singleOrNull()
    }
}