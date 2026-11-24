package com.wordeep.app.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TextToSpeechHelper(context: Context) {
    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null
    @Volatile
    private var isReady: Boolean = false
    @Volatile
    private var isInitializing: Boolean = false
    private var pendingSpeech: String? = null

    fun warmUp() {
        ensureInitialized()
    }

    private fun ensureInitialized() {
        if (tts != null || isInitializing) return
        isInitializing = true
        try {
            tts = TextToSpeech(appContext) { status ->
                isInitializing = false
                if (status == TextToSpeech.SUCCESS) {
                    tts?.language = Locale.US
                    isReady = true
                    pendingSpeech?.let { text ->
                        pendingSpeech = null
                        speak(text)
                    }
                } else {
                    isReady = false
                }
            }
        } catch (_: Throwable) {
            isInitializing = false
            isReady = false
        }
    }

    fun speak(text: String) {
        val cleaned = text.trim()
        if (cleaned.isEmpty()) return
        if (!isReady) {
            pendingSpeech = cleaned
            ensureInitialized()
            return
        }
        try {
            tts?.speak(cleaned, TextToSpeech.QUEUE_FLUSH, null, "wordig_${System.currentTimeMillis()}")
        } catch (_: Throwable) {
            // Safe no-op if audio service unavailable
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Throwable) {
            // Safe cleanup
        } finally {
            tts = null
            isReady = false
            pendingSpeech = null
        }
    }
}
