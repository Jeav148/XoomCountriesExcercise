package com.dev.xoomcountriesexcercise.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dev.xoomcountriesexcercise.R;
import com.dev.xoomcountriesexcercise.api.country.CountriesEndpoint;
import com.dev.xoomcountriesexcercise.model.CountriesResponseDTO;
import com.dev.xoomcountriesexcercise.view.adapter.CountriesRecyclerViewAdapter;
import com.dev.xoomcountriesexcercise.view.adapter.CountriesRecyclerViewOnScrollAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dev.xoomcountriesexcercise.api.country.CountriesEndpoint.*;

public class CountriesActivity extends AppCompatActivity implements ICountriesRequestResultAsync {
    @BindView(R.id.countries_recycler_view_countries) RecyclerView recyclerView;
    @BindView(R.id.countries_retry_container) LinearLayout retrySection;

    private CountriesRecyclerViewAdapter mCountriesRecyclerViewAdapter;
    private CountriesRecyclerViewOnScrollAdapter mCountriesRecyclerViewPagingAdapter;
    private CountriesEndpoint mCountriesEndpoint;
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        ButterKnife.bind(this);
        initializeView(1);
    }

    private void initializeView(int startingPage){
        ICountriesRequestResultAsync asyncResult = this;
        mCountriesEndpoint = new CountriesEndpoint();
        mCountriesRecyclerViewAdapter = new CountriesRecyclerViewAdapter(CountriesActivity.this, FlagType.SHINY, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(CountriesActivity.this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(false);
        RecyclerView.LayoutManager mLayoutManager = layoutManager;
        mCountriesRecyclerViewPagingAdapter = new CountriesRecyclerViewOnScrollAdapter(layoutManager, startingPage) {
            @Override
            protected void loadMoreItems() {
                setLoading(true);
                setCurrentPage(getCurrentPage() + 1);
                mCountriesEndpoint.fetchCountriesAsync(asyncResult, getCurrentPage(), getPageSize());
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }
        };
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mCountriesRecyclerViewAdapter);
        recyclerView.addOnScrollListener(mCountriesRecyclerViewPagingAdapter);
        mCountriesEndpoint.fetchCountriesAsync(asyncResult, startingPage, mCountriesRecyclerViewPagingAdapter.getPageSize());
    }

    public void retry(View button){
        mCountriesEndpoint.fetchCountriesAsync(this, mCountriesRecyclerViewPagingAdapter.getCurrentPage(), mCountriesRecyclerViewPagingAdapter.getPageSize());
    }

    @Override
    public void processEndpointResponse(CountriesEndpointResult response) {
        switch (response.getResult()) {
            case GENERAL_ERROR:
            case NETWORK_ERROR:
            case SEVERE_ERROR:
                Toast.makeText(this, getString(R.string.rest_error), Toast.LENGTH_SHORT).show();
                displayRefresh();
                break;
            case SUCCESS:
                populateRecyclerView(response.getResponse());
                break;
        }
    }

    public void populateRecyclerView(CountriesResponseDTO countriesResponseDTO){
        mCountriesRecyclerViewPagingAdapter.setMaxPage(countriesResponseDTO.getTotalPages());
        mCountriesRecyclerViewAdapter.addCountriesToDataSet(countriesResponseDTO.getCountries());
        retrySection.setVisibility(View.GONE);
    }

    public void displayRefresh(){
        retrySection.setVisibility(View.VISIBLE);
    }

    public enum FlagType{
        SHINY("shiny"),
        FLAT("flat");

        String type;

        FlagType(String type) {
            this.type = type;
        }

        @NonNull
        @Override
        public String toString() {
            return this.type;
        }
    }
}