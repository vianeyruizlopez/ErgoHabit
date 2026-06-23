package com.alilopez.modules.usuarios.infrastructure.rest

import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Route.usuarioRouting(controller: UsuarioController) {

    authenticate("auth-jwt") {
        route("api/v1/usuarios") {

            get("/{id}") {
                controller.verPerfil(call)
            }

            get("/usuarios") {
                controller.verTodos(call)
            }

            get("/administrador") {
                controller.verTodos(call)
            }

            put("/{id}") {
                controller.actualizar(call)
            }

            delete("/{id}") {
                controller.eliminar(call)
            }

            patch("/perfil/foto") {
                controller.actualizarFotoPerfil(call)
            }
        }
    }
}