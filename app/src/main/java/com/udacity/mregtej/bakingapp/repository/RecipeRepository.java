package com.udacity.mregtej.bakingapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.udacity.mregtej.bakingapp.application.BakingAppExecutors;
import com.udacity.mregtej.bakingapp.database.RecipeDatabase;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.rest.RecipeApiClient;
import com.udacity.mregtej.bakingapp.rest.RecipeApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository {

    //--------------------------------------------------------------------------------|
    //                                Constants                                       |
    //--------------------------------------------------------------------------------|

    private static final String TAG = RecipeRepository.class.getName();


    //--------------------------------------------------------------------------------|
    //                                  Params                                        |
    //--------------------------------------------------------------------------------|

    private static RecipeRepository INSTANCE;

    private final BakingAppExecutors mExecutors;
    private final RecipeDatabase mRecipeDB;
    private MediatorLiveData<List<Recipe>> mObservableRecipes;

    //--------------------------------------------------------------------------------|
    //                  Constructor (Singleton Pattern)                               |
    //--------------------------------------------------------------------------------|

    private RecipeRepository(final RecipeDatabase database,
                             final BakingAppExecutors executors) {
        mRecipeDB = database;
        mObservableRecipes = new MediatorLiveData<>();
        mExecutors = executors;

        /*
        mObservableRecipes.addSource(mRecipeDB.recipeDao().getRecipes(),
                productEntities -> {
                    if (mRecipeDB.getDatabaseCreated().getValue() != null) {
                        mObservableRecipes.postValue(productEntities);
                    }
                });
                */
    }

    public static RecipeRepository getInstance(final RecipeDatabase database,
                                               final BakingAppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (RecipeRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipeRepository(database, executors);
                }
            }
        }
        return INSTANCE;
    }


    //--------------------------------------------------------------------------------|
    //                               Local DB Ops                                     |
    //--------------------------------------------------------------------------------|

    public LiveData<List<Recipe>> getRecipes() {
        refreshRecipes();
        return mRecipeDB.recipeDao().getRecipes();
    }


    //--------------------------------------------------------------------------------|
    //                               Network Requests                                 |
    //--------------------------------------------------------------------------------|

    private void refreshRecipes() {

        //Creating an object of our api interface
        final RecipeApiService api = RecipeApiClient.getApiService();

        mExecutors.networkIO().execute(new Runnable() {

            @Override
            public void run() {

                // TODO check if recipes were fetched recently in order to
                // minimize the number of HTTP calls

                // Calling JSON
                Call<List<Recipe>> call = api.getRecipeJSON();

                // Enqueue Callback will be call when get response...
                call.enqueue(new Callback<List<Recipe>>() {
                    @Override
                    public void onResponse(Call<List<Recipe>> call,
                                           Response<List<Recipe>> response) {
                        if (response.isSuccessful()) {
                            List<Recipe> data = new ArrayList<>();
                            for(int i = 0; i < response.body().size(); i++) {
                                data.add(response.body().get(i));
                            }
                            mExecutors.diskIO().execute(new Runnable() {
                                   @Override
                                   public void run() {
                                       mRecipeDB.recipeDao().insertRecipes(data);
                                   }
                               }
                            );
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Recipe>> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });

            }
        });
    }

    /*
    public LiveData<List<Recipe>> getRecipes() {

        final MutableLiveData<List<Recipe>> recipes = new MutableLiveData<>();

        //Creating an object of our api interface
        final RecipeApiService api = RecipeApiClient.getApiService();

        mExecutors.networkIO().execute(new Runnable() {

               @Override
               public void run() {
                   // Calling JSON
                   Call<List<Recipe>> call = api.getRecipeJSON();

                   // Enqueue Callback will be call when get response...
                   call.enqueue(new Callback<List<Recipe>>() {
                       @Override
                       public void onResponse(Call<List<Recipe>> call,
                                              Response<List<Recipe>> response) {
                           if (response.isSuccessful()) {
                               List<Recipe> data = new ArrayList<>();
                               for(int i = 0; i < response.body().size(); i++) {
                                   data.add(response.body().get(i));
                               }
                               recipes.setValue(data);
                           }
                       }

                       @Override
                       public void onFailure(Call<List<Recipe>> call, Throwable t) {
                           Log.e(TAG, t.toString());
                           recipes.setValue(null);
                       }
                   });

               }
           });

        return recipes;

    }
    */

}
