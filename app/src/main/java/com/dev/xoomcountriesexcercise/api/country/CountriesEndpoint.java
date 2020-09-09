package com.dev.xoomcountriesexcercise.api.country;

import com.dev.xoomcountriesexcercise.model.CountriesResponseDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.google.gson.FieldNamingPolicy.IDENTITY;

public class CountriesEndpoint {

    private final String COUNTRIES_BASE_URL = "https://mobile.xoom.com";

    public  ICountriesService mCountriesService;

    public enum EndpointResult{
        SUCCESS,
        GENERAL_ERROR,
        NETWORK_ERROR,
        SEVERE_ERROR
    }

    public interface ICountriesRequestResultAsync {
        void processEndpointResponse(CountriesEndpointResult response);
    }

    public CountriesEndpoint() {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(IDENTITY)
                .create();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(COUNTRIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mCountriesService = retrofit.create(ICountriesService.class);
    }

    public void fetchCountriesAsync(ICountriesRequestResultAsync requestInterface, int page, int pageSize){
        mCountriesService.getCountries(page, pageSize).enqueue(new Callback<CountriesResponseDTO>() {
            @Override
            public void onResponse(Call<CountriesResponseDTO> call, Response<CountriesResponseDTO> response) {
                CountriesEndpointResult result;
                if(response.code() != 200){
                    result = new CountriesEndpointResult(null, EndpointResult.NETWORK_ERROR);
                    requestInterface.processEndpointResponse(result);
                    return;
                }
                if(!response.isSuccessful() || response.body() == null){
                    result = new CountriesEndpointResult(null, EndpointResult.GENERAL_ERROR);
                    requestInterface.processEndpointResponse(result);
                    return;
                }
                result = new CountriesEndpointResult(response.body(), EndpointResult.SUCCESS);
                requestInterface.processEndpointResponse(result);
            }

            @Override
            public void onFailure(Call<CountriesResponseDTO> call, Throwable t) {
                CountriesEndpointResult result = new CountriesEndpointResult(null, EndpointResult.SEVERE_ERROR);
                requestInterface.processEndpointResponse(result);
            }
        });
    }

    public static class CountriesEndpointResult {
        private CountriesResponseDTO response;
        private EndpointResult result;

        public CountriesEndpointResult(CountriesResponseDTO response, EndpointResult result) {
            this.response = response;
            this.result = result;
        }

        public CountriesResponseDTO getResponse() {
            return response;
        }

        public void setResponse(CountriesResponseDTO response) {
            this.response = response;
        }

        public EndpointResult getResult() {
            return result;
        }

        public void setResult(EndpointResult result) {
            this.result = result;
        }
    }

}
