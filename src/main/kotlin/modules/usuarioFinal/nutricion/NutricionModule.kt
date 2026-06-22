package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.ConfigurarHorariosNutricionUseCase
import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.MarcarComidaUseCase
import com.alilopez.modules.usuarioFinal.nutricion.application.usecase.VerDashboardNutricionUseCase
import com.alilopez.modules.usuarioFinal.nutricion.domain.repository.NutricionRepository
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.persistence.MysqlNutricionRepository
import com.alilopez.modules.usuarioFinal.nutricion.infrastructure.rest.NutricionController
import org.koin.dsl.module

val nutricionModule = module {
    single<NutricionRepository>{ MysqlNutricionRepository() }
    factory { ConfigurarHorariosNutricionUseCase(get()) }
    factory { MarcarComidaUseCase(get()) }
    factory { VerDashboardNutricionUseCase(get(),get()) }
    factory { NutricionController(get(),get(), get()) }
}