package com.udacity.mregtej.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.udacity.mregtej.bakingapp.application.BakingAppExecutors;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;

import java.util.List;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "recipe-database";

    public abstract RecipeDao recipeDao();
    private static BakingAppExecutors mExecutors;
    private static RecipeDatabase INSTANCE;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static RecipeDatabase getInstance(final Context context,
                                                         final BakingAppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (RecipeDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = buildDatabase(context.getApplicationContext(), executors);
                    INSTANCE.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    /**
     * Build the database. {@link Builder#build()} only sets up the database configuration and
     * creates a new instance of the database.
     * The SQLite database is only created when it's accessed for the first time.
     */
    private static RecipeDatabase buildDatabase(final Context appContext,
                                                            final BakingAppExecutors executors) {
        mExecutors = executors;
        return Room.databaseBuilder(appContext, RecipeDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                            // Add a delay to simulate a long-running operation
                            addDelay();
                            // Generate the data for pre-population
                            RecipeDatabase database = RecipeDatabase.
                                    getInstance(appContext, executors);
                            // notify that the database was created and it's ready to be used
                            database.setDatabaseCreated();
                        });
                    }
                }).build();
    }

    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        mIsDatabaseCreated.postValue(true);
    }

    private static void insertData(final RecipeDatabase database,
                                   final List<Recipe> recipeList) {
        database.runInTransaction(() -> {
            database.recipeDao().insertRecipes(recipeList);
        });
    }

    private static void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

}
