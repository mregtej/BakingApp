package com.udacity.mregtej.bakingapp.datamodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.udacity.mregtej.bakingapp.database.RecipeConverters;
import com.udacity.mregtej.bakingapp.provider.RecipeContract;

import java.util.List;

@Entity(tableName = RecipeContract.RecipeEntry.TABLE_NAME)
public class Recipe implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(index = true, name = RecipeContract.RecipeEntry.COLUMN_ID)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_ID)
    @Expose
    private Integer id;
    @ColumnInfo(name = RecipeContract.RecipeEntry.COLUMN_NAME)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_NAME)
    @Expose
    private String name;
    @TypeConverters(RecipeConverters.class)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_INGREDIENTS)
    @Expose
    private List<Ingredient> ingredients = null;
    @TypeConverters(RecipeConverters.class)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_STEPS)
    @Expose
    private List<Step> steps = null;
    @ColumnInfo(name = RecipeContract.RecipeEntry.COLUMN_SERVINGS)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_SERVINGS)
    @Expose
    private Integer servings;
    @ColumnInfo(name = RecipeContract.RecipeEntry.COLUMN_IMAGE)
    @SerializedName(RecipeContract.RecipeEntry.COLUMN_IMAGE)
    @Expose
    private String image;

    public Recipe() {}

    public Recipe(Integer id, String name, List<Ingredient> ingredients, List<Step> steps,
                  Integer servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Recipe(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.ingredients = in.readArrayList(Ingredient.class.getClassLoader());
        this.steps = in.readArrayList(Step.class.getClassLoader());
        this.servings = in.readInt();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeList(this.ingredients);
        dest.writeList(this.steps);
        dest.writeInt(this.servings);
        dest.writeString(this.image);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public Integer getServings() {
        return servings;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public static Recipe fromContentValues(ContentValues values) {
        final Recipe recipe = new Recipe();
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_ID)) {
            recipe.id = values.getAsInteger(RecipeContract.RecipeEntry.COLUMN_ID);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_NAME)) {
            recipe.name = values.getAsString(RecipeContract.RecipeEntry.COLUMN_NAME);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_SERVINGS)) {
            recipe.servings = values.getAsInteger(RecipeContract.RecipeEntry.COLUMN_SERVINGS);
        }
        if (values.containsKey(RecipeContract.RecipeEntry.COLUMN_IMAGE)) {
            recipe.image = values.getAsString(RecipeContract.RecipeEntry.COLUMN_IMAGE);
        }
        return recipe;
    }
}

