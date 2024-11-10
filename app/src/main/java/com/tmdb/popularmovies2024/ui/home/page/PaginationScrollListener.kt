package com.tmdb.popularmovies2024.ui.home.page

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tmdb.popularmovies2024.ui.home.QUERY_PAGE_SIZE

class PaginationScrollListener(
    private val onLoadMore: () -> Unit,
    private val isLoading: () -> Boolean,
    private val isLastPage: () -> Boolean
) : RecyclerView.OnScrollListener() {

    private var isScrolling = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        // Verifica si se debe cargar más contenido
        if (shouldPaginate(firstVisibleItemPosition, visibleItemCount, totalItemCount)) {
            onLoadMore()
            isScrolling = false
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) { // Cambiado de SCROLL_STATE_TOUCH_SCROLL a SCROLL_STATE_DRAGGING
            isScrolling = true
        }
    }

    // Función que determina si se debe cargar más contenido
    private fun shouldPaginate(
        firstVisibleItemPosition: Int,
        visibleItemCount: Int,
        totalItemCount: Int
    ): Boolean {
        return !isLoading() &&
                !isLastPage() &&
                firstVisibleItemPosition + visibleItemCount >= totalItemCount &&
                firstVisibleItemPosition >= 0 &&
                totalItemCount >= QUERY_PAGE_SIZE &&
                isScrolling
    }
}
