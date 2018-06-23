package com.udacity.mregtej.bakingapp.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeApiClient {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    /**
     * Get Retrofit Instance
     */
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static RecipeApiService getApiService() {
        return getRetrofitInstance().create(RecipeApiService.class);
    }

}
