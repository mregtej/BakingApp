package com.udacity.mregtej.bakingapp.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.provider.RecipeContract;
import com.udacity.mregtej.bakingapp.ui.DetailRecipeActivity;
import com.udacity.mregtej.bakingapp.ui.MainActivity;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    //--------------------------------------------------------------------------------|
    //                             Override Methods                                   |
    //--------------------------------------------------------------------------------|

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) { }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }


    //--------------------------------------------------------------------------------|
    //                               Public Methods                                   |
    //--------------------------------------------------------------------------------|

    /**
     * Updates all BakingApp Widgets.
     *
     * @param context               App context
     * @param appWidgetManager      AppWidgetManager instance
     * @param recipe                Recipe (DataModel)
     * @param appWidgetIds          Array of AppWidgetIds
     */
    static void updateBakingAppWidgets(Context context, AppWidgetManager appWidgetManager,
                                       Recipe recipe, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    /**
     * Updates a single BakingApp Widget.
     *
     * @param context               App context
     * @param appWidgetManager      AppWidgetManager instance
     * @param recipe                Recipe (DataModel)
     * @param appWidgetId           AppWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe,
                                int appWidgetId) {

        // Construct the RemoteViews
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_bakingapp_layout);

        // Set Recipe Title on RemoteView
        remoteViews.setTextViewText(R.id.tv_widget_recipe_name, recipe.getName());

        // Set Recipe Ingredients on RemoteView
        setIngredientsGridRemoteView(context, recipe.getId(), remoteViews);

        // Set Widget OnClick handler
        setWidgetOnClick(context, recipe, remoteViews);

        // Update the Widget via WidgetManager
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }


    //--------------------------------------------------------------------------------|
    //                               Private Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Sets the RecipeIngredients RemoteView.
     *
     * @param context       App context
     * @param recipe_id     Recipe Id
     * @param remoteViews   Widget RemoteView
     */
    private static void setIngredientsGridRemoteView(Context context, int recipe_id,
                                                     RemoteViews remoteViews) {
        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent gridIntent = new Intent(context, BakingAppListWidgetIngredientsService.class);
        gridIntent.putExtra(BakingAppWidgetService.EXTRA_RECIPE_ID, recipe_id);
        remoteViews.setRemoteAdapter(R.id.gv_widget_recipe_ingredients, gridIntent);
        // Handle empty gardens
        remoteViews.setEmptyView(R.id.gv_widget_recipe_ingredients, R.id.rl_empty_recipe_view);
    }

    /**
     * Sets the Widget OnClickListener RemoteView.
     *
     * @param context       App context
     * @param recipe        Recipe (DataModel)
     * @param remoteViews   Widget RemoteView
     */
    private static void setWidgetOnClick(Context context, Recipe recipe, RemoteViews remoteViews) {

        // Set the click handler to open the DetailActivity for recipe ID,
        // or the MainActivity if recipe ID is invalid
        Intent intent;
        int recipe_id = recipe.getId();
        if (recipe_id == RecipeContract.INVALID_RECIPE_ID) {
            intent = new Intent(context, MainActivity.class);
        } else { // Set on click to open the corresponding detail activity
            Log.d(BakingAppWidgetProvider.class.getSimpleName(), "recipeID=" + recipe_id);
            intent = new Intent(context, DetailRecipeActivity.class);
            intent.putExtra(DetailRecipeActivity.RECIPE_EXTRA, recipe);
        }

        // Set OnClickPendingIntent to open MainActivity
        Intent bakingAppIntent = new Intent(context, BakingAppWidgetService.class);
        bakingAppIntent.setAction(BakingAppWidgetService.ACTION_UPDATE_RECIPE);
        bakingAppIntent.putExtra(BakingAppWidgetService.EXTRA_RECIPE_ID, recipe.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_widget_recipe, pendingIntent);

    }

}
