package com.alilopez.modules.usuarioFinal.agua.domain.repository

import com.alilopez.modules.usuarioFinal.agua.domain.model.DashboardAgua

interface AguaRepository {
    fun obtenerDashboard(idUsuario: Int): DashboardAgua
    fun registrarToma(idUsuario: Int, cantidadMl: Int): Boolean
    fun actualizarMetaManual(idUsuario: Int, nuevaMetaMl: Int): Boolean
    fun guardarMetaInteligente(idUsuario: Int, metaCalculada: Int, pesoKg: Double, estaturaCm: Double): Boolean}