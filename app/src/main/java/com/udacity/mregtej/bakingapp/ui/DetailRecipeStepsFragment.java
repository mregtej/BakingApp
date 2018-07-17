package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Key for storing the list state in savedInstanceState */
    private static final String LIST_STATE_SAVED_INST = "list-state";
    /** Key for storing the recipe steps in savedInstanceState */
    private static final String RECIPE_STEPS_SAVED_INST = "recipe-steps";
    /** Key for storing the expand/collapse recipe step GridView state in savedInstanceState */
    private static final String RECIPE_STEPS_EXPANDED_SAVED_INST = "is-steps-expanded";

    /** Key for storing the recipe name in Intent.Extras (Bundle) */
    private static final String RECIPE_NAME_EXTRA = "recipe-name";
    /** Key for storing the recipe step in Intent.Extras (Bundle) */
    private static final String RECIPE_STEP_EXTRA = "recipe-step";
    /** Key for storing the recipe step position in Intent.Extras (Bundle) */
    private static final String RECIPE_STEP_POSITION_EXTRA = "recipe-step-position";

    //--------------------------------------------------------------------------------|
    //                                 Parameters                                     |
    //--------------------------------------------------------------------------------|

    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mRecipeStepsLayoutManager;
    /** List state stored in savedInstanceState */
    private Parcelable mListState;
    /** Recipe Steps Custom ArrayAdapter */
    private RecipeStepAdapter mRecipeStepsAdapter;
    /** Custom Recipe Step GridView (RecyclerView) */
    @BindView(R.id.recipe_step_description_recyclerview) RecyclerView mRecipeStepsRecyclerView;
    /** Expand/Collapse Recipe Step GridView Button */
    @BindView(R.id.ib_expand_collapse_steps_rv) ImageView mCollapseStepsImageView;
    /** Activity Context */
    private Context mContext;
    /** Recipe expandable/collapse GridView status */
    private boolean isStepsRecyclerViewExpanded;
    /** Recipe name*/
    private String mRecipeName;
    /** Fragment rootView */
    private View rootView;
    /** Flag for identifying a tablet device */
    private boolean tabletSize;
    /** Recipe Step onClickListener */
    private RecipeStepClickListener mRecipeStepClickListener;


    //--------------------------------------------------------------------------------|
    //                                 Constructor                                    |
    //--------------------------------------------------------------------------------|

    public DetailRecipeStepsFragment() { }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Bind Views
        rootView = inflater.inflate(R.layout.fragment_recipe_steps, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();
        tabletSize = getResources().getBoolean(R.bool.isTablet);

        // Load & set GridLayout
        mRecipeStepsRecyclerView.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if(savedInstanceState != null) {

            // Retrieve list of ingredients from savedInstanceState
            List<Step> recipeSteps = savedInstanceState.
                    getParcelableArrayList(RECIPE_STEPS_SAVED_INST);
            isStepsRecyclerViewExpanded = savedInstanceState.
                    getBoolean(RECIPE_STEPS_EXPANDED_SAVED_INST);
            setRecyclerViewVisibility(isStepsRecyclerViewExpanded);

            // Create RecipeIngredientAdapter (with ingredients)
            mRecipeStepsAdapter = new RecipeStepAdapter(recipeSteps,this);

            // Set Adapter and notifyDataSetChanged
            mRecipeStepsRecyclerView.setAdapter(mRecipeStepsAdapter);
            mRecipeStepsAdapter.notifyDataSetChanged();

        } else {

            // Create RecipeStepAdapter (null recipes steps)
            mRecipeStepsAdapter = new RecipeStepAdapter(null,this);

            // RecyclerViews expanded by default
            isStepsRecyclerViewExpanded = true;
            setRecyclerViewVisibility(isStepsRecyclerViewExpanded);

        }

        // Add Collapse/Expand clickListeners
        setCollapseViewClickListeners();

        // Update Status of Collapse/Expand Button
        updateStatusCollapseView();

        // Return rootView
        return rootView;

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        mListState = mRecipeStepsLayoutManager.onSaveInstanceState();
        outState.putParcelableArrayList(RECIPE_STEPS_SAVED_INST,
                (ArrayList<Step>) mRecipeStepsAdapter.getmRecipeSteps());
        outState.putBoolean(RECIPE_STEPS_EXPANDED_SAVED_INST, isStepsRecyclerViewExpanded);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_SAVED_INST);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListState != null) {
            mRecipeStepsLayoutManager.onRestoreInstanceState(mListState);
        }
    }


    //--------------------------------------------------------------------------------|
    //                               Getters/Setters                                  |
    //--------------------------------------------------------------------------------|

    public void setmRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public void setmRecipeStepClickListener(RecipeStepClickListener mRecipeStepClickListener) {
        this.mRecipeStepClickListener = mRecipeStepClickListener;
    }

    public void setRecipeSteps(List<Step> recipeSteps) {
        mRecipeStepsAdapter.setmRecipeSteps(checkRecipeStepsConsistency(recipeSteps));
        mRecipeStepsRecyclerView.setAdapter(mRecipeStepsAdapter);
        mRecipeStepsAdapter.notifyDataSetChanged();
    }


    //--------------------------------------------------------------------------------|
    //                                 Public Methods                                 |
    //--------------------------------------------------------------------------------|

    /**
     * Set Recipe Step RecyclerView visibility.
     *
     * @param isExpanded        RecyclerView¨s expand/collapse state
     */
    public void setRecyclerViewVisibility(boolean isExpanded) {
        if(isExpanded) {
            ViewUtils.expandView(mRecipeStepsRecyclerView);
        } else {
            ViewUtils.collapseView(mRecipeStepsRecyclerView);
        }
    }

    /**
     * Set RecyclerView's LayoutManager.
     * Different layouts for portrait and landscape mode
     *
     * @param   rootView    Activity view
     */
    private void setRecyclerViewLayoutManager(View rootView) {
        if(tabletSize) {
            mRecipeStepsLayoutManager = new GridLayoutManager(rootView.getContext(),
                    BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
        } else {
            switch (this.getResources().getConfiguration().orientation) {
                case BakingAppGlobals.LANDSCAPE_VIEW: // Landscape Mode
                    mRecipeStepsLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                    break;
                case BakingAppGlobals.PORTRAIT_VIEW: // Portrait Mode
                default:
                    mRecipeStepsLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                    break;
            }
        }
        mRecipeStepsRecyclerView.setLayoutManager(mRecipeStepsLayoutManager);
    }


    //--------------------------------------------------------------------------------|
    //                            Adapter --> Fragment Comm                           |
    //--------------------------------------------------------------------------------|

    @Override
    public void onRecipeStepClick(int position) {
        if(tabletSize) {
            if(mRecipeStepClickListener != null) {
                mRecipeStepClickListener.onRecipeStepClick(position);
            }
        } else {
            Intent i = new Intent(mContext, RecipeStepDetailViewActivity.class);
            ArrayList<Step> recipeSteps = (ArrayList<Step>) mRecipeStepsAdapter.getmRecipeSteps();
            if(recipeSteps != null) {
                i.putExtra(RECIPE_NAME_EXTRA, mRecipeName);
                i.putParcelableArrayListExtra(RECIPE_STEP_EXTRA, recipeSteps);
                i.putExtra(RECIPE_STEP_POSITION_EXTRA, position);
            }
            this.startActivity(i);
        }
    }


    //--------------------------------------------------------------------------------|
    //                                Private Methods                                 |
    //--------------------------------------------------------------------------------|

    /**
     * Checks coherency of recipe steps (in order, no missing ones,...).
     *
     * @param recipeSteps   List of recipe steps - List<Step>
     * @return              List of adapted recipe steps - List<Step>
     */
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

    /**
     * Adapt the onClickListeners according to RecyclerView¨s expand/collapse state.
     */
    private void setCollapseViewClickListeners() {

        // Set Expand/Collapse OnClick action for Steps RecyclerView
        mCollapseStepsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStepsRecyclerViewExpanded = !isStepsRecyclerViewExpanded;
                setRecyclerViewVisibility(isStepsRecyclerViewExpanded);
                if(isStepsRecyclerViewExpanded) {
                    mCollapseStepsImageView.setImageResource(R.mipmap.ic_collapse);
                } else {
                    mCollapseStepsImageView.setImageResource(R.mipmap.ic_expand);
                }
            }
        });

    }

    /**
     * Update the UIs according to the expand/collapse state.
     */
    private void updateStatusCollapseView() {
        // Handle expansion / collapse of RecyclerViews
        if(isStepsRecyclerViewExpanded) {
            mCollapseStepsImageView.setImageResource(R.mipmap.ic_collapse);
        } else {
            mCollapseStepsImageView.setImageResource(R.mipmap.ic_expand);
        }
    }

    //--------------------------------------------------------------------------------|
    //                             Fragment --> Activity Comm                         |
    //--------------------------------------------------------------------------------|

    public interface RecipeStepClickListener {
        public void onRecipeStepClick(int position);
    }

}
