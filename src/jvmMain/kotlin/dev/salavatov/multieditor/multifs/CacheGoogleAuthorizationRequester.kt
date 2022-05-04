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

class CacheGoogleAuthorizationRequester(val actualAuthorizationRequester: GoogleAuthorizationRequester):
    GoogleAuthorizationRequester {
    private var init = AtomicBoolean(false)

    override suspend fun requestAuthorization(): GoogleAuthTokens {
        if (!init.get()) {
            init.set(true)
            val f = Paths.get(".secret/tokens")
            if (f.exists()) {
                return Json.decodeFromString(f.readText())
            }
        }
        return actualAuthorizationRequester.requestAuthorization().also {
            Paths.get(".secret/tokens").writeText(Json.encodeToString(it))
        }
    }

    override suspend fun refreshAuthorization(expired: GoogleAuthTokens): GoogleAuthTokens {
        return actualAuthorizationRequester.refreshAuthorization(expired).also {
            Paths.get(".secret/tokens").writeText(Json.encodeToString(it.let {
                if (it.refreshToken == null) {
                    return GoogleAuthTokens(it.accessToken, expired.refreshToken)
                }
                it
            }))
        }
    }
}