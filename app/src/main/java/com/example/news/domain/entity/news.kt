package com.example.news.domain.entity

data class NewsEntity (
    val articles: List<ArticleEntity>,
    val totalResult : Int
)