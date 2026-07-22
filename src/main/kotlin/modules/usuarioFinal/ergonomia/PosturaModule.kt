package com.alilopez.modules.usuarioTester.ergonomia.infrestructure.rest

import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.ObtenerProgresoSemanalUseCase
import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.RegistrarPosturaUseCase
import com.alilopez.modules.usuarioFinal.ergonomia.application.usecase.VerHistorialPosturaUseCase
import com.alilopez.modules.usuarioFinal.ergonomia.domain.repository.PosturaRepository
import com.alilopez.modules.usuarioFinal.ergonomia.infrastructure.rest.PosturaController
import com.alilopez.modules.usuarioFinal.ergonomia.infrastructure.rest.posturaRouter
import com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.persistence.MysqlPosturaRepository
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import kotlin.getValue

val posturaModule = module {
    single<PosturaRepository> { MysqlPosturaRepository() }
    factory { RegistrarPosturaUseCase(get()) }
    factory { VerHistorialPosturaUseCase(get()) }
    factory { ObtenerProgresoSemanalUseCase(get()) }

    factory {
        PosturaController(
            registrarPosturaUseCase = get<RegistrarPosturaUseCase>(),
            verHistorialPosturaUseCase = get<VerHistorialPosturaUseCase>(),
            obtenerProgresoSemanalUseCase = get()
        )
    }
}

fun Application.posturaModuleInicio() {
    val controller by inject<PosturaController>()
    routing {posturaRouter(controller) }
}