package com.alilopez.modules.usuarios.domain.model

import java.io.InputStream
interface ImageStorageService {
    fun subirFotoPerfil(inputStream: InputStream): String?
}