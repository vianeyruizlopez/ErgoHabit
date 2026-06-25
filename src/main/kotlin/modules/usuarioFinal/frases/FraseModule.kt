package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.frases.application.usecase.ObtenerFraseAleatoriaUseCase
import com.alilopez.modules.usuarioFinal.frases.domain.repository.FrasesRepository
import com.alilopez.modules.usuarioFinal.frases.infrastructure.persistence.MysqlFrasesRepository
import com.alilopez.modules.usuarioFinal.frases.infrastructure.rest.FrasesController
import com.alilopez.modules.usuarioFinal.frases.infrastructure.rest.frasesRouter
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.dsl.module
import org.koin.ktor.ext.inject

val fraseModule = module {
    single<FrasesRepository>{ MysqlFrasesRepository() }
    factory { ObtenerFraseAleatoriaUseCase(get()) }
    factory { FrasesController(get()) }
}
fun Application.fraseModuleInicio(){
    val controller by inject<FrasesController>()
    routing {frasesRouter(controller)}
}