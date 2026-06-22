package com.alilopez.modules.usuarioFinal.ergonomia.infrestructure.rest.dto
import com.alilopez.modules.usuarioFinal.ergonomia.domain.model.Postura
import kotlinx.serialization.Serializable
@Serializable
data class PosturaResponse(
    val idHistorial: Int?,
    val idUsuario: Int,
    val fecha: String,
    val totalAlertas: Int
)

fun Postura.toResponse() = PosturaResponse(
    idHistorial = this.idHistorial,
    idUsuario = this.idUsuario,
    fecha = this.fecha.toString(),
    totalAlertas = this.totalAlertas
)

@Serializable
data class ElementoGraficaPostura(
    val diaSemana: String,
    val totalAlertas: Int,
    val esHoy: Boolean
)

@Serializable
data class ProgresoPosturaResponse(
    val mensajeMeta: String,
    val datosGrafica: List<ElementoGraficaPostura>
)