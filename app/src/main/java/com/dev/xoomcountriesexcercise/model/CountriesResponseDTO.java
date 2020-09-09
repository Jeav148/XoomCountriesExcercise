package com.dev.xoomcountriesexcercise.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountriesResponseDTO {

    @SerializedName("total_items") private final int totalItems;
    @SerializedName("total_pages") private final int totalPages;
    @SerializedName("items") private final List<CountryDTO> countries;

    public CountriesResponseDTO(int totalItems, int totalPages, List<CountryDTO> countries) {
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.countries = countries;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<CountryDTO> getCountries() {
        return countries;
    }
}
