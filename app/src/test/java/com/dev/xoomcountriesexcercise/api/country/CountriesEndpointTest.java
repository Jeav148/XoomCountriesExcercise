package com.dev.xoomcountriesexcercise.api.country;

import com.dev.xoomcountriesexcercise.model.CountriesResponseDTO;
import com.dev.xoomcountriesexcercise.model.CountryDTO;
import com.google.gson.Gson;

import static com.dev.xoomcountriesexcercise.api.country.CountriesEndpoint.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class CountriesEndpointTest {

    // region constants ----------------------------------------------------------------------------
    final static int TOTAL_PAGES = 24;
    final static int TOTAL_ITEMS = 232;
    final static int CURRENT_PAGE = 1;
    final static int PAGE_SIZE = 10;
    final static List<CountryDTO> COUNTRIES_DTO = new ArrayList<>(); //No items needed for testing, since rest result should be tested by API tests.
    public static CountriesEndpoint.EndpointResult mResult;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Captor ArgumentCaptor<CountriesEndpointResult> mCountriesCaptor;
    @Mock ICountriesRequestResultAsync countriesRequestResultAsync;
    // endregion helper fields ---------------------------------------------------------------------

    CountriesEndpoint TestEntity;

    @Before
    public void setup() throws Exception {
        prepareSuccess();
        TestEntity = new EndpointTd();
    }

    // region test cases
    @Test
    public void fetchCountries_success_responseSuccessful() throws Exception {
        //Arrange
        prepareSuccess();
        //Act
        TestEntity.fetchCountriesAsync(countriesRequestResultAsync, CURRENT_PAGE, PAGE_SIZE);
        //Assert
        verify(countriesRequestResultAsync).processEndpointResponse(mCountriesCaptor.capture());
        assertThat(mCountriesCaptor.getValue().getResult(), is(EndpointResult.SUCCESS));
        assertThat(mCountriesCaptor.getValue().getResponse().getTotalItems(), is(PAGE_SIZE));
        assertThat(mCountriesCaptor.getValue().getResponse().getTotalPages(), is(TOTAL_PAGES));
        assertThat(mCountriesCaptor.getValue().getResponse().getCountries(), is(COUNTRIES_DTO));
    }


    @Test
    public void fetchCountries_generalError_responseGeneralError() throws Exception {
        //Arrange
        prepareGeneralError();
        //Act
        TestEntity.fetchCountriesAsync(countriesRequestResultAsync, CURRENT_PAGE, PAGE_SIZE);
        //Assert
        verify(countriesRequestResultAsync).processEndpointResponse(mCountriesCaptor.capture());
        assertThat(mCountriesCaptor.getValue().getResult(), is(EndpointResult.GENERAL_ERROR));
        assertThat(mCountriesCaptor.getValue().getResponse(), is(nullValue()));
    }

    @Test
    public void fetchCountries_networkError_responseNetworkError() throws Exception {
        //Arrange
        prepareNetworkError();
        //Act
        TestEntity.fetchCountriesAsync(countriesRequestResultAsync, CURRENT_PAGE, PAGE_SIZE);
        //Assert
        verify(countriesRequestResultAsync).processEndpointResponse(mCountriesCaptor.capture());
        assertThat(mCountriesCaptor.getValue().getResult(), is(EndpointResult.NETWORK_ERROR));
        assertThat(mCountriesCaptor.getValue().getResponse(), is(nullValue()));
    }

    @Test
    public void fetchCountries_severeError_responseSevereError() throws Exception {
        //Arrange
        prepareSevereError();
        //Act
        TestEntity.fetchCountriesAsync(countriesRequestResultAsync, CURRENT_PAGE, PAGE_SIZE);
        //Assert
        verify(countriesRequestResultAsync).processEndpointResponse(mCountriesCaptor.capture());
        assertThat(mCountriesCaptor.getValue().getResult(), is(EndpointResult.SEVERE_ERROR));
        assertThat(mCountriesCaptor.getValue().getResponse(), is(nullValue()));
    }
    // endregion test cases

    // region helper methods -----------------------------------------------------------------------
    private void prepareSuccess(){
        mResult = EndpointResult.SUCCESS;
    }
    private void prepareGeneralError(){
        mResult = EndpointResult.GENERAL_ERROR;
    }
    private void prepareNetworkError(){
        mResult = EndpointResult.NETWORK_ERROR;
    }
    private void prepareSevereError(){
        mResult = EndpointResult.SEVERE_ERROR;
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class EndpointTd extends CountriesEndpoint {
        public EndpointTd() {
            super();
        }

        @Override
        public void fetchCountriesAsync(ICountriesRequestResultAsync requestInterface, int page, int pageSize) {
            CountriesEndpointResult mCountriesEndpointResultTD;
            if (mResult == EndpointResult.GENERAL_ERROR) {
                mCountriesEndpointResultTD = new CountriesEndpointResult(null, EndpointResult.GENERAL_ERROR);
                requestInterface.processEndpointResponse(mCountriesEndpointResultTD);
            } else if (mResult == EndpointResult.SEVERE_ERROR) {
                mCountriesEndpointResultTD = new CountriesEndpointResult(null, EndpointResult.SEVERE_ERROR);
                requestInterface.processEndpointResponse(mCountriesEndpointResultTD);
            } else if (mResult == EndpointResult.NETWORK_ERROR) {
                mCountriesEndpointResultTD = new CountriesEndpointResult(null, EndpointResult.NETWORK_ERROR);
                requestInterface.processEndpointResponse(mCountriesEndpointResultTD);
            } else if (mResult == EndpointResult.SUCCESS) {
                CountriesResponseDTO countriesDTO = new CountriesResponseDTO(PAGE_SIZE, TOTAL_PAGES, COUNTRIES_DTO);
                mCountriesEndpointResultTD = new CountriesEndpointResult(countriesDTO, EndpointResult.SUCCESS);
                requestInterface.processEndpointResponse(mCountriesEndpointResultTD);
            }
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}