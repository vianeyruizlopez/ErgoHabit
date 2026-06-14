package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.application.usecase.ConfigurarMetaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.ObtenerDashboardAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.RegistrarTomaAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository
import com.alilopez.modules.usuarioFinal.agua.infrastructure.persistence.MysqlAguaRepository
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.AguaController
import org.koin.dsl.module

val aguaModule = module {
    single<AguaRepository> { MysqlAguaRepository() }
    factory { ObtenerDashboardAguaUseCase(get()) }
    factory { RegistrarTomaAguaUseCase(get()) }
    factory { ConfigurarMetaUseCase(get()) }
    factory { AguaController(get(), get(), get()) }
}
