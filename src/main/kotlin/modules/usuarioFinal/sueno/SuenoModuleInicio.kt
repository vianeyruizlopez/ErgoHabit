package com.alilopez.modules.usuarioFinal.sueno

import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.AguaController
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.SuenoController
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.suenoRouter
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.suenoModuleInicio() {
    val controller by inject<SuenoController>()
    routing {suenoRouter(controller)}
}