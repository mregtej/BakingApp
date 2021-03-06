package com.udacity.mregtej.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
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

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.comm.ConnectivityHandler;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.global.BakingAppGlobals;
import com.udacity.mregtej.bakingapp.ui.adapter.RecipeCardAdapter;
import com.udacity.mregtej.bakingapp.ui.dialog.AlertDialogHelper;
import com.udacity.mregtej.bakingapp.ui.widget.BakingAppWidgetService;
import com.udacity.mregtej.bakingapp.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivityFragment extends Fragment
        implements RecipeCardAdapter.RecipeCardClickListener {

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Key for storing the list state in savedInstanceState */
    private static final String LIST_STATE_KEY = "list-state";
    /** Key for storing the recipe list in savedInstanceState */
    private static final String RECIPE_LIST_KEY = "recipe-list";
    /** Key for storing the recipe list in Intent.Extras (Bundle) */
    private static final String RECIPE_EXTRA = "recipe";


    //--------------------------------------------------------------------------------|
    //                                 Parameters                                     |
    //--------------------------------------------------------------------------------|

    /** RecyclerView LayoutManager instance */
    private RecyclerView.LayoutManager mRecipeCardLayoutManager;
    /** List state stored in savedInstanceState */
    private Parcelable mListState;
    /** Recipe ViewModel instance */
    private RecipeViewModel mRecipeViewModel;
    /** Recipe Card Custom ArrayAdapter */
    private RecipeCardAdapter mRecipeCardAdapter;
    /** Activity Context */
    private Context mContext;
    /** Tablet size flag (App Tablet version) */
    private boolean tabletSize;

    /** Custom Recipe Card GridView (RecyclerView) */
    @BindView(R.id.recipe_card_recyclerview) RecyclerView mRecipeCardRecyclerView;


    //--------------------------------------------------------------------------------|
    //                                 Constructor                                    |
    //--------------------------------------------------------------------------------|

    public MainActivityFragment() { }


    //--------------------------------------------------------------------------------|
    //                             Override Methods                                   |
    //--------------------------------------------------------------------------------|

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_activity_main, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();
        tabletSize = getResources().getBoolean(R.bool.isTablet);

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
                List<Recipe> recipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);

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
        super.onSaveInstanceState(outState);
        mListState = mRecipeCardLayoutManager.onSaveInstanceState();
        outState.putParcelableArrayList(RECIPE_LIST_KEY,
                (ArrayList<Recipe>)mRecipeCardAdapter.getmRecipeList());
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
            mRecipeCardLayoutManager.onRestoreInstanceState(mListState);
        }
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

        if(tabletSize) {
            switch (this.getResources().getConfiguration().orientation) {
                case BakingAppGlobals.LANDSCAPE_VIEW: // Landscape Mode
                    mRecipeCardLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_TABLET_LAND_COLUMN_NUMB);
                    break;
                case BakingAppGlobals.PORTRAIT_VIEW: // Portrait Mode
                default:
                    mRecipeCardLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_TABLET_PORT_COLUMN_NUMB);
                    break;
            }
        } else {
            switch (this.getResources().getConfiguration().orientation) {
                case BakingAppGlobals.LANDSCAPE_VIEW: // Landscape Mode
                    mRecipeCardLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_LAND_COLUMN_NUMB);
                    break;
                case BakingAppGlobals.PORTRAIT_VIEW: // Portrait Mode
                default:
                    mRecipeCardLayoutManager = new GridLayoutManager(rootView.getContext(),
                            BakingAppGlobals.RECIPE_GV_PORT_COLUMN_NUMB);
                    break;
            }
        }
        mRecipeCardRecyclerView.setLayoutManager(mRecipeCardLayoutManager);
    }


    //--------------------------------------------------------------------------------|
    //                               DataModel Methods                                |
    //--------------------------------------------------------------------------------|

    /**
     * Registers the View into Recipe ViewModel to receive updated recipes (Observer pattern)
     */
    private void registerToRecipeModel() {
        // Create RecipeViewModel Factory for param injection
        RecipeViewModel.Factory recipeFactory = new RecipeViewModel.Factory(
                getActivity().getApplication());
        // Get instance of RecipeViewModel
        mRecipeViewModel = ViewModelProviders.of(this, recipeFactory)
                .get(RecipeViewModel.class);
    }

    /**
     * Retrieve list of recipes from Recipe ViewModel (Observer pattern)
     *
     * TODO Implement a user-friendly data-refresh mechanism (e.g. scrolling down on the main screen)
     */
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
        Intent i;
        if(tabletSize) {
            i = new Intent(mContext, DetailRecipeTabletActivity.class);
        } else {
            i = new Intent(mContext, DetailRecipeActivity.class);
        }
        Recipe recipe = mRecipeCardAdapter.getRecipeByPosition(position);
        if(recipe != null) {
            i.putExtra(RECIPE_EXTRA, recipe);
        }
        // Update the Widget info
        // TODO store last opened recipe in OnSharedPreferences and implement Broacast to update the widget
        BakingAppWidgetService.startActionUpdateRecipe(mContext, recipe.getId());
        this.startActivity(i);
    }

}
