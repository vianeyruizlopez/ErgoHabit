package com.alilopez.modules.autentificacion.infrastructure.rest

import com.alilopez.modules.autentificacion.application.usecase.LoginUseCase
import com.alilopez.modules.autentificacion.application.usecase.RegistrarUseCase
import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import com.alilopez.modules.autentificacion.infrastructure.persistence.AutentificacionRepositoryImpl
import org.koin.dsl.module

val autentificacionModule = module {
    single<AutentificacionRepository> { AutentificacionRepositoryImpl() }

    factory { LoginUseCase(get()) }
    factory { RegistrarUseCase(get()) }
    factory { AutentificacionController(get(), get()) }
}