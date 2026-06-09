package com.alilopez.common.infrastructure

import com.alilopez.modules.usuarios.infrastructure.persistence.UsuarioTable
import com.alilopez.modules.catalogosRol.infrastructure.persistence.RolTable

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val dotenv = dotenv {
            ignoreIfMissing = true
        }
        val config = HikariConfig().apply {
            driverClassName = "com.mysql.cj.jdbc.Driver"

            jdbcUrl = dotenv["DB_URL"] ?: System.getenv("DB_URL")
            username = dotenv["DB_USER"] ?: System.getenv("DB_USER")
            password = dotenv["DB_PASSWORD"] ?: System.getenv("DB_PASSWORD")
            maximumPoolSize = 10
            if (jdbcUrl == null) {
                throw IllegalArgumentException("¡ERROR! No se encontró la URL de la base de datos en el .env")
            }
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            SchemaUtils.create(
                RolTable,
                UsuarioTable
            )
        }
    }
}