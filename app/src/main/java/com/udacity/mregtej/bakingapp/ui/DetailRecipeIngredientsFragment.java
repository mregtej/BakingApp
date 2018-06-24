package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
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
import com.udacity.mregtej.bakingapp.datamodel.Ingredient;
import com.udacity.mregtej.bakingapp.global.BakingAppGlobals;
import com.udacity.mregtej.bakingapp.ui.adapter.RecipeIngredientAdapter;
import com.udacity.mregtej.bakingapp.ui.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailRecipeIngredientsFragment extends Fragment {

    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mRecipeIngredientsLayoutManager;
    /** Recipe Ingredients Custom ArrayAdapter */
    private RecipeIngredientAdapter mRecipeIngredientsAdapter;
    /** Custom Recipe Card GridView (RecyclerView) */
    @BindView(R.id.recipe_ingredients_recyclerview) RecyclerView mRecipeIngredientsRecyclerView;
    /** Activity Context */
    private Context mContext;

    private View rootView;

    public DetailRecipeIngredientsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Bind Views
        rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        // Create RecipeCardAdapter (null recipes)
        mRecipeIngredientsAdapter = new RecipeIngredientAdapter(null);

        // Load & set GridLayout
        mRecipeIngredientsRecyclerView.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        // Return rootView
        return rootView;

    }

    public void setRecipeIngredients(List<Ingredient> ingredients) {
        mRecipeIngredientsAdapter.setmIngredients(ingredients);
        mRecipeIngredientsRecyclerView.setAdapter(mRecipeIngredientsAdapter);
        mRecipeIngredientsAdapter.notifyDataSetChanged();
    }

    public void setRecyclerViewVisibility(boolean isExpanded) {
        if(isExpanded) {
            ViewUtils.collapseView(rootView);
        } else {
            ViewUtils.expandView(rootView);
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
                mRecipeIngredientsLayoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        BakingAppGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                break;
            case BakingAppGlobals.PORTRAIT_VIEW: // Portrait Mode
            default:
                mRecipeIngredientsLayoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                break;
        }
        mRecipeIngredientsRecyclerView.setLayoutManager(mRecipeIngredientsLayoutManager);
    }

}