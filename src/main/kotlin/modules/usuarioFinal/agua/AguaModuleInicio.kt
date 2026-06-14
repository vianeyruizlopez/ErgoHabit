package com.alilopez.modules.usuarioFinal.agua

import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.AguaController
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.aguaRouter
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.aguaModuleInicio() {
    val controller by inject<AguaController>()
    routing {aguaRouter(controller)}
}