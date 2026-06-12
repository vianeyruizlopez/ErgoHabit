package com.alilopez.modules.usuarioFinal.progresoDiario

import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.ProgresoDiarioController
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.progresoDiarioRouting
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.progresoDiarioModuleIniciar() {
    val controller by inject<ProgresoDiarioController>()
    routing {
        progresoDiarioRouting(controller)
    }
}