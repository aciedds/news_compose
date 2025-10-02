package com.example.news.data.mapper

import com.example.news.data.models.Source
import com.example.news.domain.entity.SourceEntity

fun Source.toSourceEntity() = SourceEntity(
    id = id ?: "",
    category = category ?: "",
    country = country ?: "",
    description = description ?: "",
    language = language ?: "",
    name = name ?: "",
    url = url ?: ""
)