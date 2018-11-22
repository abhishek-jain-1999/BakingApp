package com.example.abhishek.bakingapp.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.abhishek.bakingapp.ApiClient;
import com.example.abhishek.bakingapp.ApiClientService;
import com.example.abhishek.bakingapp.BuildConfig;
import com.example.abhishek.bakingapp.dataHolder.Ingredients;
import com.example.abhishek.bakingapp.dataHolder.RecipeDetail;
import com.example.abhishek.bakingapp.dataHolder.Steps;
import com.example.abhishek.bakingapp.fragment.IngredientsAndStepsListFragment;
import com.example.abhishek.bakingapp.fragment.VideoDisplayFragment;
import com.example.abhishek.bakingapp.R;
import com.example.abhishek.bakingapp.widget.IngredientWidget;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    List<RecipeDetail> recipeDetailList = new ArrayList<>(5);
    ApiClientService apiClientService;
    private IngredientsAndStepsListFragment fragment;

    boolean landMode = false, stepClicked = false;
    private IngredientsAndStepsListFragment fragment1;
    private VideoDisplayFragment fragment2;
    private FragmentTransaction fragmentTransaction1;
    private String name;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_activty);
        final Intent intent = getIntent();

        FrameLayout layout = findViewById(R.id.detail_contain_fragment);
        if (layout != null) {
            landMode = true;
        }


        apiClientService = ApiClient.getApiClientService();
        Call<List<RecipeDetail>> recipeLISTCall = apiClientService.getRecipeList();
        recipeLISTCall.enqueue(new Callback<List<RecipeDetail>>() {
            @Override
            public void onResponse(@NonNull Call<List<RecipeDetail>> call, Response<List<RecipeDetail>> response) {
                if (response.body() != null) {
                    recipeDetailList.addAll(response.body());
                    setFragment(intent);
                }
            }

            @Override
            public void onFailure(Call<List<RecipeDetail>> call, Throwable t) {

            }
        });
        if (intent != null) {
            name = intent.getStringExtra("name");
            i = intent.getIntExtra("id", -1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_to_widget, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedPreferences = this.getSharedPreferences(BuildConfig.APPLICATION_ID, this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("title", name);
        List<Ingredients> ingredientsList = null;
        if (recipeDetailList.size() != 0) {
            ingredientsList = recipeDetailList.get(i).getIngredients();
        }
        if (ingredientsList != null) {
            String s = "";
            for (Ingredients i : ingredientsList) {
                s = s + i.getIngredient().toUpperCase() + " ( " + i.getQuantity() + " " + i.getMeasure() + " )\n";
            }
            s = s.substring(0, s.length() - 1);
            editor.putString("ingredient", s);
        }
        editor.commit();
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(),
                        IngredientWidget.class));

        IngredientWidget ingredientWidget = new IngredientWidget();
        ingredientWidget.onUpdate(this, AppWidgetManager.getInstance(this), ids);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (!stepClicked) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            if (fragment2 != null) {
                fragmentTransaction.remove(fragment2);
            }
            fragmentTransaction.commit();
        }
        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        stepClicked = false;
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setFragment(final Intent intent) {
        if (intent != null) {
            if (i != -1) {
                final RecipeDetail recipeDetail = recipeDetailList.get(i);
                setTitle(recipeDetail.getName());
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragment = IngredientsAndStepsListFragment.newInstance("", "", recipeDetail);
                fragment.setOnFragmentInteractionListener(new IngredientsAndStepsListFragment.OnFragmentInteractionListener() {
                    @Override
                    public void onFragmentInteraction(int ii) {
                        if (!landMode) {
                            Intent intent1 = new Intent(DetailActivity.this, StepsActivity.class);
                            intent1.putExtra("Description", createListOfString(recipeDetail.getSteps(), 1));
                            intent1.putExtra("ShortDescription", createListOfString(recipeDetail.getSteps(), 2));
                            intent1.putExtra("Id", recipeDetail.getSteps().get(ii).getId());
                            intent1.putExtra("VideoURL", createListOfString(recipeDetail.getSteps(), 3));
                            intent1.putExtra("ThumbnailURL", createListOfString(recipeDetail.getSteps(), 4));
                            intent1.putExtra("title", recipeDetail.getName());
                            stepClicked = true;
                            DetailActivity.this.startActivityForResult(intent1, 0);
                        } else {
                            fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                            fragment2 = VideoDisplayFragment.newInstance(recipeDetail.getSteps().get(ii).getVideoURL(), recipeDetail.getSteps().get(ii).getDescription(), null);
                            fragmentTransaction1.replace(R.id.detail_contain_fragment, fragment2);
                            fragmentTransaction1.commit();
                        }
                    }
                });
                fragmentTransaction.replace(R.id.list_contain_fragment, fragment);

                if (landMode) {
                    fragment2 = VideoDisplayFragment.newInstance("", "", null);
                    fragmentTransaction.replace(R.id.detail_contain_fragment, fragment2);
                }
                fragmentTransaction.commit();

            }
        }
    }


    private String[] createListOfString(List<Steps> list, int i) {
        String[] s = new String[list.size()];
        int count = 0;
        switch (i) {
            case 1:
                for (Steps o : list) {
                    s[count++] = o.getDescription();
                }
                break;
            case 2:
                for (Steps o : list) {
                    s[count++] = o.getShortDescription();
                }
                break;
            case 3:
                for (Steps o : list) {
                    s[count++] = o.getVideoURL();
                }
                break;
            case 4:
                for (Steps o : list) {
                    s[count++] = o.getThumbnailURL();
                }
                break;

        }
        return s;
    }
}
