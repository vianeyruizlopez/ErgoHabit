package com.alilopez.modules.usuarioFinal.ergonomia.infrastructure.rest

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.posturaRouter(controller: PosturaController) {

    authenticate("auth-jwt") {
        route("api/v1/ergonomia") {

            post("/sincronizar") {
                controller.sincronizarAlertas(call)
            }

            get("/historial") {
                controller.verHistorial(call)
            }

            get("/progreso-semanal") {
                controller.obtenerProgresoSemanal(call)
            }
        }
    }
}