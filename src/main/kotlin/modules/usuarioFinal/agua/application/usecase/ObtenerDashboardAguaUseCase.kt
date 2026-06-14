package com.alilopez.modules.usuarioFinal.agua.application.usecase

import com.alilopez.modules.usuarioFinal.agua.domain.model.DashboardAgua
import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository

class ObtenerDashboardAguaUseCase(private val repository: AguaRepository) {
    fun execute(idUsuario: Int): DashboardAgua = repository.obtenerDashboard(idUsuario)
}