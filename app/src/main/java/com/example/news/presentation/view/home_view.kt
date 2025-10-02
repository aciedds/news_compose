package com.example.news.presentation.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.news.domain.entity.ArticleEntity
import com.example.news.presentation.components.BreakingNewsCard
import com.example.news.presentation.components.CategoryChip
import com.example.news.presentation.components.PagerIndicator
import com.example.news.presentation.components.RecommendationCard
import com.example.news.presentation.navigation.Routes
import com.example.news.presentation.view_model.HomeViewModel
import com.example.news.state.ViewState
import java.net.URLEncoder


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val carouselState by viewModel.newsCarouselViewState
    val recommendationState by viewModel.newsRecommendationViewState
    val categories by viewModel.chips.collectAsState()
    val selectedCategory by viewModel.selectedChip.collectAsState()
    val isPaginating by viewModel.isPaginating

    val isRefreshing =
        carouselState is ViewState.Loading && recommendationState is ViewState.Loading
    val lazyListState = rememberLazyListState()

    val handleArticleClick = { article: ArticleEntity ->
        val routes = Routes.NEWS_DETAIL.path
        val url = URLEncoder.encode(article.url, "UTF-8")
        navController.navigate(
            routes
                .replace("{title}", article.title)
                .replace("{url}", url)
        )
    }

    Scaffold(
        topBar = { TopBarSection() }
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { viewModel.refreshAllNews() },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                item {
                    BreakingNewsSection(
                        state = carouselState,
                        onArticleClick = handleArticleClick
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                stickyHeader {
                    CategorySection(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background),
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = viewModel::onCategorySelected
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                recommendationSection(
                    state = recommendationState,
                    isPaginating = isPaginating,
                    onArticleClick = handleArticleClick
                )
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                if (totalItemsCount > 0 && lastVisibleItemIndex >= totalItemsCount - 3) {
                    viewModel.loadMoreRecommendations()
                }
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSection() {
    CenterAlignedTopAppBar(
        title = { Text("NEWS", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BreakingNewsSection(
    state: ViewState<List<ArticleEntity>>,
    onArticleClick: (ArticleEntity) -> Unit // 1. Ganti NavController dengan lambda
) {
    Column {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),) {
            Text(
                text = "Breaking News",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        when (state) {
            is ViewState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ViewState.Success -> {
                val articles = state.data

                if (articles.isEmpty()) {
                    // 2. Tampilkan pesan jika data kosong
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Tidak ada berita utama saat ini.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    val pagerState = rememberPagerState(pageCount = { articles.size })

                    HorizontalPager(
                        state = pagerState,
                        key = { articles[it].url }, // 5. Kunci untuk performa
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        pageSpacing = 16.dp,
                    ) { page ->
                        val article = articles[page]
                        BreakingNewsCard(
                            article = article,
                            modifier = Modifier.clickable { onArticleClick(article) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PagerIndicator(
                        pageCount = articles.size,
                        currentPage = pagerState.currentPage,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            is ViewState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Gagal memuat berita:\n${state.message}",
                        // 3. Gunakan warna dari tema
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }
            }

            is ViewState.Init -> { /* Do nothing */
            }
        }
    }
}

fun LazyListScope.recommendationSection(
    state: ViewState<List<ArticleEntity>>,
    isPaginating: Boolean,
    onArticleClick: (ArticleEntity) -> Unit
) {
    item {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = "Recommendation",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

    when (state) {
        is ViewState.Loading -> {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        is ViewState.Success -> {
            if (state.data.isEmpty()) {
                item {
                    Text(
                        text = "Tidak ada rekomendasi untuk Anda saat ini.",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(state.data, key = { it.url }) { article ->
                    RecommendationCard(
                        article = article,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { onArticleClick(article) }
                    )
                }
            }
        }

        is ViewState.Error -> {
            item {
                Text(
                    text = "Gagal memuat rekomendasi: ${state.message}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        is ViewState.Init -> { /* Do nothing */
        }
    }

//    if (isPaginating) {
//        item {
//            Box(
//                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                CircularProgressIndicator()
//            }
//        }
//    }
}

@Composable
fun CategorySection(
    modifier: Modifier = Modifier,
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                text = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}