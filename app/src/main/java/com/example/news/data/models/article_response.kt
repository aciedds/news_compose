package com.example.news.data.models
import com.google.gson.annotations.SerializedName

data class ArticleResponse (
    @SerializedName("status")
    val status: String?,
    @SerializedName("totalResults")
    val totalResults: Int?,
    @SerializedName("articles")
    val articles: List<Article>?
)