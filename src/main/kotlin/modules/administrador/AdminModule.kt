package com.alilopez.modules.administrador

import com.alilopez.modules.administrador.reportes.application.usecase.ObtenerReportePosturaDiasExtremosUseCase
import com.alilopez.modules.administrador.reportes.application.usecase.ObtenerReportePosturaUseCase
import com.alilopez.modules.administrador.reportes.domain.repository.AdminReportesRepository
import com.alilopez.modules.administrador.reportes.infrastructure.persistence.MysqlAdminReportesRepository
import com.alilopez.modules.administrador.reportes.infrastructure.rest.AdminReportesController
import org.koin.dsl.module

val adminModule = module {
    single<AdminReportesRepository> { MysqlAdminReportesRepository() }
    factory { ObtenerReportePosturaUseCase(get()) }
    factory { ObtenerReportePosturaDiasExtremosUseCase(get()) }
    factory { AdminReportesController(get(), get()) }
}
