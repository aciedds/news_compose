package com.example.news.data.mapper

import com.example.news.data.models.ArticleResponse
import com.example.news.domain.entity.NewsEntity

fun ArticleResponse.toNewsEntity() = NewsEntity(
    articles = articles?.map { it.toArticleEntity() } ?: emptyList(),
    totalResult = totalResults ?: 0
)