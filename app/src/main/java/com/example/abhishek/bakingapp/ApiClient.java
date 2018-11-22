package com.example.abhishek.bakingapp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static Retrofit retrofit;
    private static ApiClientService apiClientService;

   public static Retrofit getRetrofit() {
        if (retrofit == null) {
            Retrofit.Builder builder;
            builder = new Retrofit.Builder()
                    .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                    .addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.build();
        }

        return retrofit;

    }

    public static ApiClientService getApiClientService() {
        if (apiClientService == null) {

            apiClientService = getRetrofit().create(ApiClientService.class);
        }
        return apiClientService;

    }


}
