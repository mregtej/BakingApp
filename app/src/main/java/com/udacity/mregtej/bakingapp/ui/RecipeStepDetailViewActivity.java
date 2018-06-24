package com.udacity.mregtej.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailViewActivity extends AppCompatActivity {

    private static final String RECIPE_NAME_EXTRA = "recipe-name";
    private static final String RECIPE_STEPS_EXTRA = "recipe-step";
    private static final String RECIPE_STEP_POSITION_EXTRA = "recipe-step-position";

    @BindView(R.id.bt_next_recipe_step) TextView mRecipeNextStepButton;
    @BindView(R.id.bt_previous_recipe_step) TextView mRecipePreviousStepButton;

    private ArrayList<Step> mRecipeSteps;
    private int mRecipeStepPosition;
    private String mRecipeName;

    private ActionBar mActionBar;

    private RecipeStepDetailViewFragment mRecipeStepDetailViewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe_step);
        ButterKnife.bind(this);

        // Retrieve ActionBar and enable up navigation
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        // Check if intent has extras
        Intent i = getIntent();
        if (i.hasExtra(RECIPE_STEPS_EXTRA) && i.hasExtra(RECIPE_STEP_POSITION_EXTRA) &&
                i.hasExtra(RECIPE_NAME_EXTRA)) {

            // Retrieve Recipe Step parcelable object from Intent.extras
            mRecipeName = i.getStringExtra(RECIPE_NAME_EXTRA);
            mRecipeSteps = i.getParcelableArrayListExtra(RECIPE_STEPS_EXTRA);
            mRecipeStepPosition = i.getIntExtra(RECIPE_STEP_POSITION_EXTRA,0);

            // Set Recipe name as Activity Title
            mActionBar.setTitle(mRecipeName);

            // Set Prev/Next OnClickListeners
            setButtonClickListeners();

            // Create Recipe Fragments (Populate UI)
            populateUIFragments();

        }

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
        mRecipeStepDetailViewFragment = (RecipeStepDetailViewFragment)
                getSupportFragmentManager().
                        findFragmentById(R.id.fr_detail_recipe_step);
        if(mRecipeStepDetailViewFragment != null) {
            mRecipeStepDetailViewFragment.setmRecipeStep(mRecipeSteps.get(mRecipeStepPosition));
        }
    }

    private void setButtonClickListeners() {
        mRecipeNextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecipeStepDetailViewFragment != null) {
                    mRecipeStepDetailViewFragment.setmRecipeStep(mRecipeSteps.get(
                            (++mRecipeStepPosition) % mRecipeSteps.size()));
                }
            }
        });
        mRecipePreviousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecipeStepDetailViewFragment != null) {
                    mRecipeStepDetailViewFragment.setmRecipeStep(mRecipeSteps.get(
                            (--mRecipeStepPosition) % mRecipeSteps.size()));
                }
            }
        });
    }

}
