package com.alilopez.modules.autentificacion

import com.alilopez.modules.autentificacion.application.usecase.LoginUseCase
import com.alilopez.modules.autentificacion.application.usecase.RegistrarUseCase
import com.alilopez.modules.autentificacion.application.usecase.RestablecerPasswordUseCase
import com.alilopez.modules.autentificacion.application.usecase.EnviarCodigoUseCase
import com.alilopez.modules.autentificacion.application.usecase.VerificarCodigoUseCase
import com.alilopez.modules.autentificacion.domain.repository.AutentificacionRepository
import com.alilopez.modules.autentificacion.domain.repository.CodigoVerificacionRepository
import com.alilopez.modules.autentificacion.infrastructure.persistence.AutentificacionRepositoryImpl
import com.alilopez.modules.autentificacion.infrastructure.persistence.MysqlCodigoVerificacionRepository
import com.alilopez.modules.autentificacion.infrastructure.rest.AutentificacionController
import org.koin.dsl.module

val autentificacionModule = module {
    single<AutentificacionRepository> { AutentificacionRepositoryImpl() }
    single<CodigoVerificacionRepository> { MysqlCodigoVerificacionRepository() }

    factory { LoginUseCase(get()) }
    factory { RegistrarUseCase(get()) }
    factory { RestablecerPasswordUseCase(get()) }
    factory { EnviarCodigoUseCase(get(), get()) }
    factory { VerificarCodigoUseCase(get(), get()) }
    factory { AutentificacionController(get(), get(), get(), get(), get()) }
}