package com.udacity.mregtej.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Ingredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientAdapter extends
        RecyclerView.Adapter<RecipeIngredientAdapter.ViewHolder> {

    List<Ingredient> mIngredients;
    private Context mContext;

    public RecipeIngredientAdapter(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    @NonNull
    @Override
    public RecipeIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                 int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_recipe_ingredient, parent,false);
        return new RecipeIngredientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Retrieve Recipe data from Model object
        Ingredient ingredient = mIngredients.get(position);

        // Set position-tag
        holder.recipeIngredientLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, ingredient);

    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public void setmIngredients(List<Ingredient> mIngredients) {
        this.mIngredients = mIngredients;
    }

    //--------------------------------------------------------------------------------|
    //                              Support Classes                                   |
    //--------------------------------------------------------------------------------|

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ingredient_name) TextView ingredientName;
        @BindView(R.id.tv_ingredient_quantity) TextView ingredientQuantity;
        private final View recipeIngredientLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeIngredientLayout = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * Populate UI view elements
     *
     * @param holder        ViewHolder (View container)
     * @param ingredient    Ingredient object
     */
    private void populateUIView(ViewHolder holder, Ingredient ingredient) {
        // Set Ingredient name
        holder.ingredientName.setText(ingredient.getIngredient());
        // Set Ingredient quantity:
        // Quantity(Numeric) + Measure(Metrics))
        holder.ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()) + " "
                + ingredient.getMeasure());
    }

}
