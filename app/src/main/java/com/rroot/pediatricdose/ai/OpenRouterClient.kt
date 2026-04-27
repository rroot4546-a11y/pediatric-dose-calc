package com.rroot.pediatricdose.ai

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Minimal OpenRouter (https://openrouter.ai) chat-completion client.
 *
 * Uses the OpenAI-compatible /chat/completions endpoint. We keep this hand-
 * rolled so the app does not pull in a large SDK and to make it obvious
 * exactly what is sent over the wire (the token, the messages array, and
 * the chosen model — nothing else).
 */
class OpenRouterClient(
    private val tokenProvider: () -> String?,
    private val modelProvider: () -> String,
) {

    private val http: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    /**
     * Send a chat conversation and return the assistant reply text.
     * Throws on network errors or non-2xx responses.
     */
    suspend fun chat(messages: List<ChatMessage>): String = withContext(Dispatchers.IO) {
        val token = tokenProvider()?.takeIf { it.isNotBlank() }
            ?: throw OpenRouterException("OpenRouter token is not set. Open Settings to add one.")

        val body = JSONObject().apply {
            put("model", modelProvider())
            put("messages", JSONArray().apply {
                for (m in messages) {
                    put(JSONObject().apply {
                        put("role", m.role)
                        put("content", m.content)
                    })
                }
            })
            put("temperature", 0.2)
        }

        val request = Request.Builder()
            .url(ENDPOINT)
            .addHeader("Authorization", "Bearer $token")
            .addHeader("HTTP-Referer", "https://github.com/rroot4546-a11y/pediatric-dose-calc")
            .addHeader("X-Title", "PediCalc AI")
            .post(body.toString().toRequestBody(JSON))
            .build()

        http.newCall(request).execute().use { resp ->
            val raw = resp.body?.string().orEmpty()
            if (!resp.isSuccessful) {
                throw OpenRouterException(
                    "OpenRouter ${resp.code}: ${friendly(raw, resp.code)}"
                )
            }
            val json = JSONObject(raw)
            val choice = json.optJSONArray("choices")?.optJSONObject(0)
                ?: throw OpenRouterException("Empty response from OpenRouter.")
            val message = choice.optJSONObject("message")
                ?: throw OpenRouterException("Malformed response from OpenRouter.")
            message.optString("content").ifBlank {
                throw OpenRouterException("Model returned an empty message.")
            }
        }
    }

    private fun friendly(raw: String, code: Int): String {
        val msg = try {
            JSONObject(raw).optJSONObject("error")?.optString("message").orEmpty()
        } catch (_: Throwable) {
            ""
        }
        return when {
            msg.isNotBlank() -> msg
            code == 401 -> "invalid token (401). Check your OpenRouter API key."
            code == 402 -> "out of credit (402). Top up at openrouter.ai."
            code == 429 -> "rate limited (429). Wait a moment and try again."
            else -> raw.take(200)
        }
    }

    companion object {
        private const val ENDPOINT = "https://openrouter.ai/api/v1/chat/completions"
        private val JSON = "application/json; charset=utf-8".toMediaType()
    }
}

class OpenRouterException(message: String) : RuntimeException(message)

data class ChatMessage(
    val role: String, // "system" | "user" | "assistant"
    val content: String,
)
