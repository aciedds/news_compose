package com.example.news.data.mapper

import com.example.news.data.models.Article
import com.example.news.domain.entity.ArticleEntity
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun Article.toArticleEntity() : ArticleEntity{
    fun formatTimeAgo(publishedAt: String?): String {
        if (publishedAt == null) return "Unknown time"
        return try {
            val publishedTime = ZonedDateTime.parse(publishedAt)
            val now = ZonedDateTime.now()
            val hours = ChronoUnit.HOURS.between(publishedTime, now)
            when {
                hours < 1 -> "${ChronoUnit.MINUTES.between(publishedTime, now)} minutes ago"
                hours < 24 -> "$hours hours ago"
                else -> publishedTime.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
            }
        } catch (e: Exception) {
            "Unknown time"
        }
    }
    return ArticleEntity(
        author = author ?: "",
        content = content ?: "",
        description = description ?: "",
        publishedAt = formatTimeAgo(this.publishedAt),
        source = source?.toSourceEntity(),
        title = title ?: "",
        url = url ?: "",
        urlToImage = urlToImage ?: ""
    )
}