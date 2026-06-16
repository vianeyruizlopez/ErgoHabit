package com.alilopez.modules.usuarioFinal.sueno.infrastructure.persistence

import com.alilopez.modules.usuarioFinal.ConfiguracionHabitos.infrastructure.persistence.ConfiguracionHabitos
import com.alilopez.modules.usuarioFinal.sueno.domain.model.HorarioSueno
import com.alilopez.modules.usuarioFinal.sueno.domain.model.ProgresoSueno
import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalTime
import java.time.LocalDate
import java.math.BigDecimal

class MysqlSuenoRepository : SuenoRepository {

    override fun guardarConfiguracionHorario(idUsuario: Int, horaDormir: LocalTime, horaDespertar: LocalTime) {
        transaction {
            val registroExistente = ConfiguracionHabitos
                .select { ConfiguracionHabitos.idUsuario eq idUsuario }
                .singleOrNull()

            if (registroExistente != null) {
                ConfiguracionHabitos.update({ ConfiguracionHabitos.idUsuario eq idUsuario }) {
                    it[this.horaDormir] = horaDormir
                    it[this.horaDespertar] = horaDespertar
                }
            } else {
                ConfiguracionHabitos.insert {
                    it[this.idUsuario] = idUsuario
                    it[this.horaDormir] = horaDormir
                    it[this.horaDespertar] = horaDespertar
                    it[this.metaAgua] = 2450
                    it[this.metaEjercicio] = BigDecimal.ZERO
                }
            }
        }
    }
    override fun obtenerConfiguracion(idUsuario: Int): HorarioSueno? = transaction {
        ConfiguracionHabitos
            .select { ConfiguracionHabitos.idUsuario eq idUsuario }
            .map {
                HorarioSueno(
                    horaDormir = it[ConfiguracionHabitos.horaDormir],
                    horaDespertar = it[ConfiguracionHabitos.horaDespertar]
                )
            }
            .singleOrNull()
    }

    override fun registrarProgresoSuenoDiario(idUsuario: Int, horasDormidas: Double, despertoATiempo: Boolean): Boolean = transaction {
        val hoy = LocalDate.now()

        val filaExistente = SuenoTable
            .select { (SuenoTable.idUsuario eq idUsuario) and (SuenoTable.fecha eq hoy) }
            .singleOrNull()

        if (filaExistente != null) {
            SuenoTable.update({ (SuenoTable.idUsuario eq idUsuario) and (SuenoTable.fecha eq hoy) }) {
                it[this.horasDormidas] = BigDecimal.valueOf(horasDormidas)
                it[this.despertoATiempo] = despertoATiempo
            }
        } else {
            SuenoTable.insert {
                it[this.idUsuario] = idUsuario
                it[this.fecha] = hoy
                it[this.horasDormidas] = BigDecimal.valueOf(horasDormidas)
                it[this.despertoATiempo] = despertoATiempo
            }
        }
        true
    }

    override fun obtenerProgresoHoy(idUsuario: Int): ProgresoSueno? = transaction {
        val hoy = LocalDate.now()

        SuenoTable
            .select { (SuenoTable.idUsuario eq idUsuario) and (SuenoTable.fecha eq hoy) }
            .map {
                ProgresoSueno(
                    horasDormidas = it[SuenoTable.horasDormidas].toDouble(),
                    despertoATiempo = it[SuenoTable.despertoATiempo]
                )
            }
            .singleOrNull()
    }

}