package com.example.news.di

import ApiService
import com.example.news.data.sources.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Injection {
    @Singleton
    @Provides
    fun  provideApi(): NewsApi = ApiService.create()

}