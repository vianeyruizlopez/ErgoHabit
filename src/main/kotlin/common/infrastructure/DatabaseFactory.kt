package com.alilopez.common.infrastructure

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable
import com.alilopez.modules.autentificacion.infrastructure.persistence.CodigoVerificacionTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"

            val url = System.getenv("DB_URL")

            if (url != null) {
                jdbcUrl = url
            } else {
                val host = System.getenv("DB_HOST")
                val port = System.getenv("DB_PORT") ?: "3306"
                val name = System.getenv("DB_NAME")

                if (host != null && name != null) {
                    jdbcUrl = "jdbc:mysql://$host:$port/$name?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
                } else {
                    throw IllegalArgumentException("¡ERROR! No se pudo configurar la URL de la base de datos.")
                }
            }

            username = System.getenv("DB_USER")
                ?: throw IllegalArgumentException("¡ERROR! Falta configurar DB_USER.")

            password = System.getenv("DB_PASSWORD")
                ?: throw IllegalArgumentException("¡ERROR! Falta configurar DB_PASSWORD.")

            maximumPoolSize = 10
            connectionTimeout = 30000
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(RolTable, UsuarioTable, CodigoVerificacionTable)
        }
    }
}