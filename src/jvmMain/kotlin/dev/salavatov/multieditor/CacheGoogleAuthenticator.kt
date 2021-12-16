package dev.salavatov.multieditor

import dev.salavatov.multifs.cloud.googledrive.GoogleAppCredentials
import dev.salavatov.multifs.cloud.googledrive.GoogleAuthTokens
import dev.salavatov.multifs.cloud.googledrive.GoogleAuthenticator
import dev.salavatov.multifs.cloud.googledrive.sampleGoogleAuthenticator
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.math.exp

class CacheGoogleAuthenticator(appCredentials: GoogleAppCredentials) : GoogleAuthenticator {
    private val actualAuthenticator = sampleGoogleAuthenticator(appCredentials)
    private var init = false

    override suspend fun authenticate(): GoogleAuthTokens {
        if (!init) {
            init = true
            val f = Paths.get(".secret/tokens")
            if (f.exists()) {
                return Json.decodeFromString(f.readText())
            }
        }
        return actualAuthenticator.authenticate().also {
            Paths.get(".secret/tokens").writeText(Json.encodeToString(it))
        }
    }

    override suspend fun refresh(expired: GoogleAuthTokens): GoogleAuthTokens {
        return actualAuthenticator.refresh(expired).also {
            Paths.get(".secret/tokens").writeText(Json.encodeToString(it.let {
                if (it.refreshToken == null) {
                    return GoogleAuthTokens(it.accessToken, it.expiresIn, expired.refreshToken)
                }
                it
            }))
        }
    }
}