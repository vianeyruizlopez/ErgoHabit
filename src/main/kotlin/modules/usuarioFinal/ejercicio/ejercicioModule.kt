package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.ConfigurarMetaEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.ObtenerProgresoEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.RegistrarKilometrosUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.application.usecase.VerDashboardEjercicioUseCase
import com.alilopez.modules.usuarioFinal.ejercicio.domain.repository.EjercicioRepository
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.persistence.MysqlEjercicioRepository
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.EjercicioController
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.ejercicioRouter
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.dsl.module
import org.koin.ktor.ext.inject

val ejercicioModule = module {
    single<EjercicioRepository> { MysqlEjercicioRepository() }
    factory { ConfigurarMetaEjercicioUseCase(get()) }
    factory { RegistrarKilometrosUseCase(get()) }
    factory { VerDashboardEjercicioUseCase(get()) }
    factory { ObtenerProgresoEjercicioUseCase(get()) }

    factory { EjercicioController(get(), get(), get(),get()) }
}
fun Application.ejercicioModuleInicio() {
    val controller by inject<EjercicioController>()
    routing { ejercicioRouter(controller) }
}