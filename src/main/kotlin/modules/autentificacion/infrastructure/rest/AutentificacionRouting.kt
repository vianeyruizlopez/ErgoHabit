package com.alilopez.modules.autentificacion.infrastructure.rest

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.autentificacionRoutes() {
    val controller by inject<AutentificacionController>()

    route("/auth") {
        post("/login") { controller.login(call) }
        post("/register") { controller.registrar(call) }
    }
}