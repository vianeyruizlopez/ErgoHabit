package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.application.usecase.ConfigurarMetaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.ObtenerDashboardAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.ObtenerProgresoAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.application.usecase.RegistrarTomaAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.domain.repository.AguaRepository
import com.alilopez.modules.usuarioFinal.agua.infrastructure.persistence.MysqlAguaRepository
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.AguaController
import com.alilopez.modules.usuarioFinal.frases.domain.repository.FrasesRepository
import com.alilopez.modules.usuarioFinal.frases.infrastructure.persistence.MysqlFrasesRepository
import org.koin.dsl.module

val aguaModule = module {
    single<AguaRepository> { MysqlAguaRepository() }
    single<FrasesRepository>{ MysqlFrasesRepository() }
    factory { ObtenerDashboardAguaUseCase(get(),get()) }
    factory { RegistrarTomaAguaUseCase(get()) }
    factory { ConfigurarMetaUseCase(get()) }
    factory { ObtenerProgresoAguaUseCase(get()) }
    factory { AguaController(get(), get(), get(),get()) }
}
