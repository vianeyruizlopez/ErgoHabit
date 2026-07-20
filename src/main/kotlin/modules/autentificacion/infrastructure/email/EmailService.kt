package com.alilopez.modules.autentificacion.infrastructure.email

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.add

object EmailService {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun enviarCodigo(destinatario: String, codigo: String): Boolean {
        val apiKey   = System.getenv("RESEND_API_KEY")
            ?: throw IllegalStateException("Falta RESEND_API_KEY en el entorno.")
        val remitente = System.getenv("RESEND_FROM_EMAIL")
            ?: "onboarding@resend.dev"

        val cuerpoHtml = """
            <div style="font-family: Arial, sans-serif; max-width: 400px; margin: auto;">
              <h2 style="color: #4A90E2;">ErgoHabit — Verificación de acceso</h2>
              <p>Tu código de verificación es:</p>
              <h1 style="letter-spacing: 8px; color: #333;">$codigo</h1>
              <p style="color: #888; font-size: 13px;">
                Este código expira en <strong>10 minutos</strong>.<br>
                Si no intentaste iniciar sesión, ignora este correo.
              </p>
            </div>
        """.trimIndent()

        return try {
            val respuesta: HttpResponse = client.post("https://api.resend.com/emails") {
                header(HttpHeaders.Authorization, "Bearer $apiKey")
                contentType(ContentType.Application.Json)

                val jsonBody = kotlinx.serialization.json.buildJsonObject {
                    put("from", remitente)
                    putJsonArray("to") {
                        add(destinatario)
                    }
                    put("subject", "Tu código de verificación - ErgoHabit")
                    put("html", cuerpoHtml)
                }

                setBody(jsonBody)
            }
            respuesta.status.value in 200..201
        } catch (e: Exception) {
            println("Error al enviar correo: ${e.message}")
            false
        }
    }
}
