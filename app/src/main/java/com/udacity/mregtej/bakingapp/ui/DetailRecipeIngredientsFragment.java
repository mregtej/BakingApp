package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
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
import com.udacity.mregtej.bakingapp.datamodel.Ingredient;
import com.udacity.mregtej.bakingapp.global.BakingAppGlobals;
import com.udacity.mregtej.bakingapp.ui.adapter.RecipeIngredientAdapter;
import com.udacity.mregtej.bakingapp.ui.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeIngredientsFragment extends Fragment {

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Key for storing the list state in savedInstanceState */
    private static final String LIST_STATE_KEY = "list-state";
    /** Key for storing the recipe ingredients in savedInstanceState */
    private static final String RECIPE_INGREDIENTS_KEY = "ingredients";
    /** Key for storing the expand/collapse status of the recipe ingr. list in savedInstanceState */
    private static final String RECIPE_INGREDIENTS_EXPANDED_KEY = "is-ingredients-expanded";


    //--------------------------------------------------------------------------------|
    //                                 Parameters                                     |
    //--------------------------------------------------------------------------------|

    /** Flag for storing if ngredientsRecyclerView is expanded */
    private boolean isIngredientsRecyclerViewExpanded;
    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mRecipeIngredientsLayoutManager;
    /** Recipe Ingredients Custom ArrayAdapter */
    private RecipeIngredientAdapter mRecipeIngredientsAdapter;
    /** List state stored in savedInstanceState */
    private Parcelable mListState;
    /** Activity Context */
    private Context mContext;
    /** Tablet size flag (App Tablet version) */
    private boolean tabletSize;

    /** Custom Recipe Card GridView (RecyclerView) */
    @BindView(R.id.recipe_ingredients_recyclerview) RecyclerView mRecipeIngredientsRecyclerView;
    /** Expand/Collapse GridView Button (RecyclerView) */
    @BindView(R.id.ib_expand_collapse_ingredients_rv) ImageView mCollapseIngredientsImageView;


    //--------------------------------------------------------------------------------|
    //                                 Constructor                                    |
    //--------------------------------------------------------------------------------|

    public DetailRecipeIngredientsFragment() { }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();
        tabletSize = getResources().getBoolean(R.bool.isTablet);

        // Load & set GridLayout
        mRecipeIngredientsRecyclerView.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        if(savedInstanceState != null) {

            // Retrieve list of ingredients from savedInstanceState
            List<Ingredient> ingredients = savedInstanceState.
                    getParcelableArrayList(RECIPE_INGREDIENTS_KEY);
            isIngredientsRecyclerViewExpanded = savedInstanceState.
                    getBoolean(RECIPE_INGREDIENTS_EXPANDED_KEY);
            setRecyclerViewVisibility(isIngredientsRecyclerViewExpanded);

            // Create RecipeIngredientAdapter (with ingredients)
            mRecipeIngredientsAdapter = new RecipeIngredientAdapter(ingredients);

            // Set Adapter and notifyDataSetChanged
            mRecipeIngredientsRecyclerView.setAdapter(mRecipeIngredientsAdapter);
            mRecipeIngredientsAdapter.notifyDataSetChanged();

        } else {

            // Create RecipeIngredientAdapter (nulll ingredients)
            mRecipeIngredientsAdapter = new RecipeIngredientAdapter(null);

            // RecyclerViews expanded by default
            isIngredientsRecyclerViewExpanded = true;
            setRecyclerViewVisibility(isIngredientsRecyclerViewExpanded);

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
        super.onSaveInstanceState(outState);
        mListState = mRecipeIngredientsLayoutManager.onSaveInstanceState();
        outState.putParcelable(LIST_STATE_KEY, mListState);
        outState.putParcelableArrayList(RECIPE_INGREDIENTS_KEY,
                (ArrayList<Ingredient>) mRecipeIngredientsAdapter.getmIngredients());
        outState.putBoolean(RECIPE_INGREDIENTS_EXPANDED_KEY, isIngredientsRecyclerViewExpanded);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListState != null) {
            mRecipeIngredientsLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    //--------------------------------------------------------------------------------|
    //                               Getters / Setters                                |
    //--------------------------------------------------------------------------------|

    public void setRecipeIngredients(List<Ingredient> ingredients) {
        mRecipeIngredientsAdapter.setmIngredients(ingredients);
        mRecipeIngredientsRecyclerView.setAdapter(mRecipeIngredientsAdapter);
        mRecipeIngredientsAdapter.notifyDataSetChanged();
    }


    //--------------------------------------------------------------------------------|
    //                                  UI Methods                                    |
    //--------------------------------------------------------------------------------|

    /**
     * Expand / Collapse RecyclerView
     *
     * @param isExpanded    RecyclerView visible state
     */
    private void setRecyclerViewVisibility(boolean isExpanded) {
        if(isExpanded) {
            ViewUtils.expandView(mRecipeIngredientsRecyclerView);
        } else {
            ViewUtils.collapseView(mRecipeIngredientsRecyclerView);
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
            mRecipeIngredientsLayoutManager = new GridLayoutManager(rootView.getContext(),
                    BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
        } else {
            switch (this.getResources().getConfiguration().orientation) {
                case BakingAppGlobals.LANDSCAPE_VIEW: // Landscape Mode
                    mRecipeIngredientsLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                    break;
                case BakingAppGlobals.PORTRAIT_VIEW: // Portrait Mode
                default:
                    mRecipeIngredientsLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                    break;
            }
        }
        mRecipeIngredientsRecyclerView.setLayoutManager(mRecipeIngredientsLayoutManager);
    }

    /**
     * Set Expand/Collapse GridView Button listener
     */
    private void setCollapseViewClickListeners() {

        // Set Expand/Collapse OnClick action for Ingredients RecyclerView
        mCollapseIngredientsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isIngredientsRecyclerViewExpanded = !isIngredientsRecyclerViewExpanded;
                setRecyclerViewVisibility(isIngredientsRecyclerViewExpanded);
                if(isIngredientsRecyclerViewExpanded) {
                    mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_collapse);
                } else {
                    mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_expand);
                }
            }
        });

    }

    /**
     * Update UI status of Expand/Collapse GridView Button
     */
    private void updateStatusCollapseView() {
        // Handle expansion / collapse of RecyclerViews
        if(isIngredientsRecyclerViewExpanded) {
            mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_collapse);
        } else {
            mCollapseIngredientsImageView.setImageResource(R.mipmap.ic_expand);
        }
    }

}
