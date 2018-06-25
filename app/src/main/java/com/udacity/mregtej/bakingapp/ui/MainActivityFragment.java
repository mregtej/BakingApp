package com.udacity.mregtej.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
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
import com.udacity.mregtej.bakingapp.comm.ConnectivityHandler;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.global.BakingAppGlobals;
import com.udacity.mregtej.bakingapp.ui.adapter.RecipeCardAdapter;
import com.udacity.mregtej.bakingapp.ui.dialog.AlertDialogHelper;
import com.udacity.mregtej.bakingapp.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment
        implements RecipeCardAdapter.RecipeCardClickListener {

    private static final String RECIPE_LIST_SAVED_INST = "recipe-list";

    private static final String RECIPE_EXTRA = "recipe";

    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mRecipeCardLayoutManager;
    /** Custom Recipe Card GridView (RecyclerView) */
    @BindView(R.id.recipe_card_recyclerview) RecyclerView mRecipeCardRecyclerView;
    /** Recipe ViewModel instance */
    private RecipeViewModel mRecipeViewModel;
    /** Recipe Card Custom ArrayAdapter */
    private RecipeCardAdapter mRecipeCardAdapter;
    /** Activity Context */
    private Context mContext;

    public MainActivityFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_activity_main, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        // Check network connectivity
        if (!ConnectivityHandler.isConnected(getActivity())) {
            displayConnectivityAlertDialog();
        }

        else {

            // Load & set GridLayout
            mRecipeCardRecyclerView.setHasFixedSize(true);
            setRecyclerViewLayoutManager(rootView);

            if(savedInstanceState != null) {

                // Retrieve recipes from savedInstanceState
                List<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST_SAVED_INST);

                // Create RecipeCardAdapter (with recipes)
                mRecipeCardAdapter = new RecipeCardAdapter(recipes, this);

                // Set Adapter and notifyDataSetChanged
                mRecipeCardRecyclerView.setAdapter(mRecipeCardAdapter);
                mRecipeCardAdapter.notifyDataSetChanged();

            } else {

                // Create RecipeCardAdapter (null recipes)
                mRecipeCardAdapter = new RecipeCardAdapter(null, this);

                // Subscribe MainActivityFragment to receive notifications from RecipeViewModel
                registerToRecipeModel();

                // Get recipe list from RecipeViewModel
                getRecipes();

            }
        }

        // Return rootView
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        ArrayList<Recipe> recipes = (ArrayList<Recipe>)mRecipeCardAdapter.getmRecipeList();
        outState.putParcelableArrayList(RECIPE_LIST_SAVED_INST, recipes);
    }

    //--------------------------------------------------------------------------------|
    //                               UI View Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Display an AlertDialog to warn user that there is no Internet connectivity
     */
    private void displayConnectivityAlertDialog() {

        DialogInterface.OnClickListener okListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(
                        android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        };

        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent
                        (android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS));
            }
        };

        DialogInterface.OnClickListener neutralListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getActivity().finish();
            }
        };

        DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActivity().finish();
            }
        };

        AlertDialogHelper.createMessage(
                mContext,
                this.getResources().getString(R.string.network_failure),
                this.getResources().getString(R.string.network_user_choice),
                this.getResources().getString(R.string.network_user_choice_wifi),
                this.getResources().getString(R.string.network_user_choice_3g),
                this.getResources().getString(R.string.network_user_choice_no),
                okListener, cancelListener, neutralListener, dismissListener,
                true
        ).show();
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

    private void registerToRecipeModel() {
        // Create RecipeViewModel Factory for param injection
        RecipeViewModel.Factory recipeFactory = new RecipeViewModel.Factory(
                getActivity().getApplication());
        // Get instance of RecipeViewModel
        mRecipeViewModel = ViewModelProviders.of(this, recipeFactory)
                .get(RecipeViewModel.class);
    }

    private void getRecipes(){
        mRecipeViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes == null || recipes.isEmpty()) { return; }
                else {
                    mRecipeCardAdapter.setmRecipeList(recipes);
                    mRecipeCardRecyclerView.setAdapter(mRecipeCardAdapter);
                    mRecipeCardAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    //--------------------------------------------------------------------------------|
    //                           Adapter --> Fragment comm                            |
    //--------------------------------------------------------------------------------|

    @Override
    public void OnRecipeCardClick(int position) {
        Intent i = new Intent(mContext, DetailRecipeActivity.class);
        Recipe recipe = mRecipeCardAdapter.getRecipeByPosition(position);
        if(recipe != null) {
            i.putExtra(RECIPE_EXTRA, recipe);
        }
        this.startActivity(i);
    }
}
