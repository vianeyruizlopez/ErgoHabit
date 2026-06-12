package com.alilopez.modules.usuarios.infrastructure

import com.alilopez.modules.autentificacion.infrastructure.rest.AutentificacionController
import com.alilopez.modules.autentificacion.infrastructure.rest.autentificacionRoutes
import com.alilopez.modules.usuarios.infrastructure.rest.UsuarioController
import com.alilopez.modules.usuarios.infrastructure.rest.usuarioRouting
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.usuarioModuleIniciar() {
    val controller by inject<UsuarioController>()
    routing {
        usuarioRouting(controller)
    }

}