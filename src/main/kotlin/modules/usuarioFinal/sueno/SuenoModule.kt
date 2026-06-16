package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.agua.application.usecase.RegistrarTomaAguaUseCase
import com.alilopez.modules.usuarioFinal.agua.infrastructure.rest.AguaController
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.ConfigurarHorarioSuenoUseCase
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.RegistrarDespertarUseCase
import com.alilopez.modules.usuarioFinal.sueno.application.usecase.VerDashboarSuenoUseCase
import com.alilopez.modules.usuarioFinal.sueno.domain.repository.SuenoRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.persistence.MysqlSuenoRepository
import com.alilopez.modules.usuarioFinal.sueno.infrastructure.rest.SuenoController
import org.koin.dsl.module

val suenoModule=module {
    single <SuenoRepository>{ MysqlSuenoRepository() }
    factory { ConfigurarHorarioSuenoUseCase(get()) }
    factory { RegistrarDespertarUseCase(get()) }
    factory { VerDashboarSuenoUseCase(get()) }
    factory { SuenoController(get(), get(), get()) }
}