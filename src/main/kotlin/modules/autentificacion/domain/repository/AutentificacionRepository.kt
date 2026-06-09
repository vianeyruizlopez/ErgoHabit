package com.alilopez.modules.autentificacion.domain.repository

import com.alilopez.modules.autentificacion.domain.model.Registro

interface AutentificacionRepository {
    suspend fun verPorEmail(email: String): Registro?
    suspend fun registrar(registro: Registro): Registro?
}