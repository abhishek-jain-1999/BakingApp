package com.example.abhishek.bakingapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.abhishek.bakingapp.adapter.RecipeCardAdapter;
import com.example.abhishek.bakingapp.ApiClient;
import com.example.abhishek.bakingapp.ApiClientService;
import com.example.abhishek.bakingapp.dataHolder.Ingredients;
import com.example.abhishek.bakingapp.dataHolder.RecipeDetail;
import com.example.abhishek.bakingapp.ImageLink;
import com.example.abhishek.bakingapp.R;
import com.example.abhishek.bakingapp.dataHolder.Steps;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    RecyclerView mainRecyclerViewPortrait;
    @Nullable
    RecyclerView mainRecyclerViewLand;


    RecipeCardAdapter recipeCardAdapter;
    List<RecipeDetail> recipeDetailList = new ArrayList<>(5);
    ApiClientService apiClientService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageLink.setImages();

        mainRecyclerViewPortrait = findViewById(R.id.main_recycler_view_portrait);
        mainRecyclerViewLand = findViewById(R.id.main_recycler_view_land);

        apiClientService = ApiClient.getApiClientService();
        Call<List<RecipeDetail>> recipeLISTCall = apiClientService.getRecipeList();
        recipeLISTCall.enqueue(new Callback<List<RecipeDetail>>() {
            @Override
            public void onResponse(Call<List<RecipeDetail>> call, Response<List<RecipeDetail>> response) {
                if (response.body() != null) {
                    recipeDetailList.addAll(response.body());
                    setRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<List<RecipeDetail>> call, Throwable t) {

            }
        });

        /*RecipeDetail recipeDetail = new RecipeDetail();
        recipeDetail.setId(0);
        recipeDetail.setImage("");
        recipeDetail.setServings(8);

        List<Ingredients> ingredients = new ArrayList<>();
        List<Steps> steps = new ArrayList<>();
        recipeDetail.setIngredients(ingredients);
        recipeDetail.setSteps(steps);

        recipeDetailList.add(recipeDetail);
        setRecyclerView();*/


    }

    private void setRecyclerView() {
        if (mainRecyclerViewLand == null) {
            recipeCardAdapter = new RecipeCardAdapter(this, recipeDetailList, 1);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mainRecyclerViewPortrait.setAdapter(recipeCardAdapter);
            mainRecyclerViewPortrait.setLayoutManager(linearLayoutManager);
            mainRecyclerViewPortrait.setClickable(true);
        } else {
            recipeCardAdapter = new RecipeCardAdapter(this, recipeDetailList, 2);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            mainRecyclerViewLand.setLayoutManager(gridLayoutManager);
            mainRecyclerViewLand.setAdapter(recipeCardAdapter);
        }
    }
}
