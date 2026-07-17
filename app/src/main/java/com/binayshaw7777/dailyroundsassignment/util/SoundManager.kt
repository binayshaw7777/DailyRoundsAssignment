package com.binayshaw7777.dailyroundsassignment.util

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.PI
import kotlin.math.sin

object SoundManager {

    fun playSuccess() = playSynthTone(listOf(700f to 120, 950f to 200))

    fun playFailure() = playSynthTone(listOf(330f to 200, 220f to 250))

    private fun playSynthTone(notes: List<Pair<Float, Int>>) {
        Thread {
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
            track.play()

            // Wait for playback head to reach end before releasing
            while (track.playbackHeadPosition < allSamples.size) {
                Thread.sleep(10)
            }

            track.stop()
            track.release()
        }.start()
    }
}
