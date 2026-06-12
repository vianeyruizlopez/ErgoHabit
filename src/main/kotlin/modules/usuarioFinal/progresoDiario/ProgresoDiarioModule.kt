package com.alilopez.modules.usuarioTester.habitos.infrastructure.rest

import com.alilopez.modules.usuarioFinal.progresoDiario.application.usecase.VerProgresoDiarioUseCase
import com.alilopez.modules.usuarioFinal.progresoDiario.domain.repository.ProgresoDiarioRepository
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.persistence.MysqlProgresoDiarioRepo
import com.alilopez.modules.usuarioFinal.progresoDiario.infrastructure.rest.ProgresoDiarioController
import org.koin.dsl.module

val progresoDiarioModule = module {
    factory { VerProgresoDiarioUseCase(get(), get())}
    factory { ProgresoDiarioController(get()) }
    single<ProgresoDiarioRepository>{ MysqlProgresoDiarioRepo()}
}
