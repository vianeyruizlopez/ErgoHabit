package com.alilopez.modules.usuarioFinal.frases.infrastructure.rest

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.frasesRouter(controller: FrasesController) {
    authenticate("auth-jwt") {
        route("/api/v1/frases") {

            get("/aleatoria/{categoria}") {
                controller.obtenerFraseAleatoria(call)
            }

            get("/aleatoria") {
                controller.obtenerFraseAleatoria(call)
            }

        }
    }
}