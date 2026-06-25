package com.alilopez.modules.autentificacion.infrastructure.rest

import io.ktor.server.routing.*

fun Route.autentificacionRoutes(controller: AutentificacionController) {
    route("api/v1/auth") {
        post("/login") { controller.login(call) }
        post("/register") { controller.registrar(call) }
        post("/password") { controller.restablecerContrasena(call) }
    }
}