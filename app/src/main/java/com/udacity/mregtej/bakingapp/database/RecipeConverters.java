package com.udacity.mregtej.bakingapp.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.mregtej.bakingapp.datamodel.Ingredient;
import com.udacity.mregtej.bakingapp.datamodel.Step;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RecipeConverters {

    static Gson gson = new Gson();

    @TypeConverter
    public static List<Ingredient> stringToIngredientList(String ingredients) {
        if(ingredients == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Ingredient>>() {}.getType();
        return gson.fromJson(ingredients, listType);
    }

    @TypeConverter
    public static String ingredientListToString(List<Ingredient> ingredients) {
        return gson.toJson(ingredients);
    }

    @TypeConverter
    public static List<Step> stringToStepList(String steps) {
        if(steps == null) {
            return Collections.emptyList();
        }
        Type listType = new TypeToken<List<Step>>() {}.getType();
        return gson.fromJson(steps, listType);
    }

    @TypeConverter
    public static String stepListToString(List<Step> steps) {
        return gson.toJson(steps);
    }

}
