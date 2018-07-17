package com.udacity.mregtej.bakingapp.ui.widget;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.database.RecipeConverters;
import com.udacity.mregtej.bakingapp.datamodel.Ingredient;
import com.udacity.mregtej.bakingapp.provider.RecipeContract;

import java.util.List;

/**
 * RemoteViewsService for updating the list of Recipe Ingredients (GridView)
 */
public class BakingAppListWidgetIngredientsService extends RemoteViewsService{

    //--------------------------------------------------------------------------------|
    //                             Override Methods                                   |
    //--------------------------------------------------------------------------------|

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListIngredientsRemoteViewsFactory(this.getApplicationContext(),
                intent.getIntExtra(BakingAppWidgetService.EXTRA_RECIPE_ID, RecipeContract.INVALID_RECIPE_ID));
    }

}

/**
 * GridView RemoteViewsFactory (Adapter class)
 */
class ListIngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    //--------------------------------------------------------------------------------|
    //                                 Parameters                                     |
    //--------------------------------------------------------------------------------|

    /** Recipe Id */
    int mRecipeId;
    /** Application Context */
    Context mContext;
    /** Recipe Cursor (Data retrieved from ContentProvider) */
    Cursor mCursor;
    /** List of Recipe Ingredients */
    List<Ingredient> mRecipeIngredients;


    //--------------------------------------------------------------------------------|
    //                                 Constructor                                    |
    //--------------------------------------------------------------------------------|

    public ListIngredientsRemoteViewsFactory(Context applicationContext, int recipe_id) {
        mContext = applicationContext;
        mRecipeId = recipe_id;
    }


    //--------------------------------------------------------------------------------|
    //                               Override Methods                                 |
    //--------------------------------------------------------------------------------|

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        // Build Query to get last visited Recipe ID
        Uri SINGLE_RECIPE_URI = ContentUris.withAppendedId(RecipeContract.BASE_CONTENT_URI
                .buildUpon()
                .appendPath(RecipeContract.RECIPE_PATH).build(), mRecipeId);
        // Query last visited Recipe ID
        if(mCursor != null) { mCursor.close(); }
        mCursor = mContext.getContentResolver().query(
                SINGLE_RECIPE_URI,
                null,
                null,
                null,
                null
        );
        // Check if cursor is not empty and retrieve list of ingredients
        if (mCursor == null || mCursor.getCount() == 0) return;
        mCursor.moveToFirst();
        int recipeIngredientsIndex = mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS);
        mRecipeIngredients = RecipeConverters.stringToIngredientList(mCursor.getString(recipeIngredientsIndex));
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        if (mRecipeIngredients == null || mRecipeIngredients.size() == 0) return 0;
        return mRecipeIngredients.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {

        if (mRecipeIngredients == null || mRecipeIngredients.size() == 0) return null;

        // Load GridViewItem layout
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_bakingapp_ingredient_item_layout);

        // Populate UI elements of a GridViewItem
        views.setTextViewText(R.id.tv_widget_ingredient_name, mRecipeIngredients.get(position).getIngredient());
        views.setTextViewText(R.id.tv_widget_ingredient_quantity, String.valueOf(mRecipeIngredients.get(position).getQuantity()));
        views.setTextViewText(R.id.tv_widget_ingredient_measure, String.valueOf(mRecipeIngredients.get(position).getMeasure()));

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
