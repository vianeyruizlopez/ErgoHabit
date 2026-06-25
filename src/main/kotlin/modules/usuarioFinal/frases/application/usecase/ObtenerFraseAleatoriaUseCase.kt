package com.alilopez.modules.usuarioFinal.frases.application.usecase

import com.alilopez.modules.usuarioFinal.frases.domain.repository.FrasesRepository
import com.alilopez.modules.usuarioFinal.frasesMotivacionales.domain.model.Frases

class ObtenerFraseAleatoriaUseCase(
    private val repository: FrasesRepository
) {
    fun execute(categoria: String?): Frases {
        if (!categoria.isNullOrBlank()) {
            return repository.obtenerFraseAleatoriaPorCategoria(categoria.trim().uppercase())
                ?: throw NoSuchElementException("No se encontraron frases para la categoría: $categoria")
        }
        val categoriasPorDefecto = listOf("TAREA_EXITO", "SUENO", "NUTRICION", "ERGONOMIA")

        val todasLasFrases = categoriasPorDefecto.flatMap { cat ->
            repository.obtenerTodasPorCategoria(cat)
        }

        if (todasLasFrases.isNotEmpty()) {
            return todasLasFrases.shuffled().first()
        }

        throw NoSuchElementException("No hay ninguna frase disponible en el sistema.")
    }
}