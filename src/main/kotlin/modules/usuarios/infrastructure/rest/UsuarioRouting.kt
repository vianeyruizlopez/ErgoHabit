package com.alilopez.modules.usuarios.infrastructure.rest

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.usuarioRouting() {
    val usuarioController by inject<UsuarioController>()

    authenticate("auth-jwt") {
        route("/usuarios") {

            get("/{id}") {
                usuarioController.verPerfil(call)
            }

            get("/usuarios") {
                usuarioController.verTodos(call)
            }

            get("/administrador") {
                usuarioController.verTodos(call)
            }

            put("/{id}") {
                usuarioController.actualizar(call)
            }

            delete("/{id}") {
                usuarioController.eliminar(call)
            }
        }
    }
}