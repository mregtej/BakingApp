package com.udacity.mregtej.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.mregtej.bakingapp.datamodel.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> getRecipes();

    @Query("SELECT * FROM recipe where name LIKE :name")
    Recipe findRecipeByName(String name);

    @Query("SELECT COUNT(*) from recipe")
    int countRecipes();

    @Update
    void updateRecipes(List<Recipe> recipes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipe> recipes);

    @Delete
    void deleteRecipe(Recipe recipe);
}
