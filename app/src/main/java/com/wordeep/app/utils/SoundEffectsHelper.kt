package com.wordeep.app.utils

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import java.util.concurrent.Executors
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

class SoundEffectsHelper {
    private val executor = Executors.newSingleThreadExecutor()

    private data class ToneNote(
        val freqHz: Double,
        val durationMs: Int,
        val volume: Double = 0.22
    )

    fun playTileClick(stepIndex: Int = 1, isDeselect: Boolean = false) {
        val freq = if (isDeselect) {
            310.0
        } else {
            val scale = doubleArrayOf(1.0, 1.122, 1.26, 1.498, 1.682, 2.0, 2.245, 2.52)
            440.0 * scale[(stepIndex - 1).coerceIn(0, scale.lastIndex)]
        }
        playNotesAsync(listOf(ToneNote(freqHz = freq, durationMs = 55, volume = 0.14)))
    }

    fun playWordMatch() {
        playNotesAsync(
            listOf(
                ToneNote(freqHz = 523.25, durationMs = 80, volume = 0.20),
                ToneNote(freqHz = 659.25, durationMs = 85, volume = 0.22),
                ToneNote(freqHz = 783.99, durationMs = 130, volume = 0.20)
            )
        )
    }

    fun playExtraWord(isDuplicate: Boolean = false) {
        if (isDuplicate) {
            playNotesAsync(
                listOf(
                    ToneNote(freqHz = 360.0, durationMs = 55, volume = 0.14),
                    ToneNote(freqHz = 330.0, durationMs = 65, volume = 0.12)
                )
            )
            return
        }
        playNotesAsync(
            listOf(
                ToneNote(freqHz = 587.33, durationMs = 65, volume = 0.18),
                ToneNote(freqHz = 739.99, durationMs = 65, volume = 0.18),
                ToneNote(freqHz = 880.0, durationMs = 75, volume = 0.20),
                ToneNote(freqHz = 1174.66, durationMs = 140, volume = 0.18)
            )
        )
    }

    private fun playNotesAsync(notes: List<ToneNote>) {
        if (executor.isShutdown) return
        executor.execute {
            try {
                val sampleRate = 22050
                val samplesPerNote = notes.map { (sampleRate * it.durationMs) / 1000 }
                val totalSamples = samplesPerNote.sum().coerceAtLeast(1)
                val pcm = ShortArray(totalSamples)

                var offset = 0
                notes.forEachIndexed { noteIdx, note ->
                    val count = samplesPerNote[noteIdx]
                    for (i in 0 until count) {
                        val t = i.toDouble() / sampleRate
                        val attack = (i / (sampleRate * 0.008)).coerceAtMost(1.0)
                        val decay = exp(-4.5 * i / count.coerceAtLeast(1))
                        val wave = sin(2.0 * PI * note.freqHz * t)
                        val sample = (wave * attack * decay * note.volume * Short.MAX_VALUE)
                            .toInt()
                            .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                            .toShort()
                        pcm[offset + i] = sample
                    }
                    offset += count
                }

                val bufferSize = pcm.size * 2
                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_GAME)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(pcm, 0, pcm.size)
                track.play()
                val sleepMs = notes.sumOf { it.durationMs.toLong() } + 40L
                Thread.sleep(sleepMs)
                track.stop()
                track.release()
            } catch (_: Throwable) {
                // Safe no-op on environments without audio output
            }
        }
    }

    fun shutdown() {
        try {
            executor.shutdownNow()
        } catch (_: Throwable) {
            // Safe cleanup
        }
    }
}
