package com.alilopez.modules.autentificacion.domain.repository

import com.alilopez.modules.autentificacion.domain.model.CodigoVerificacion

interface CodigoVerificacionRepository {
    suspend fun guardarCodigo(idUsuario: Int, codigo: String)
    suspend fun obtenerCodigoActivo(idUsuario: Int): CodigoVerificacion?
    suspend fun marcarComoUsado(idCodigo: Int)
    suspend fun eliminarCodigosAnteriores(idUsuario: Int)
}
