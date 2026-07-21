package com.alilopez.modules.administrador.reportes.infrastructure.rest

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.routing.*

fun Route.adminReportesRouter(controller: AdminReportesController) {
    authenticate("auth-jwt") {
        route("/api/v1/admin/reportes") {

            get("/postura") {
                val idRol = call.principal<JWTPrincipal>()
                    ?.payload?.getClaim("idRol")?.asInt() ?: 0
                controller.obtenerReportePostura(call, idRol)
            }
        }
    }
}
