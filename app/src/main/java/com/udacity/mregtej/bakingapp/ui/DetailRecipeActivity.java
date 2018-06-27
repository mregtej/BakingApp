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

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Key for storing the recipe in savedInstanceState */
    private static final String RECIPE_KEY = "recipe";
    /** Key for storing the recipe list in Intent.Extras (Bundle) */
    private static final String RECIPE_EXTRA = "recipe";


    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Recipe object (From DataModel) */
    private Recipe mRecipe;

    /** Activity ActionBar */
    private ActionBar mActionBar;
    /** Recipe Ingredients Fragment (GridView) */
    private DetailRecipeIngredientsFragment mDetailRecipeIngredientsFragment;
    /** Recipe Steps Fragment (GridView) */
    private DetailRecipeStepsFragment mDetailRecipeStepsFragment;


    //--------------------------------------------------------------------------------|
    //                            Override Methods                                    |
    //--------------------------------------------------------------------------------|

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        ButterKnife.bind(this);

        // Retrieve ActionBar and enable up navigation
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {

            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);

        } else {

            // Check if intent has extras
            Intent i = getIntent();
            if (i.hasExtra(RECIPE_EXTRA)) {

                // Retrieve Recipe parcelable object from Intent.extras
                mRecipe = i.getParcelableExtra(RECIPE_EXTRA);

                // Set recipe name as Activity title
                mActionBar.setTitle(mRecipe.getName());

            }

        }

        // Create Recipe Fragments (Populate UI)
        populateUIFragments();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_KEY, mRecipe);
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


    //--------------------------------------------------------------------------------|
    //                            Override Methods                                    |
    //--------------------------------------------------------------------------------|

    /**
     * Populate Fragments with ModelInfo (Activity --> Fragment Comm)
     */
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
            mDetailRecipeStepsFragment.setmRecipeName(mRecipe.getName());
        }

    }

}
