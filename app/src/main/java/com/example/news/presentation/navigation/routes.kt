package com.example.news.presentation.navigation

import com.example.news.domain.entity.SourceEntity

enum class Routes(val path: String) {
    HOMESCREEN("home"),
    NEWS_DETAIL ("news_detail/{title}/{url}")
}