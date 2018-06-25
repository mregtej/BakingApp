package com.udacity.mregtej.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Step;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeStepDetailViewActivity extends AppCompatActivity
        implements RecipeStepDataDetailViewFragment.ChangeRecipeStepClickListener{

    private static final String RECIPE_NAME_SAVED_INST = "recipe-name";
    private static final String RECIPE_STEPS_SAVED_INST = "recipe-step";
    private static final String RECIPE_STEP_POSITION_SAVED_INST = "recipe-step-position";

    private static final String RECIPE_NAME_EXTRA = "recipe-name";
    private static final String RECIPE_STEPS_EXTRA = "recipe-step";
    private static final String RECIPE_STEP_POSITION_EXTRA = "recipe-step-position";

    private ArrayList<Step> mRecipeSteps;
    private int mRecipeStepPosition;
    private String mRecipeName;

    private ActionBar mActionBar;

    private RecipeStepDataDetailViewFragment mRecipeStepDataDetailViewFragment;
    private RecipeStepVideoDetailViewFragment mRecipeStepVideoDetailViewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        switch(getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            default:
            case Configuration.ORIENTATION_PORTRAIT:
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe_step);
        ButterKnife.bind(this);

        // Retrieve ActionBar and enable up navigation
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null) {

            mRecipeName = savedInstanceState.getString(RECIPE_NAME_SAVED_INST);
            mRecipeSteps = savedInstanceState.getParcelableArrayList(RECIPE_STEPS_SAVED_INST);
            mRecipeStepPosition = savedInstanceState.getInt(RECIPE_STEP_POSITION_SAVED_INST, 0);

        } else {

            // Check if intent has extras
            Intent i = getIntent();
            if (i.hasExtra(RECIPE_STEPS_EXTRA) && i.hasExtra(RECIPE_STEP_POSITION_EXTRA) &&
                    i.hasExtra(RECIPE_NAME_EXTRA)) {

                // Retrieve Recipe Step parcelable object from Intent.extras
                mRecipeName = i.getStringExtra(RECIPE_NAME_EXTRA);
                mRecipeSteps = i.getParcelableArrayListExtra(RECIPE_STEPS_EXTRA);
                mRecipeStepPosition = i.getIntExtra(RECIPE_STEP_POSITION_EXTRA, 0);

                // Set Recipe name as Activity Title
                mActionBar.setTitle(mRecipeName);

            }

        }

        // Create Recipe Fragments (Populate UI)
        populateUIFragments();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(RECIPE_NAME_SAVED_INST, mRecipeName);
        outState.putParcelableArrayList(RECIPE_STEPS_SAVED_INST, mRecipeSteps);
        outState.putInt(RECIPE_STEP_POSITION_SAVED_INST, mRecipeStepPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUIFragments() {

        mRecipeStepVideoDetailViewFragment = (RecipeStepVideoDetailViewFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_step_video);
        if(mRecipeStepVideoDetailViewFragment != null) {
            mRecipeStepVideoDetailViewFragment.setmRecipeStep(
                    mRecipeSteps.get(mRecipeStepPosition));
        }

        mRecipeStepDataDetailViewFragment = (RecipeStepDataDetailViewFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_step_data);
        if(mRecipeStepDataDetailViewFragment != null) {
            mRecipeStepDataDetailViewFragment.setmChangeRecipeStepClickListener(this);
            mRecipeStepDataDetailViewFragment.setmRecipeStep(mRecipeSteps.get(mRecipeStepPosition));
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mActionBar.hide();
        } else {
            mActionBar.show();
        }

    }

    @Override
    public void onClickNextRecipeStep() {
        if (mRecipeStepDataDetailViewFragment != null && mRecipeStepVideoDetailViewFragment != null) {
            if (mRecipeStepPosition < mRecipeSteps.size()) {
                mRecipeStepPosition = (++mRecipeStepPosition) % mRecipeSteps.size();
                mRecipeStepDataDetailViewFragment.getmRecipePreviousStepButton().setEnabled(true);
                // Set Recipe Step data to Fragments
                mRecipeStepDataDetailViewFragment.setmRecipeStep(
                        mRecipeSteps.get(mRecipeStepPosition));
                mRecipeStepVideoDetailViewFragment.setmRecipeStep(
                        mRecipeSteps.get(mRecipeStepPosition));
                // Update UI
                mRecipeStepDataDetailViewFragment.updateUI();
                mRecipeStepVideoDetailViewFragment.updateUI();
                if (mRecipeStepPosition == mRecipeSteps.size() - 1) {
                    mRecipeStepDataDetailViewFragment.getmRecipeNextStepButton().setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onClickPreviousRecipeStep() {
        if (mRecipeStepDataDetailViewFragment != null && mRecipeStepVideoDetailViewFragment != null) {
            if (mRecipeStepPosition > 0) {
                mRecipeStepPosition = (--mRecipeStepPosition) % mRecipeSteps.size();
                mRecipeStepDataDetailViewFragment.getmRecipeNextStepButton().setEnabled(true);
                // Set Recipe Step data to Fragments
                mRecipeStepDataDetailViewFragment.setmRecipeStep(
                        mRecipeSteps.get(mRecipeStepPosition));
                mRecipeStepVideoDetailViewFragment.setmRecipeStep(
                        mRecipeSteps.get(mRecipeStepPosition));
                // Update UI
                mRecipeStepDataDetailViewFragment.updateUI();
                mRecipeStepVideoDetailViewFragment.updateUI();
                if (mRecipeStepPosition == 0) {
                    mRecipeStepDataDetailViewFragment.getmRecipePreviousStepButton().setEnabled(false);
                }
            }
        }
    }

}
