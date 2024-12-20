package org.openmrs.android.fhir

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

object EncryptionHelper {

  private const val AES_TRANSFORMATION = "AES/CBC/PKCS7Padding"

  fun encrypt(data: String, secretKey: SecretKey): Pair<String, ByteArray> {
    val cipher = Cipher.getInstance(AES_TRANSFORMATION)
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val iv = cipher.iv // Save this IV for decryption
    val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
    return Pair(Base64.encodeToString(encryptedBytes, Base64.DEFAULT), iv)
  }

  fun decrypt(encryptedData: String, secretKey: SecretKey, iv: ByteArray): String {
    val cipher = Cipher.getInstance(AES_TRANSFORMATION)
    val ivSpec = IvParameterSpec(iv)
    cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
    val decryptedBytes = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT))
    return String(decryptedBytes, Charsets.UTF_8)
  }
}