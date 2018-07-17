package com.udacity.mregtej.bakingapp.ui.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.database.RecipeConverters;
import com.udacity.mregtej.bakingapp.datamodel.Ingredient;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.datamodel.Step;
import com.udacity.mregtej.bakingapp.provider.RecipeContract;

import java.util.ArrayList;
import java.util.List;

public class BakingAppWidgetService extends IntentService {

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Update Recipe Service Action */
    public static final String ACTION_UPDATE_RECIPE =
            "com.udacity.mregtej.bakingapp.ui.widget.action.update_recipe";
    /** Recipe ID Extra Intent parameter */
    public static final String EXTRA_RECIPE_ID =
            "com.udacity.mregtej.bakingapp.ui.widget.extra.recipe_id";

    //--------------------------------------------------------------------------------|
    //                                 Constructor                                    |
    //--------------------------------------------------------------------------------|

    public BakingAppWidgetService() {
        super(BakingAppWidgetService.class.getName());
    }


    //--------------------------------------------------------------------------------|
    //                              Public Methods                                    |
    //--------------------------------------------------------------------------------|

    /**
     * Triggers the action for updating the recipe info on widget.
     *
     * @param context       App context
     * @param recipeId      Recipe Id
     */
    public static void startActionUpdateRecipe(Context context, int recipeId) {
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        context.startService(intent);
    }


    //--------------------------------------------------------------------------------|
    //                            Override Methods                                    |
    //--------------------------------------------------------------------------------|

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE.equals(action)) {
                handleActionUpdateRecipe(intent.getIntExtra(EXTRA_RECIPE_ID,
                        RecipeContract.INVALID_RECIPE_ID));
            }
        }
    }

    //--------------------------------------------------------------------------------|
    //                              Private Methods                                   |
    //--------------------------------------------------------------------------------|

    /**
     * Handles the action for updating the recipe info on widget.
     *
     * @param recipe_id        Recipe Id
     */
    private void handleActionUpdateRecipe(int recipe_id) {
        // Build Query to get last visited Recipe ID
        Uri SINGLE_RECIPE_URI = ContentUris.withAppendedId(RecipeContract.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(RecipeContract.RECIPE_PATH).build(), recipe_id);
        // Query last visited Recipe ID
        Cursor cursor = getContentResolver().query(
                SINGLE_RECIPE_URI,
                null,
                null,
                null,
                null
        );
        // Extract the recipe details
        int recipeId = RecipeContract.INVALID_RECIPE_ID;
        String recipeName = getString(R.string.recipe_title);
        List<Ingredient> recipeIngredients = new ArrayList<>();
        List<Step> recipeSteps = new ArrayList<>();
        int recipeServings = RecipeContract.INVALID_RECIPE_SERVINGS;
        String recipeImage = "";
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int recipeIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_ID);
            int recipeNameIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME);
            int recipeIngredientsIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS);
            int recipeStepsIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_STEPS);
            int recipeServingsIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SERVINGS);
            int recipeImageIndex = cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE);
            recipeId = cursor.getInt(recipeIndex);
            recipeName = cursor.getString(recipeNameIndex);
            recipeIngredients = RecipeConverters.stringToIngredientList(cursor.getString(recipeIngredientsIndex));
            recipeSteps = RecipeConverters.stringToStepList(cursor.getString(recipeStepsIndex));
            recipeServings = cursor.getInt(recipeServingsIndex);
            recipeImage = cursor.getString(recipeImageIndex);
            cursor.close();
        }
        // Build Recipe ModelObject
        Recipe recipe = new Recipe(recipeId, recipeName, recipeIngredients, recipeSteps, recipeServings, recipeImage);
        // Now update all widgets
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        // Notifies the changes on widget data (for updating the Recipe Ingredients GridRemoteView
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.gv_widget_recipe_ingredients);
        // updateAppWidget and pass recipe ingredients
        BakingAppWidgetProvider.updateBakingAppWidgets(this, appWidgetManager, recipe, appWidgetIds);

    }

}
