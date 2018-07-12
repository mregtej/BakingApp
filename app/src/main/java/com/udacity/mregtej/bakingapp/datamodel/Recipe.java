package com.udacity.mregtej.bakingapp.datamodel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.udacity.mregtej.bakingapp.database.RecipeConverters;

import java.util.List;

@Entity(tableName = "recipe")
public class Recipe implements Parcelable {

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private Integer id;
    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name;
    @TypeConverters(RecipeConverters.class)
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @TypeConverters(RecipeConverters.class)
    @SerializedName("steps")
    @Expose
    private List<Step> steps = null;
    @ColumnInfo(name = "servings")
    @SerializedName("servings")
    @Expose
    private Integer servings;
    @ColumnInfo(name = "image")
    @SerializedName("image")
    @Expose
    private String image;

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

}

