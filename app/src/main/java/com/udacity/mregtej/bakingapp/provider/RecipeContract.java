package com.udacity.mregtej.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    /* Add content provider constants to the Contract
     Clients need to know how to access the Recipe data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the FavoriteMoviesEntry class
      */

    /** The authority of this content provider. */
    public static final String AUTHORITY = "com.udacity.mregtej.bakingapp.provider.provider";

    /** The Base Content URI */
    public static final Uri BASE_CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY);

    /** The Path for the Recipe directory */
    public static final String RECIPE_PATH = RecipeEntry.TABLE_NAME;

    /* TaskEntry is an inner class that defines the contents of the task table */
    public static final class RecipeEntry implements BaseColumns {

        // TaskEntry content URI = base content URI + path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();

        /** The name of the Recipe table. */
        public static final String TABLE_NAME = "recipes";

        /** The name of the recipe ID column. */
        public static final String COLUMN_ID = "id";

        /** The name of the recipe name column. */
        public static final String COLUMN_NAME = "name";

        /** The name of the recipe ingredients column. */
        public static final String COLUMN_INGREDIENTS = "ingredients";

        /** The name of the recipe steps column. */
        public static final String COLUMN_STEPS = "steps";

        /** The name of the recipe servings column. */
        public static final String COLUMN_SERVINGS = "servings";

        /** The name of the recipe image column. */
        public static final String COLUMN_IMAGE = "image";
    }

}


