package com.example.news.domain.repository
import com.example.news.domain.entity.NewsEntity
import com.example.news.domain.entity.SourceEntity
import com.example.news.state.DataState

interface NewsRepository {
    suspend fun getTopHeadlines(
        country: String,
        category: String? = null,
        sources: String? = null,
        query: String? = null,
        pageSize: Int? = null,
        page: Int? = null
    ): DataState<NewsEntity>

    suspend fun getEverything(
        query: String,
        sources: String? = null,
        domains: String? = null,
        from: String? = null,
        to: String? = null,
        language: String? = null,
        sortBy: String? = null,
        pageSize: Int? = null,
        page: Int? = null
    ) : DataState<NewsEntity>

    suspend fun getSources(
        category: String? = null,
        language: String? = null,
        country: String? = null
    ): DataState<List<SourceEntity>>
}