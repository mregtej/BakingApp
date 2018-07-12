package com.udacity.mregtej.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.provider.RecipeContract;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME)
    LiveData<List<Recipe>> getRecipes();

    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME)
    Cursor getRecipesViaCP();

    @Query("SELECT * FROM " + RecipeContract.RecipeEntry.TABLE_NAME +
            " WHERE " + RecipeContract.RecipeEntry.COLUMN_ID + " = :id")
    Cursor findRecipeById(long id);

    @Query("SELECT COUNT(*) FROM " + RecipeContract.RecipeEntry.TABLE_NAME)
    int countRecipes();

    @Update
    int updateRecipe(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRecipe(Recipe recipe);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertRecipes(List<Recipe> recipes);

    @Delete
    void deleteRecipe(Recipe recipe);

    @Query("DELETE FROM " + RecipeContract.RecipeEntry.TABLE_NAME +
            " WHERE " + RecipeContract.RecipeEntry.COLUMN_ID + " = :id")
    int deleteRecipeById(long id);

}
