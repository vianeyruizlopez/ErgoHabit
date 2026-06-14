package com.alilopez.modules.autentificacion

import com.alilopez.modules.autentificacion.infrastructure.rest.AutentificacionController
import com.alilopez.modules.autentificacion.infrastructure.rest.autentificacionRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.autentificacionModuleInicial() {
    val controller by inject<AutentificacionController>()
    routing {
        autentificacionRoutes(controller)
    }
}