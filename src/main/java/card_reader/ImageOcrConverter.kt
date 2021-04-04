package card_reader

import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.vision.v1.*
import com.google.protobuf.ByteString
import common.GOOGLE_KEY
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files

class ImageOcrConverter {

  private val visionClient = ImageAnnotatorClient.create(
      ImageAnnotatorSettings.newBuilder()
          .setCredentialsProvider(
              FixedCredentialsProvider.create(
                  GoogleCredentials.fromStream(FileInputStream(GOOGLE_KEY))
                      .createScoped(listOf("https://www.googleapis.com/auth/cloud-platform"))))
          .build())

  fun read(files: List<File>): List<String> {
    val request = files.map { createRequest(it) }

    return visionClient.batchAnnotateImages(request).responsesList.map { it.fullTextAnnotation.text }
  }

  private fun createRequest(file: File): AnnotateImageRequest {
    val feature =
        Feature.newBuilder()
            .setType(Feature.Type.TEXT_DETECTION)
            .build()

    val image = Image.newBuilder()
        .setContent(ByteString.copyFrom(Files.readAllBytes(file.toPath())))
        .build()

    return AnnotateImageRequest.newBuilder()
        .addFeatures(feature)
        .setImage(image)
        .build()
  }
}