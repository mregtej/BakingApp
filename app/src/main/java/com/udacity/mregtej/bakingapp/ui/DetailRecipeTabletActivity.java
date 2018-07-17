package com.udacity.mregtej.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;

public class DetailRecipeTabletActivity extends AppCompatActivity
        implements RecipeStepDataDetailViewFragment.ChangeRecipeStepClickListener,
        DetailRecipeStepsFragment.RecipeStepClickListener {

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Key for storing the recipe in savedInstanceState */
    private static final String RECIPE_SAVED_INST = "recipe";
    /** Key for storing the recipe step position in savedInstanceState */
    private static final String RECIPE_STEP_POSITION_SAVED_INST = "recipe-step-position";

    /** Key for storing the recipe in Intent.Extras (Bundle) */
    private static final String RECIPE_EXTRA = "recipe";


    //--------------------------------------------------------------------------------|
    //                                 Parameters                                     |
    //--------------------------------------------------------------------------------|

    /** Recipe Ingredients Fragment */
    private DetailRecipeIngredientsFragment mDetailRecipeIngredientsFragment;
    /** Recipe Steps Fragment */
    private DetailRecipeStepsFragment mDetailRecipeStepsFragment;
    /** Recipe Step Data Fragment */
    private RecipeStepDataDetailViewFragment mRecipeStepDataDetailViewFragment;
    /** Recipe Step Video Fragment */
    private RecipeStepVideoDetailViewFragment mRecipeStepVideoDetailViewFragment;
    /** Recipe (DataModel) */
    private Recipe mRecipe;
    /** App ActionBar */
    private ActionBar mActionBar;
    /** Recipe Step Position */
    private int mRecipeStepPosition;


    //--------------------------------------------------------------------------------|
    //                                 Override Methods                               |
    //--------------------------------------------------------------------------------|

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe_tablet);

        // Retrieve ActionBar and enable up navigation
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {

            mRecipe = savedInstanceState.getParcelable(RECIPE_SAVED_INST);
            mRecipeStepPosition = savedInstanceState.getInt(RECIPE_STEP_POSITION_SAVED_INST, 0);

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

        // Set recipe name as Activity title
        mActionBar.setTitle(mRecipe.getName());

        // Create Recipe Fragments (Populate UI)
        populateUIFragments();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RECIPE_SAVED_INST, mRecipe);
        outState.putInt(RECIPE_STEP_POSITION_SAVED_INST, mRecipeStepPosition);
        super.onSaveInstanceState(outState);
    }


    //--------------------------------------------------------------------------------|
    //                               Private Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Populates all UI Fragments.
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
            mDetailRecipeStepsFragment.setmRecipeStepClickListener(this);
            mDetailRecipeStepsFragment.setmRecipeName(mRecipe.getName());
        }

        mRecipeStepVideoDetailViewFragment = (RecipeStepVideoDetailViewFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_step_video);
        if(mRecipeStepVideoDetailViewFragment != null) {
            mRecipeStepVideoDetailViewFragment.setmRecipeStep(
                    mRecipe.getSteps().get(mRecipeStepPosition));
        }

        mRecipeStepDataDetailViewFragment = (RecipeStepDataDetailViewFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_step_data);
        if(mRecipeStepDataDetailViewFragment != null) {
            mRecipeStepDataDetailViewFragment.setmChangeRecipeStepClickListener(this);
            mRecipeStepDataDetailViewFragment.setmRecipeStep(mRecipe.getSteps().get(mRecipeStepPosition));
        }

    }


    //--------------------------------------------------------------------------------|
    //                           Fragment --> Activity Comm                           |
    //--------------------------------------------------------------------------------|

    @Override
    public void onClickNextRecipeStep() {
        if (mRecipeStepDataDetailViewFragment != null && mRecipeStepVideoDetailViewFragment != null) {
            if (mRecipeStepPosition < mRecipe.getSteps().size()) {
                mRecipeStepPosition = (++mRecipeStepPosition) % mRecipe.getSteps().size();
                mRecipeStepDataDetailViewFragment.getmRecipePreviousStepButton().setEnabled(true);
                // Set Recipe Step data to Fragments
                mRecipeStepDataDetailViewFragment.setmRecipeStep(
                        mRecipe.getSteps().get(mRecipeStepPosition));
                mRecipeStepVideoDetailViewFragment.setmRecipeStep(
                        mRecipe.getSteps().get(mRecipeStepPosition));
                // Update UI
                mRecipeStepDataDetailViewFragment.updateUI();
                mRecipeStepVideoDetailViewFragment.updateUI();
                if (mRecipeStepPosition == mRecipe.getSteps().size() - 1) {
                    mRecipeStepDataDetailViewFragment.getmRecipeNextStepButton().setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onClickPreviousRecipeStep() {
        if (mRecipeStepDataDetailViewFragment != null && mRecipeStepVideoDetailViewFragment != null) {
            if (mRecipeStepPosition > 0) {
                mRecipeStepPosition = (--mRecipeStepPosition) % mRecipe.getSteps().size();
                mRecipeStepDataDetailViewFragment.getmRecipeNextStepButton().setEnabled(true);
                // Set Recipe Step data to Fragments
                mRecipeStepDataDetailViewFragment.setmRecipeStep(
                        mRecipe.getSteps().get(mRecipeStepPosition));
                mRecipeStepVideoDetailViewFragment.setmRecipeStep(
                        mRecipe.getSteps().get(mRecipeStepPosition));
                // Update UI
                mRecipeStepDataDetailViewFragment.updateUI();
                mRecipeStepVideoDetailViewFragment.updateUI();
                if (mRecipeStepPosition == 0) {
                    mRecipeStepDataDetailViewFragment.getmRecipePreviousStepButton().setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onRecipeStepClick(int position) {

        mRecipeStepPosition = position;

        if(mRecipeStepVideoDetailViewFragment != null) {
            mRecipeStepVideoDetailViewFragment.setmRecipeStep(
                   mRecipe.getSteps().get(mRecipeStepPosition));
            mRecipeStepVideoDetailViewFragment.updateUI();
        }

        if(mRecipeStepDataDetailViewFragment != null) {
            mRecipeStepDataDetailViewFragment.setmRecipeStep(mRecipe.getSteps().get(mRecipeStepPosition));
            mRecipeStepDataDetailViewFragment.updateUI();
        }
    }

}
