package com.udacity.mregtej.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.datamodel.Step;
import com.udacity.mregtej.bakingapp.ui.adapter.RecipeStepAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeActivity extends AppCompatActivity {

    private static final String RECIPE_SAVED_INST = "recipe";
    private static final String RECIPE_INGREDIENTS_EXPANDED_SAVED_INST = "is-ingredients-expanded";
    private static final String RECIPE_STEPS_EXPANDED_SAVED_INST = "is-steps-expanded";

    private static final String RECIPE_EXTRA = "recipe";

    private Recipe mRecipe;
    private ActionBar mActionBar;

    private boolean isIngredientsRecyclerViewExpanded;
    private boolean isStepsRecyclerViewExpanded;

    @BindView(R.id.ib_expand_collapse_ingredients_rv) ImageView mCollapseIngredientsImageView;
    @BindView(R.id.ib_expand_collapse_steps_rv) ImageView mCollapseStepsImageView;

    private DetailRecipeIngredientsFragment mDetailRecipeIngredientsFragment;
    private DetailRecipeStepsFragment mDetailRecipeStepsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        ButterKnife.bind(this);

        // Retrieve ActionBar and enable up navigation
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {

            mRecipe = savedInstanceState.getParcelable(RECIPE_SAVED_INST);
            isIngredientsRecyclerViewExpanded = savedInstanceState.
                    getBoolean(RECIPE_INGREDIENTS_EXPANDED_SAVED_INST);
            isStepsRecyclerViewExpanded = savedInstanceState.
                    getBoolean(RECIPE_STEPS_EXPANDED_SAVED_INST);

        } else {

            // Check if intent has extras
            Intent i = getIntent();
            if (i.hasExtra(RECIPE_EXTRA)) {

                // Retrieve Recipe parcelable object from Intent.extras
                mRecipe = i.getParcelableExtra(RECIPE_EXTRA);

                // Set recipe name as Activity title
                mActionBar.setTitle(mRecipe.getName());

                // RecyclerViews expanded by default
                isIngredientsRecyclerViewExpanded = true;
                isStepsRecyclerViewExpanded = true;
            }

        }

        // Create Recipe Fragments (Populate UI)
        populateUIFragments();

        // Add Collapse/Expand clickListeners
        setCollapseViewClickListeners();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_SAVED_INST, mRecipe);
        outState.putBoolean(RECIPE_INGREDIENTS_EXPANDED_SAVED_INST, isIngredientsRecyclerViewExpanded);
        outState.putBoolean(RECIPE_STEPS_EXPANDED_SAVED_INST, isStepsRecyclerViewExpanded);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUIFragments() {

        mDetailRecipeIngredientsFragment = (DetailRecipeIngredientsFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_ingredients_fragment);
        if(mDetailRecipeIngredientsFragment != null) {
            mDetailRecipeIngredientsFragment.setRecipeIngredients(mRecipe.getIngredients());
            mDetailRecipeIngredientsFragment.setRecyclerViewVisibility(
                    isIngredientsRecyclerViewExpanded);
        }

        mDetailRecipeStepsFragment = (DetailRecipeStepsFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_steps_fragment);
        if(mDetailRecipeStepsFragment != null) {
            mDetailRecipeStepsFragment.setRecipeSteps(mRecipe.getSteps());
            mDetailRecipeStepsFragment.setmRecipeName(mRecipe.getName());
            mDetailRecipeStepsFragment.setRecyclerViewVisibility(isStepsRecyclerViewExpanded);
        }

        // Handle expansion / collapse of RecyclerViews
        if(isIngredientsRecyclerViewExpanded) {
            mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_collapse);
        } else {
            mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_expand);
        }
        if(isStepsRecyclerViewExpanded) {
            mCollapseStepsImageView.setImageResource(R.mipmap.ic_collapse);
        } else {
            mCollapseStepsImageView.setImageResource(R.mipmap.ic_expand);
        }

    }

    private void setCollapseViewClickListeners() {

        // Set Expand/Collapse OnClick action for Ingredients RecyclerView
        mCollapseIngredientsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIngredientsRecyclerViewExpanded = !isIngredientsRecyclerViewExpanded;
                if(mDetailRecipeIngredientsFragment != null) {
                    mDetailRecipeIngredientsFragment.setRecyclerViewVisibility(isIngredientsRecyclerViewExpanded);
                }
                if(isIngredientsRecyclerViewExpanded) {
                    mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_collapse);
                } else {
                    mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_expand);
                }
            }
        });

        // Set Expand/Collapse OnClick action for Steps RecyclerView
        mCollapseStepsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStepsRecyclerViewExpanded = !isStepsRecyclerViewExpanded;
                if(mDetailRecipeStepsFragment != null) {
                    mDetailRecipeStepsFragment.setRecyclerViewVisibility(isStepsRecyclerViewExpanded);
                }
                if(isStepsRecyclerViewExpanded) {
                    mCollapseStepsImageView.setImageResource(R.mipmap.ic_collapse);
                } else {
                    mCollapseStepsImageView.setImageResource(R.mipmap.ic_expand);
                }
            }
        });

    }

}
