package com.alilopez.modules.usuarioFinal.frases.infrastructure.rest.dto

import com.alilopez.modules.usuarioFinal.frasesMotivacionales.domain.model.Frases
import kotlinx.serialization.Serializable

@Serializable
data class FraseResponse(
    val idFrase: Int,
    val categoria: String,
    val texto: String
) {
    companion object {
        fun fromDomain(f: Frases) = FraseResponse(
            idFrase = f.idFrase,
            categoria = f.categoria,
            texto = f.texto
        )
    }
}

@Serializable
data class FraseErrorResponse(
    val code: String,
    val message: String
)