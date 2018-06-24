package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailViewFragment extends Fragment {

    private Context mContext;

    private Step mRecipeStep;

    @BindView(R.id.tv_recipe_step_short_description) TextView mRecipeStepShortDescription;
    @BindView(R.id.tv_recipe_step_full_description) TextView mRecipeStepFullDescription;

    public RecipeStepDetailViewFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_detail_recipe_step, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();


        // Return rootView
        return rootView;
    }

    public void setmRecipeStep(Step mRecipeStep) {
        this.mRecipeStep = mRecipeStep;
        mRecipeStepShortDescription.setText(mRecipeStep.getShortDescription());
        mRecipeStepFullDescription.setText(mRecipeStep.getDescription());
    }

}
