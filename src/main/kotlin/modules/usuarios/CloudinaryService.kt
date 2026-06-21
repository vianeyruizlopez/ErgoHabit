package com.alilopez.modules.usuarios

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import io.github.cdimascio.dotenv.dotenv
import java.io.InputStream

class CloudinaryService {
    private val env = dotenv()

    private val cloudinary = Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", env["CLOUDINARY_CLOUD_NAME"],
            "api_key", env["CLOUDINARY_API_KEY"],
            "api_secret", env["CLOUDINARY_API_SECRET"]
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