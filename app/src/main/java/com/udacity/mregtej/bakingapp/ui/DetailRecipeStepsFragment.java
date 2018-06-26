package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Step;
import com.udacity.mregtej.bakingapp.global.BakingAppGlobals;
import com.udacity.mregtej.bakingapp.ui.adapter.RecipeStepAdapter;
import com.udacity.mregtej.bakingapp.ui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeStepsFragment extends Fragment
        implements RecipeStepAdapter.RecipeStepClickListener {

    private static final String RECIPE_STEPS_SAVED_INST = "recipe-steps";

    private static final String RECIPE_NAME_EXTRA = "recipe-name";
    private static final String RECIPE_STEP_EXTRA = "recipe-step";
    private static final String RECIPE_STEP_POSITION_EXTRA = "recipe-step-position";

    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mRecipeStepsLayoutManager;
    /** Recipe Ingredients Custom ArrayAdapter */
    private RecipeStepAdapter mRecipeStepsAdapter;
    /** Custom Recipe Card GridView (RecyclerView) */
    @BindView(R.id.recipe_step_description_recyclerview) RecyclerView mRecipeStepsRecyclerView;
    /** Activity Context */
    private Context mContext;

    private String mRecipeName;

    private View rootView;

    public DetailRecipeStepsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Bind Views
        rootView = inflater.inflate(R.layout.fragment_recipe_steps, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        // Load & set GridLayout
        mRecipeStepsRecyclerView.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if(savedInstanceState != null) {

            // Retrieve list of ingredients from savedInstanceState
            List<Step> recipeSteps = savedInstanceState.
                    getParcelableArrayList(RECIPE_STEPS_SAVED_INST);

            // Create RecipeIngredientAdapter (with ingredients)
            mRecipeStepsAdapter = new RecipeStepAdapter(recipeSteps,this);

            // Set Adapter and notifyDataSetChanged
            mRecipeStepsRecyclerView.setAdapter(mRecipeStepsAdapter);
            mRecipeStepsAdapter.notifyDataSetChanged();

        } else {

            // Create RecipeStepAdapter (null recipes steps)
            mRecipeStepsAdapter = new RecipeStepAdapter(null,this);

        }

        // Return rootView
        return rootView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        ArrayList<Step> recipeSteps = (ArrayList<Step>) mRecipeStepsAdapter.getmRecipeSteps();
        outState.putParcelableArrayList(RECIPE_STEPS_SAVED_INST, recipeSteps);
        super.onSaveInstanceState(outState);
    }

    public void setmRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public void setRecipeSteps(List<Step> recipeSteps) {
        mRecipeStepsAdapter.setmRecipeSteps(checkRecipeStepsConsistency(recipeSteps));
        mRecipeStepsRecyclerView.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsAdapter.notifyDataSetChanged();
    }

    public void setRecyclerViewVisibility(boolean isExpanded) {
        if(isExpanded) {
            ViewUtils.expandView(rootView);
        } else {
            ViewUtils.collapseView(rootView);
        }
    }

    /**
     * Set RecyclerView's LayoutManager.
     * Different layouts for portrait and landscape mode
     *
     * @param   rootView    Activity view
     */
    private void setRecyclerViewLayoutManager(View rootView) {
        switch(this.getResources().getConfiguration().orientation) {
            case BakingAppGlobals.LANDSCAPE_VIEW: // Landscape Mode
                mRecipeStepsLayoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        BakingAppGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                break;
            case BakingAppGlobals.PORTRAIT_VIEW: // Portrait Mode
            default:
                mRecipeStepsLayoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                break;
        }
        mRecipeStepsRecyclerView.setLayoutManager(mRecipeStepsLayoutManager);
    }

    @Override
    public void OnRecipeStepClick(int position) {
        Intent i = new Intent(mContext, RecipeStepDetailViewActivity.class);
        ArrayList<Step> recipeSteps = (ArrayList<Step>) mRecipeStepsAdapter.getmRecipeSteps();
        if(recipeSteps != null) {
            i.putExtra(RECIPE_NAME_EXTRA, mRecipeName);
            i.putParcelableArrayListExtra(RECIPE_STEP_EXTRA, recipeSteps);
            i.putExtra(RECIPE_STEP_POSITION_EXTRA, position);
        }
        this.startActivity(i);
    }

    private List<Step> checkRecipeStepsConsistency(List<Step> recipeSteps) {
        ArrayList<Step> wellFormatedRecipeSteps = new ArrayList<>();
        int idKeyCounter = 0;
        for(int i = 0; i < recipeSteps.size(); i++) {
            if(recipeSteps.get(i).getId() == idKeyCounter) {
                wellFormatedRecipeSteps.add(recipeSteps.get(i));
                idKeyCounter++;
            } else {
                do {
                    wellFormatedRecipeSteps.add(new Step(idKeyCounter, "",
                            "", "", ""));
                    idKeyCounter++;
                } while (idKeyCounter != recipeSteps.get(i).getId());
                wellFormatedRecipeSteps.add(recipeSteps.get(i));
                idKeyCounter++;
            }
        }
        return wellFormatedRecipeSteps;
    }

}
