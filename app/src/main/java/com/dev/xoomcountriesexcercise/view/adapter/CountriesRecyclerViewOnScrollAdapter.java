package com.dev.xoomcountriesexcercise.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.*;

public abstract class CountriesRecyclerViewOnScrollAdapter extends OnScrollListener {

    final int PAGE_SIZE = 40;
    int mCurrentPage;
    int mMaxPage = -1;
    boolean mIsLoading = false;

    private LinearLayoutManager mLayoutManager;

    public CountriesRecyclerViewOnScrollAdapter(LinearLayoutManager layoutManager, int startingPage) {
        this.mLayoutManager = layoutManager;
        this.mCurrentPage = startingPage;
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if(newState == SCROLL_STATE_IDLE){
            int visibleItems = mLayoutManager.getChildCount();
            int totalItems = mLayoutManager.getItemCount();
            int firstVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            if (!isLoading() && !lastPageReached()) {
                if ((visibleItems + firstVisibleItem) >= totalItems
                        && firstVisibleItem >= 0) {
                    loadMoreItems();
                }
            }
        }
    }

    private boolean lastPageReached(){
        if(mMaxPage == -1)return false;
        return getCurrentPage() >= mMaxPage;
    }

    public void setMaxPage(int maxPage){
        this.mMaxPage = maxPage;
    }

    public void setLoading(boolean isLoading){
        this.mIsLoading = isLoading;
    }

    public void setCurrentPage(int currentPage){
        this.mCurrentPage = currentPage;
    }

    public int getCurrentPage(){
        return this.mCurrentPage;
    }

    public int getPageSize(){
        return PAGE_SIZE;
    }

    protected abstract void loadMoreItems();

    public abstract boolean isLoading();
}
