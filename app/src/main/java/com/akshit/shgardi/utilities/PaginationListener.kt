package com.akshit.shgardi.utilities

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationListener(private val layoutManager: GridLayoutManager) : RecyclerView.OnScrollListener() {
    abstract fun loadMoreItems()
    abstract fun isLastPage(): Boolean
    abstract fun isLoading(): Boolean

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val totalItemCount = layoutManager.itemCount
        val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

        if (!isLoading() && !isLastPage() && lastVisibleItemPosition >= totalItemCount - 4) {
            loadMoreItems()
        }
    }
}