package com.udacity.mregtej.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;

import butterknife.ButterKnife;

public class DetailRecipeActivity extends AppCompatActivity {

    private static final String RECIPE_EXTRA = "recipe";

    private Recipe mRecipe;
    private ActionBar mActionBar;

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
    }

}
