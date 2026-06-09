plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
}

group = "com.vianeyruiz"
version = "0.0.1"


application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.swagger)
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    // Koin para Ktor
    implementation("io.insert-koin:koin-ktor:3.5.0")

    // Base de Datos
    implementation(libs.bundles.exposed)
    implementation("com.mysql:mysql-connector-j:9.0.0")
    implementation(libs.hikaricp)

    ///claudinary
    implementation("com.cloudinary:cloudinary-http5:2.0.0")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    // Serialization
    implementation(libs.ktor.serialization.kotlinx.json)
    ///token y firebase
    implementation("com.google.firebase:firebase-admin:9.3.0")
    implementation("io.ktor:ktor-server-auth-jwt:2.3.12")
    //para cors
    implementation("io.ktor:ktor-server-cors:2.3.12")
    //encriptar contrasena
    implementation("org.mindrot:jbcrypt:0.4")
}
tasks.shadowJar {
    archiveClassifier.set("all")

    manifest {
        attributes(
            "Main-Class" to "io.ktor.server.netty.EngineMain"
        )
    }

    mergeServiceFiles()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
