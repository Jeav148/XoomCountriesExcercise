package com.dev.xoomcountriesexcercise.api.country;

import com.dev.xoomcountriesexcercise.model.CountriesResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ICountriesService {

    @GET("/catalog/v2/countries")
    Call<CountriesResponseDTO> getCountries(@Query("page") int page, @Query("page_size") int pageSize);


}
