package com.udacity.mregtej.bakingapp.ui;

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
import com.udacity.mregtej.bakingapp.global.BakingAppGlobals;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment {

    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mRecipeCardLayoutManager;
    /** Custom Recipe Card GridView (RecyclerView) */
    @BindView(R.id.recipe_card_recyclerview) RecyclerView mRecipeCardRecyclerView;

    public MainActivityFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_activity_main, container,
                false);
        ButterKnife.bind(this, rootView);

        // Load & set GridLayout
        mRecipeCardRecyclerView.setHasFixedSize(true);
        setRecyclerViewLayoutManager(rootView);

        // TODO Subscribes MainActivityFragment to receive notifications from RecipeViewModel

        // TODO Enable notifications for receiving updated recipe list from RecipeViewModel

        // Return rootView
        return rootView;
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
                mRecipeCardLayoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        BakingAppGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                break;
            case BakingAppGlobals.PORTRAIT_VIEW: // Portrait Mode
            default:
                mRecipeCardLayoutManager = new GridLayoutManager(
                        rootView.getContext(),
                        BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                break;
        }
        mRecipeCardRecyclerView.setLayoutManager(mRecipeCardLayoutManager);
    }

}
