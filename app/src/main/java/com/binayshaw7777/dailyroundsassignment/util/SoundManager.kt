package com.binayshaw7777.dailyroundsassignment.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.math.PI
import kotlin.math.sin

/**
 * Lightweight synthesizer that generates short UI feedback tones without any audio files.
 *
 * Notes are produced as raw PCM sine waves with a simple attack/decay envelope,
 * played through an [AudioTrack] in static mode. The coroutines-friendly API
 * suspends until playback completes.
 *
 * ### Usage
 * ```kotlin
 * // In a ViewModel or coroutine scope
 * SoundManager.playSuccess()   // ascending two-note chime
 * SoundManager.playFailure()   // descending two-note buzz
 * ```
 *
 * @see hapticSuccess
 * @see hapticFailure
 */
object SoundManager {

    /**
     * Plays a descending two-note "failure" tone (700 Hz → 950 Hz).
     *
     * Suspends until playback finishes or is cancelled.
     */
    suspend fun playFailure() = playSynthTone(listOf(700f to 120, 950f to 200))

    /**
     * Plays an ascending two-note "success" tone (330 Hz → 220 Hz).
     *
     * Suspends until playback finishes or is cancelled.
     */
    suspend fun playSuccess() = playSynthTone(listOf(330f to 200, 220f to 250))

    /**
     * Internal helper that synthesizes and plays a sequence of notes.
     *
     * @param notes Ordered list of `(frequencyHz, durationMs)` pairs to concatenate
     *   into a single PCM buffer.
     */
    private suspend fun playSynthTone(notes: List<Pair<Float, Int>>) = withContext(Dispatchers.Default) {
        val sampleRate = 44100

        // Concatenate all note samples into one buffer
        val allSamples = notes.flatMap { (freq, durationMs) ->
            val numSamples = sampleRate * durationMs / 1000
            List(numSamples) { i ->
                val angle = 2.0 * PI * i * freq / sampleRate
                val envelope = when {
                    i < numSamples * 0.05 -> i / (numSamples * 0.05)
                    i > numSamples * 0.75 -> (numSamples - i) / (numSamples * 0.25)
                    else -> 1.0
                }
                (sin(angle) * envelope * Short.MAX_VALUE * 0.6).toInt().toShort()
            }
        }.toShortArray()

        val bufferSize = allSamples.size * 2

        val track = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .build()
            )
            .setBufferSizeInBytes(bufferSize)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()

        track.write(allSamples, 0, allSamples.size)

        suspendCancellableCoroutine<Unit> { continuation ->
            val handler = Handler(Looper.getMainLooper())
            val listener = object : AudioTrack.OnPlaybackPositionUpdateListener {
                override fun onMarkerReached(t: AudioTrack) {
                    t.stop()
                    t.release()
                    if (continuation.isActive) {
                        continuation.resume(Unit)
                    }
                }

                override fun onPeriodicNotification(t: AudioTrack) {}
            }

            track.setPlaybackPositionUpdateListener(listener, handler)
            track.notificationMarkerPosition = allSamples.size

            continuation.invokeOnCancellation {
                track.stop()
                track.release()
            }

            track.play()
        }
    }
}
