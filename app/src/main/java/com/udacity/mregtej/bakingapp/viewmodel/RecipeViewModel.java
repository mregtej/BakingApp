package com.udacity.mregtej.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.mregtej.bakingapp.application.BakingApp;
import com.udacity.mregtej.bakingapp.datamodel.Recipe;
import com.udacity.mregtej.bakingapp.repository.RecipeRepository;

import java.util.List;

public class RecipeViewModel extends AndroidViewModel {

    private final RecipeRepository mRecipeRepository;

    public RecipeViewModel(Application application, RecipeRepository repository) {
        super(application);
        this.mRecipeRepository = repository;
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipeRepository.getRecipes();
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final RecipeRepository mRepository;

        public Factory(@NonNull Application application) {
            mApplication = application;
            mRepository = ((BakingApp) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new RecipeViewModel(mApplication, mRepository);
        }
    }
}
