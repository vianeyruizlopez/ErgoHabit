package com.alilopez.modules.autentificacion.infrastructure.rest

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.autentificacionRoutes(controller: AutentificacionController) {
    route("api/v1/auth") {
        post("/login") { controller.login(call) }
        post("/register") { controller.registrar(call) }
    }
}