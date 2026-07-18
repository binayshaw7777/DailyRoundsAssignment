package com.binayshaw7777.dailyroundsassignment.data.repository

import com.binayshaw7777.dailyroundsassignment.domain.repository.QuizRepository
import com.binayshaw7777.dailyroundsassignment.data.model.Question
import com.binayshaw7777.dailyroundsassignment.data.remote.QuizApiService
import com.binayshaw7777.dailyroundsassignment.data.remote.dto.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Remote implementation of [QuizRepository] that fetches questions from the
 * network via [QuizApiService].
 *
 * @param apiService The Ktor-based HTTP service that performs the actual request.
 */
class RemoteQuizRepositoryImpl @Inject constructor(private val apiService: QuizApiService) : QuizRepository {

    /**
     * Fetches questions from the remote gist and maps each DTO to the domain model.
     *
     * Runs on [Dispatchers.IO].
     *
     * @return [Result.success] with the mapped [Question] list, or
     *   [Result.failure] with the network/deserialization exception.
     */
    override suspend fun loadQuestions(): Result<List<Question>> = withContext(Dispatchers.IO) {
        Timber.d("loadQuestions() — fetching from REMOTE")
        val startTime = System.currentTimeMillis()

        runCatching {
            val dtos = apiService.fetchQuestions()
            Timber.d("Received %d DTOs from API, mapping to domain...", dtos.size)
            dtos.map { it.toDomain() }
        }.onSuccess {
            Timber.d("loadQuestions() SUCCESS: %d questions in %dms", it.size, System.currentTimeMillis() - startTime)
        }.onFailure {
            Timber.e(it, "loadQuestions() FAILED after %dms", System.currentTimeMillis() - startTime)
        }
    }
}
