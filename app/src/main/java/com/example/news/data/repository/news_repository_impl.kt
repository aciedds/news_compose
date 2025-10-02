package com.example.news.data.repository

import com.example.news.data.mapper.toNewsEntity
import com.example.news.data.mapper.toSourceEntity
import com.example.news.data.models.ErrorResponse
import com.example.news.data.sources.NewsApi
import com.example.news.domain.entity.NewsEntity
import com.example.news.domain.entity.SourceEntity
import com.example.news.domain.repository.NewsRepository
import com.example.news.state.DataState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class NewsRepositoryImpl (private val dataSource: NewsApi): NewsRepository {
    override suspend fun getTopHeadlines(
        country: String,
        category: String?,
        sources: String?,
        query: String?,
        pageSize: Int?,
        page: Int?
    ): DataState<NewsEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = dataSource.getTopHeadlines(
                    country = country,
                    category = category,
                    sources = sources,
                    query = query,
                    pageSize = pageSize,
                    page = page
                )
                when{
                    response.status == "ok" -> { DataState.Success(response.toNewsEntity())
                    }
                    else -> {
                        DataState.Error(message = response.status ?: "Unidentified Error")
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val error = Gson().fromJson(
                            e.response()?.errorBody()?.string().orEmpty(),
                            ErrorResponse::class.java,
                        )
                        DataState.Error(message = error?.message ?: e.message())
                    }
                    else -> {
                        DataState.Error(message = e.message ?: "Unidentified Error")
                    }
                }
            }
        }
    }

    override suspend fun getEverything(
        query: String,
        sources: String?,
        domains: String?,
        from: String?,
        to: String?,
        language: String?,
        sortBy: String?,
        pageSize: Int?,
        page: Int?
    ): DataState<NewsEntity> {
        return withContext(Dispatchers.IO) {
            try {
                val response = dataSource.getEverything(
                    query = query,
                    sources = sources,
                    domains = domains,
                    from = from,
                    to = to,
                    language = language,
                    sortBy = sortBy,
                    pageSize = pageSize,
                    page = page
                )

                when{
                    response.status == "ok" -> {
                        DataState.Success(response.toNewsEntity())
                    }
                    else -> {
                        DataState.Error(message = response.status ?: "Unidentified Error")
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val error = Gson().fromJson(
                            e.response()?.errorBody()?.string().orEmpty(),
                            ErrorResponse::class.java,
                        )
                        DataState.Error(message = error?.message ?: e.message())
                    }
                    else -> {
                        DataState.Error(message = e.message ?: "Unidentified Error")
                    }
                }
            }
        }
    }

    override suspend fun getSources(
        category: String?,
        language: String?,
        country: String?
    ): DataState<List<SourceEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = dataSource.getSources(
                    category = category,
                    language = language,
                    country = country
                )
                when{
                    response.status == "ok" -> {
                        DataState.Success(response.sources?.map { it.toSourceEntity() } ?: emptyList())
                    }
                    else -> {
                        DataState.Error(message = response.status ?: "Unidentified Error")
                    }
                }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        val error = Gson().fromJson(
                            e.response()?.errorBody()?.string().orEmpty(),
                            ErrorResponse::class.java,
                        )
                        DataState.Error(message = error?.message ?: e.message())
                    }
                    else -> {
                        DataState.Error(message = e.message ?: "Unidentified Error")
                    }
                }
            }
        }
    }

}