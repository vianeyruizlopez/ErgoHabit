package com.alilopez.modules.usuarioFinal.frases.domain.repository

import com.alilopez.modules.usuarioFinal.frasesMotivacionales.domain.model.Frases

interface FrasesRepository {
    fun obtenerTodasPorCategoria(categoria: String): List<Frases>
    fun obtenerFraseAleatoriaPorCategoria(categoria: String): Frases?
}