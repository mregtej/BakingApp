package com.udacity.mregtej.bakingapp.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.mregtej.bakingapp.application.BakingAppExecutors;
import com.udacity.mregtej.bakingapp.database.RecipeDao;
import com.udacity.mregtej.bakingapp.database.RecipeDatabase;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeContentProvider extends ContentProvider {

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** The match code for some items in the Recipe table. */
    private static final int CODE_RECIPE_DIR = 1;

    /** The match code for a recipe in the Recipe table. */
    private static final int CODE_RECIPE_ITEM = 2;


    //--------------------------------------------------------------------------------|
    //                                 Parameters                                     |
    //--------------------------------------------------------------------------------|

    /** The URI matcher. */
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        MATCHER.addURI(RecipeContract.AUTHORITY, RecipeContract.RecipeEntry.TABLE_NAME, CODE_RECIPE_DIR);
        MATCHER.addURI(RecipeContract.AUTHORITY, RecipeContract.RecipeEntry.TABLE_NAME + "/#", CODE_RECIPE_ITEM);
    }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final int code = MATCHER.match(uri);
        if (code == CODE_RECIPE_DIR || code == CODE_RECIPE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            RecipeDao recipeDao = RecipeDatabase.getInstance(context,
                    new BakingAppExecutors()).recipeDao();
            final Cursor cursor;
            if (code == CODE_RECIPE_DIR) {
                cursor = recipeDao.getRecipesViaCP();
            } else {
                cursor = recipeDao.findRecipeById(ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_RECIPE_DIR:
                return "vnd.android.cursor.dir/" + RecipeContract.AUTHORITY + "."
                        + RecipeContract.RecipeEntry.TABLE_NAME;
            case CODE_RECIPE_ITEM:
                return "vnd.android.cursor.item/" + RecipeContract.AUTHORITY + "."
                        + RecipeContract.RecipeEntry.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_RECIPE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                final long id = RecipeDatabase.getInstance(context,
                        new BakingAppExecutors()).recipeDao().insertRecipe(Recipe.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            case CODE_RECIPE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_RECIPE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_RECIPE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = RecipeDatabase.getInstance(context,
                        new BakingAppExecutors()).recipeDao().deleteRecipeById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_RECIPE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);
            case CODE_RECIPE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final Recipe recipe = Recipe.fromContentValues(values);
                recipe.setId((int)ContentUris.parseId(uri));
                final int count = RecipeDatabase.getInstance(context,
                        new BakingAppExecutors()).recipeDao().updateRecipe(recipe);
                context.getContentResolver().notifyChange(uri, null);
                return count;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }


    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final Context context = getContext();
        if (context == null) {
            return new ContentProviderResult[0];
        }
        final RecipeDatabase database = RecipeDatabase.getInstance(context,
                new BakingAppExecutors());
        database.beginTransaction();
        try {
            final ContentProviderResult[] result = super.applyBatch(operations);
            database.setTransactionSuccessful();
            return result;
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (MATCHER.match(uri)) {
            case CODE_RECIPE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final RecipeDatabase database = RecipeDatabase.getInstance(context,
                        new BakingAppExecutors());
                final List<Recipe> recipes = new ArrayList<>(values.length);
                for (int i = 0; i < values.length; i++) {
                    recipes.add(Recipe.fromContentValues(values[i]));
                }
                return database.recipeDao().insertRecipes(recipes).length;
            case CODE_RECIPE_ITEM:
                throw new IllegalArgumentException("Invalid URI, cannot insert with ID: " + uri);
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

}