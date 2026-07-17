package com.binayshaw7777.dailyroundsassignment.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

object SoundManager {

    fun playSuccess() = playSynthTone(listOf(700f to 120, 950f to 180))

    fun playFailure() = playSynthTone(listOf(330f to 180, 220f to 220))

    private fun playSynthTone(notes: List<Pair<Float, Int>>) {
        Thread {
            val sampleRate = 44100
            for ((freq, durationMs) in notes) {
                val numSamples = sampleRate * durationMs / 1000
                val samples = ShortArray(numSamples)
                for (i in 0 until numSamples) {
                    val angle = 2.0 * PI * i * freq / sampleRate
                    val envelope = when {
                        i < numSamples * 0.05 -> i / (numSamples * 0.05)
                        i > numSamples * 0.75 -> (numSamples - i) / (numSamples * 0.25)
                        else -> 1.0
                    }
                    samples[i] = (sin(angle) * envelope * Short.MAX_VALUE * 0.6).toInt().toShort()
                }
                val bufferSize = samples.size * 2
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
                    .build()
                track.write(samples, 0, samples.size)
                track.play()
                Thread.sleep(durationMs.toLong())
                track.stop()
                track.release()
            }
        }.start()
    }
}
