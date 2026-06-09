package com.alilopez.modules.usuarios

import com.alilopez.modules.usuarios.application.usecase.ActualizarUseCase
import com.alilopez.modules.usuarios.application.usecase.EliminarUseCase
import com.alilopez.modules.usuarios.application.usecase.VerPerfilUseCase
import com.alilopez.modules.usuarios.application.usecase.VerTodoUseCase
import com.alilopez.modules.usuarios.domain.repository.UsuarioRepository
import com.alilopez.modules.usuarios.infrastructure.persistence.MysqlUsuarioRepository
import com.alilopez.modules.usuarios.infrastructure.rest.UsuarioController
import org.koin.dsl.module

val usuarioModule = module {
    factory { ActualizarUseCase(get())}
    factory { EliminarUseCase(get()) }
    factory { VerPerfilUseCase(get()) }
    factory { VerTodoUseCase(get()) }
    factory { UsuarioController(get(), get(), get(), get()) }
    single<UsuarioRepository> { MysqlUsuarioRepository() }
}