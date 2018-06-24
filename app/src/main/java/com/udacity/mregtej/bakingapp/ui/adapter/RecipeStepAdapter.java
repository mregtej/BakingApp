package com.udacity.mregtej.bakingapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    List<Step> mRecipeSteps;
    private Context mContext;

    public RecipeStepAdapter(List<Step> ingredients) {
        this.mRecipeSteps = ingredients;
    }

    @NonNull
    @Override
    public RecipeStepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_recipe_step, parent,false);
        return new RecipeStepAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapter.ViewHolder holder, int position) {

        // Retrieve Recipe data from Model object
        Step recipeStep = mRecipeSteps.get(position);

        // Set position-tag
        holder.recipeStepLayout.setTag(position);

        // Populate UI elements
        populateUIView(holder, recipeStep, position);

        // TODO SetOnViewClickListener

    }

    @Override
    public int getItemCount() {
        return mRecipeSteps.size();
    }

    public void setmRecipeSteps(List<Step> mRecipeSteps) {
        this.mRecipeSteps = mRecipeSteps;
    }


    //--------------------------------------------------------------------------------|
    //                              Support Classes                                   |
    //--------------------------------------------------------------------------------|

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recipe_step_number) TextView recipeStepNumber;
        @BindView(R.id.tv_recipe_step_description) TextView recipeStepDescription;
        private final View recipeStepLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeStepLayout = itemView;
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * Populate UI view elements
     *
     * @param holder        ViewHolder (View container)
     * @param recipeStep    Recipe Step object
     */
    private void populateUIView(ViewHolder holder, Step recipeStep, int position) {
        // Set Recipe Step Number
        holder.recipeStepNumber.setText(String.valueOf(position));
        // Set Recipe Step shortDescription
        holder.recipeStepDescription.setText(recipeStep.getShortDescription());
    }

}
