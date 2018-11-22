package com.example.abhishek.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishek.bakingapp.dataHolder.RecipeDetail;
import com.example.abhishek.bakingapp.ImageLink;
import com.example.abhishek.bakingapp.R;
import com.example.abhishek.bakingapp.activities.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder> {


    private Context context;
    private LayoutInflater layoutInflater;
    List<RecipeDetail> recipeDetailList;
    private int i;

    public RecipeCardAdapter(Context context, List<RecipeDetail> recipeDetailList, int i) {
        this.context = context;
        this.recipeDetailList = recipeDetailList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.i = i;
    }

    @NonNull
    @Override
    public RecipeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recipe_card, parent, false);
        RecipeCardViewHolder recipeCardViewHolder = new RecipeCardViewHolder(view);
        return recipeCardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeCardViewHolder holder, int position) {
        int width;
        int height;
        if (i == 1) {
            width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
            height = (int) ((context.getResources().getDisplayMetrics().widthPixels * 0.9) / 1.2);
            holder.mainCard.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
            holder.mainCard.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9 / 1.17);
            holder.film.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
            holder.film.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9 / 1.2);
        } else {
            width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9 / 3);
            height = (int) ((context.getResources().getDisplayMetrics().widthPixels * 0.9) / 3.2);
            holder.mainCard.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9 / 3);
            holder.mainCard.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9 / 3.17);
            holder.film.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9 / 3);
            holder.film.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9 / 3.2);
        }


        Picasso.get().load(ImageLink.getImages().get(position)).resize(width, height).centerCrop().into(holder.imageHolder);
        holder.nameOfRecipe.setText(recipeDetailList.get(position).getName());
        holder.mainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", holder.getAdapterPosition());
                intent.putExtra("name", recipeDetailList.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeDetailList.size();
    }

    class RecipeCardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_holder)
        ImageView imageHolder;
        @BindView(R.id.name_of_recipe)
        TextView nameOfRecipe;
        @BindView(R.id.main_card)
        CardView mainCard;

        ImageView film;


        public RecipeCardViewHolder(View itemView) {
            super(itemView);
            film = itemView.findViewById(R.id.film_on_poster_image_view);

            ButterKnife.bind(this, itemView);

        }
    }
}
