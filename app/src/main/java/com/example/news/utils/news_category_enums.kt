package com.example.news.utils

enum class NewsCategory(val value: String) {
    BUSINESS("business"),
    ENTERTAINMENT("entertainment"),
    GENERAL("general"),
    HEALTH("health"),
    SCIENCE("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    companion object {
        private val valueMap = entries.associateBy { it.value }
        fun fromValue(value: String?): NewsCategory? = valueMap[value]
    }
}