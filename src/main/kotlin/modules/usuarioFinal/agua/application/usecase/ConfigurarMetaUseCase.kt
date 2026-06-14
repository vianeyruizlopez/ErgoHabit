package com.alilopez.modules.usuarioFinal.agua.application.usecase

import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository

class ConfigurarMetaUseCase(private val repository: AguaRepository) {

    fun manual(idUsuario: Int, metaMl: Int?): Boolean {
        val metaFinal = metaMl ?: 2450

        if (metaFinal < 1000) {
            throw IllegalArgumentException("La meta mínima permitida es de 1000 ml.")
        }
        return repository.actualizarMetaManual(idUsuario, metaFinal)
    }

    fun porPesoYEstatura(idUsuario: Int, pesoKg: Double?, estaturaCm: Double?): Boolean {
        if (pesoKg == null || estaturaCm == null) {
            throw IllegalArgumentException("Para el cálculo automático se requiere tanto el peso como la estatura.")
        }

        if (pesoKg <= 0.0 || estaturaCm <= 0.0) {
            throw IllegalArgumentException("El peso y la estatura deben ser mayores a cero.")
        }

        val basePeso = pesoKg * 35
        val ajusteEstatura = if (estaturaCm > 160.0) 100 else 0
        val metaCalculada = (basePeso + ajusteEstatura).toInt()

        return repository.guardarMetaInteligente(idUsuario, metaCalculada, pesoKg, estaturaCm)
    }
}