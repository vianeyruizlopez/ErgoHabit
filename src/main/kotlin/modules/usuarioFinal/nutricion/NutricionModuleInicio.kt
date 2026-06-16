package com.alilopez.modules.usuarioFinal.nutricion

import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.NutricionController
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.nutricionRouter
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.nutricionModuleInicio() {
    val controller by inject<NutricionController>()
    routing { nutricionRouter(controller)}
}