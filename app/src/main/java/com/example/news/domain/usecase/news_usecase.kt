package com.example.news.domain.usecase

import com.example.news.domain.entity.NewsEntity
import com.example.news.domain.entity.SourceEntity
import com.example.news.domain.repository.NewsRepository
import com.example.news.state.DataState
import com.example.news.utils.NewsCategory
import com.example.news.utils.NewsCountry
import com.example.news.utils.NewsLanguage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class NewsUsecase(private val repository: NewsRepository) {
    suspend fun getTopHeadlines(
        country: NewsCountry = NewsCountry.INDONESIA,
        category: NewsCategory? = null,
        sources: String? = null,
        query: String? = null,
        pageSize: Int = 10,
        page: Int = 1,
    ) : DataState<NewsEntity> {
        return repository.getTopHeadlines(
            country = country.value,
            category = category?.value,
            sources = sources,
            query = query,
            pageSize = pageSize,
            page = page
        )
    }

    suspend fun getEverything(
        query: String,
        sources: String? = null,
        domains: String? = null,
        from: LocalDateTime? = null,
        to: LocalDateTime? = null,
        language: NewsLanguage? = null,
        sortBy: String? = null,
        pageSize: Int = 10,
        page: Int = 1
    ) : DataState<NewsEntity> {
        val formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm", Locale("id", "ID"))
        val formattedDate = from?.format(formatter)
        val formattedToDate = to?.format(formatter)
        return repository.getEverything(
            query = query,
            sources = sources,
            domains = domains,
            from = formattedDate,
            to = formattedToDate,
            language = language?.value,
            sortBy = sortBy,
            pageSize = pageSize,
            page = page
        )
    }

    suspend fun getSources(
        category: NewsCategory? = null,
        language: NewsLanguage? = null,
        country: NewsCountry? = null
    ) : DataState<List<SourceEntity>>
    {
        return repository.getSources(
            category = category?.value,
            language = language?.value,
            country = country?.value
        )
    }
}