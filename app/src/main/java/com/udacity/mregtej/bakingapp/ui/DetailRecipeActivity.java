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

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeActivity extends AppCompatActivity {

    private static final String RECIPE_EXTRA = "recipe";

    private Recipe mRecipe;
    private ActionBar mActionBar;

    private static boolean isIngredientsRecyclerViewExpanded;
    private static boolean isStepsRecyclerViewExpanded;

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

        // Check if intent has extras
        Intent i = getIntent();
        if (i.hasExtra(RECIPE_EXTRA)) {

            // Retrieve Recipe parcelable object from Intent.extras
            mRecipe = i.getParcelableExtra(RECIPE_EXTRA);

            // Set recipe name as Activity title
            mActionBar.setTitle(mRecipe.getName());

            // TODO Create Recipe Fragments (Populate UI)
            populateUIFragments();

            // TODO Add Collapse/Expand clickListeners
            setCollapseViewClickListeners();

        }

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
        }
        mDetailRecipeStepsFragment = (DetailRecipeStepsFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_steps_fragment);
        if(mDetailRecipeStepsFragment != null) {
            mDetailRecipeStepsFragment.setRecipeSteps(mRecipe.getSteps());
        }

        // RecyclerViews expanded by default
        isIngredientsRecyclerViewExpanded = true;
        isStepsRecyclerViewExpanded = true;
        mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_collapse);
        mCollapseStepsImageView.setImageResource(R.mipmap.ic_collapse);
    }

    private void setCollapseViewClickListeners() {

        // Set Expand/Collapse OnClick action for Ingredients RecyclerView
        mCollapseIngredientsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDetailRecipeIngredientsFragment != null) {
                    mDetailRecipeIngredientsFragment.setRecyclerViewVisibility(isIngredientsRecyclerViewExpanded);
                }
                isIngredientsRecyclerViewExpanded = !isIngredientsRecyclerViewExpanded;
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
                if(mDetailRecipeStepsFragment != null) {
                    mDetailRecipeStepsFragment.setRecyclerViewVisibility(isStepsRecyclerViewExpanded);
                }
                isStepsRecyclerViewExpanded = !isStepsRecyclerViewExpanded;
                if(isStepsRecyclerViewExpanded) {
                    mCollapseStepsImageView.setImageResource(R.mipmap.ic_collapse);
                } else {
                    mCollapseStepsImageView.setImageResource(R.mipmap.ic_expand);
                }
            }
        });

    }

}
