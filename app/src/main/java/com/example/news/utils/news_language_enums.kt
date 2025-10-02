package com.example.news.utils

enum class NewsLanguage(val value: String) {
    ARABIC("ar"),
    GERMAN("de"),
    ENGLISH("en"),
    SPANISH("es"),
    FRENCH("fr"),
    ITALIAN("it"),
    JAPANESE("ja"),
    KOREAN("ko"),
    PORTUGUESE("pt"),
    RUSSIAN("ru"),
    CHINESE("zh");

    companion object {
        private val valueMap = entries.associateBy { it.value }
        fun fromValue(value: String?): NewsLanguage? = valueMap[value]
    }
}