package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.tarea.application.usecase.*
import com.alilopez.modules.usuarioFinal.tarea.domain.repository.TareaRepository
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.persistence.MysqlTareaRepository
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.TareaController
import com.alilopez.modules.usuarioFinal.tarea.infrastructure.rest.tareaRouter
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.dsl.module
import org.koin.ktor.ext.inject

val tareaModule = module {
        single<TareaRepository> { MysqlTareaRepository() }
    factory { CrearTareaUseCase(get()) }
    factory { ListarTareasUseCase(get()) }
    factory { ObtenerDetalleCronometroUseCase(get()) }
    factory { IniciarCronometroUseCase(get()) }
    factory { CompletarTareaUseCase(get()) }
    factory { EliminarTareaUseCase(get()) }
    factory { ExtenderTareaUseCase(get()) }
    factory { PausarTareaUseCase(get()) }

    factory { TareaController(get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get(),
        get())
    }
}

fun Application.tareaModuleInicio() {
    val controller by inject<TareaController>()
    routing { tareaRouter(controller) }
}