package com.example.abhishek.bakingapp;

import com.example.abhishek.bakingapp.dataHolder.RecipeDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClientService {


    @GET("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
    Call<List<RecipeDetail>> getRecipeList();


}
