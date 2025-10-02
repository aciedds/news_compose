package com.example.news.di.modules

import com.example.news.data.repository.NewsRepositoryImpl
import com.example.news.data.sources.NewsApi
import com.example.news.domain.repository.NewsRepository
import com.example.news.domain.usecase.NewsUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object HomeModules {
    @Provides
    fun provideRepository(newsApi: NewsApi):NewsRepository = NewsRepositoryImpl(newsApi)

    @Provides
    fun provideUsecase(repository: NewsRepository) = NewsUsecase(repository)
}