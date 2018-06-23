package com.udacity.mregtej.bakingapp.application;

import android.app.Application;

import com.udacity.mregtej.bakingapp.comm.ConnectivityHandler;
import com.udacity.mregtej.bakingapp.repository.RecipeRepository;

public class BakingApp extends Application {

    private static BakingApp mInstance;
    private BakingAppExecutors mBakingAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();
        mBakingAppExecutors = new BakingAppExecutors();
        mInstance = this;
    }

    public static synchronized BakingApp getInstance() {
        return mInstance;
    }

    public RecipeRepository getRepository() {
        return RecipeRepository.getInstance(mBakingAppExecutors);
    }

    public void setConnectivityListener(ConnectivityHandler.ConnectivityHandlerListener listener) {
        ConnectivityHandler.connectivityHandlerListener = listener;
    }
}
