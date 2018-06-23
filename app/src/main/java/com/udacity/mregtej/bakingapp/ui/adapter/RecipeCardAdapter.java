package com.udacity.mregtej.bakingapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.mregtej.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {

    public RecipeCardAdapter() {
    }

    @NonNull
    @Override
    public RecipeCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_recipe_card, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // TODO Retrieve Recipe data from Model object

        // Set position-tag
        holder.recipeCardLayout.setTag(position);

        // TODO Populate UI elements
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_photo) ImageView recipePhoto;
        @BindView(R.id.tv_recipe_name) TextView recipeName;
        private final View recipeCardLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeCardLayout = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

}
