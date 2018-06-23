package com.udacity.mregtej.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.ViewHolder> {

    private List<Recipe> mRecipeList;
    private Context mContext;

    public RecipeCardAdapter(List<Recipe> recipes) {
        this.mRecipeList = recipes;
    }

    @NonNull
    @Override
    public RecipeCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                           int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_recipe_card, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Retrieve Recipe data from Model object
        Recipe recipe = mRecipeList.get(position);

        // Set position-tag
        holder.recipeCardLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, recipe);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public void setmRecipeList(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
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

    /**
     * Populate UI view elements
     *
     * @param holder    ViewHolder (View container)
     * @param recipe    Recipe object
     */
    private void populateUIView(ViewHolder holder, Recipe recipe) {
        Picasso
                .with(mContext)
                .load(R.drawable.im_baking)
                .fit()
                .centerCrop()
                // .error(R.drawable.im_image_not_available)
                .into(holder.recipePhoto);
        // Set title of TMDBFilm
        holder.recipeName.setText(recipe.getName());
    }

}
