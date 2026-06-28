package com.alilopez.modules.usuarios

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import java.io.InputStream

class CloudinaryService {
    private val cloudinary = Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"),
            "api_key", System.getenv("CLOUDINARY_API_KEY"),
            "api_secret", System.getenv("CLOUDINARY_API_SECRET")
        )
    )

    fun uploadImage(inputStream: InputStream): String? {
        return try {
            val options = ObjectUtils.asMap("upload_preset", "incidencias_preset")
            val uploadResult = cloudinary.uploader().upload(inputStream.readBytes(), options)
            uploadResult["secure_url"] as String
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}