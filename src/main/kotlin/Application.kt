package com.alilopez

import com.alilopez.common.infrastructure.DatabaseFactory
import com.alilopez.common.infrastructure.security.configureSecurity
import com.alilopez.modules.autentificacion.autentificacionModule
import com.alilopez.modules.autentificacion.infrastructure.rest.AutentificacionController
import com.alilopez.modules.autentificacion.infrastructure.rest.autentificacionRoutes
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.AguaController
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.aguaRouter
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.EjercicioController
import com.alilopez.modules.usuarioFinal.ejercicio.infrastructure.rest.ejercicioRouter
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.NutricionController
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.nutricionRouter
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.ProgresoDiarioController
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.progresoDiarioRouting
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.SuenoController
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.suenoRouter
import com.alilopez.modules.usuarioTester.habitos.infrastructure.rest.aguaModule
import com.alilopez.modules.usuarioTester.habitos.infrastructure.rest.ejercicioModule
import com.alilopez.modules.usuarioTester.habitos.infrastructure.rest.nutricionModule
import com.alilopez.modules.usuarioTester.habitos.infrastructure.rest.progresoDiarioModule
import com.alilopez.modules.usuarioTester.habitos.infrastructure.rest.suenoModule
import com.alilopez.modules.usuarios.infrastructure.rest.UsuarioController
import com.alilopez.modules.usuarios.infrastructure.rest.usuarioRouting
import com.alilopez.modules.usuarios.usuarioModule
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import io.ktor.server.plugins.cors.routing.*
import org.koin.ktor.ext.inject

fun Application.module() {
    DatabaseFactory.init()
    // 1. Configuración de Inyección de Dependencias


    install(Koin) {
        slf4jLogger() // Opcional: para ver logs de Koin
        modules(
            usuarioModule,
            autentificacionModule,
            progresoDiarioModule,
            aguaModule,suenoModule,
            nutricionModule,
            ejercicioModule,
            )
    }

    configureSecurity()

    install(CORS) {
        anyHost()
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Get)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowNonSimpleContentTypes = true
        allowCredentials = true
    }


    // 2. Configuración de Serialización (Content Negotiation)
    install(ContentNegotiation) {
        json()
    }

    val progresoDiarioController by inject<ProgresoDiarioController>()
    val autentificacionController by inject<AutentificacionController>()
    val usuarioController by inject<UsuarioController>()
    val aguaController by inject<AguaController>()
    val suenoController by inject<SuenoController>()
    val nutricionController by inject<NutricionController>()
    val ejercicioController by inject<EjercicioController>()

    routing {
        usuarioRouting(usuarioController)
        autentificacionRoutes(autentificacionController)
        progresoDiarioRouting(progresoDiarioController)
        aguaRouter(aguaController)
        suenoRouter(suenoController)
        nutricionRouter(nutricionController)
        ejercicioRouter(ejercicioController)
    }
}