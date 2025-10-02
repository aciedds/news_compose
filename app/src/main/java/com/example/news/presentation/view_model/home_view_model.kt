package com.example.news.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.domain.entity.ArticleEntity
import com.example.news.domain.usecase.NewsUsecase
import com.example.news.state.DataState
import com.example.news.state.ViewState
import com.example.news.utils.NewsCategory
import com.example.news.utils.NewsCountry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val usecase: NewsUsecase
) : ViewModel() {
    val newsCarouselViewState = mutableStateOf<ViewState<List<ArticleEntity>>>(ViewState.Init)

    private val _chips = MutableStateFlow(NewsCategory.entries.map { it.value })
    val chips = _chips.asStateFlow()

    private val _selectedChip = MutableStateFlow(_chips.value.first())
    val selectedChip = _selectedChip.asStateFlow()

    private val _newsRecommendationViewState =
        mutableStateOf<ViewState<List<ArticleEntity>>>(ViewState.Init)
    val newsRecommendationViewState: State<ViewState<List<ArticleEntity>>> =
        _newsRecommendationViewState

    private val _isPaginating = mutableStateOf(false)
    val isPaginating: State<Boolean> = _isPaginating

    private var currentPage = 1
    private var canLoadMore = true
    private var isLoadingMore = false

    init {
        getCarouselNews()
        getRecommendationNews()
    }

    fun onCategorySelected(category: String) {
        if (_selectedChip.value == category) return
        _selectedChip.value = category
        getRecommendationNews()
    }

    fun getRecommendationNews() {
        viewModelScope.launch {
            _newsRecommendationViewState.value = ViewState.Loading
            currentPage = 1
            canLoadMore = true
            val result = usecase.getTopHeadlines(
                country = NewsCountry.UNITED_STATES,
                category = NewsCategory.fromValue(_selectedChip.value),
                pageSize = 10,
                page = currentPage
            )
            when (result) {
                is DataState.Success -> {
                    _newsRecommendationViewState.value = ViewState.Success(result.data.articles)
                }

                is DataState.Error -> {
                    _newsRecommendationViewState.value = ViewState.Error(result.message)
                }
            }
        }
    }

    fun loadMoreRecommendations() {
        if (isLoadingMore || !canLoadMore) return
        viewModelScope.launch {
            isLoadingMore = true
            _isPaginating.value = true

            val currentData =
                (_newsRecommendationViewState.value as? ViewState.Success)?.data ?: emptyList()

            val newArticles = usecase.getTopHeadlines(
                country = NewsCountry.UNITED_STATES,
                category = NewsCategory.fromValue(_selectedChip.value),
                pageSize = 10,
                page = currentPage + 1
            )

            when (newArticles) {
                is DataState.Success -> {
                    val articles = newArticles.data.articles
                    if (articles.isNotEmpty()) {
                        currentPage++
                        val combinedList = currentData + articles
                        _newsRecommendationViewState.value = ViewState.Success(combinedList)
                    } else {
                        canLoadMore = false
                    }
                }

                is DataState.Error -> {
                    canLoadMore = false
                }
            }
            isLoadingMore = false
            _isPaginating.value = false
        }
    }

    fun refreshAllNews() {
        getCarouselNews()
        getRecommendationNews()
    }

    fun getCarouselNews() {
        viewModelScope.launch {
            newsCarouselViewState.value = ViewState.Loading
            val result = usecase.getTopHeadlines(
                country = NewsCountry.UNITED_STATES,
                pageSize = 5,
                page = 1
            )
            when (result) {
                is DataState.Success -> {
                    newsCarouselViewState.value = ViewState.Success(result.data.articles)
                }

                is DataState.Error -> {
                    newsCarouselViewState.value = ViewState.Error(result.message)
                }
            }
        }
    }
}