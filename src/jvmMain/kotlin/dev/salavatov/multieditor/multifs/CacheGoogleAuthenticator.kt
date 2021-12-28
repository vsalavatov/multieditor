package dev.salavatov.multieditor.multifs

import dev.salavatov.multifs.cloud.googledrive.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.file.Paths
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class CacheGoogleAuthenticator(val actualAuthenticator: GoogleAuthenticator) : GoogleAuthenticator {
    private var init = AtomicBoolean(false)

    override suspend fun authenticate(): GoogleAuthTokens {
        if (!init.get()) {
            init.set(true)
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