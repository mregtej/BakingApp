package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDataDetailViewFragment extends Fragment {

    private Context mContext;

    private static final String RECIPE_STEPS_SAVED_INSTANCE = "recipe-step";

    private Step mRecipeStep;

    private ChangeRecipeStepClickListener mChangeRecipeStepClickListener;

    @BindView(R.id.tv_recipe_step_short_description) TextView mRecipeStepShortDescription;
    @BindView(R.id.tv_recipe_step_full_description) TextView mRecipeStepFullDescription;
    @BindView(R.id.bt_next_recipe_step) TextView mRecipeNextStepButton;
    @BindView(R.id.bt_previous_recipe_step) TextView mRecipePreviousStepButton;

    public RecipeStepDataDetailViewFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_detail_recipe_step_data, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        if(savedInstanceState != null) {
            mRecipeStep = savedInstanceState.getParcelable(RECIPE_STEPS_SAVED_INSTANCE);
        }

        // Set Button OnClickListeners
        setButtonClickListeners();

        // Return rootView
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mRecipeStepShortDescription.setText(mRecipeStep.getShortDescription());
        mRecipeStepFullDescription.setText(mRecipeStep.getDescription());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECIPE_STEPS_SAVED_INSTANCE, mRecipeStep);
        super.onSaveInstanceState(outState);
    }

    //--------------------------------------------------------------------------------|
    //                            Getters / Setters                                   |
    //--------------------------------------------------------------------------------|

    public void setmRecipeStep(Step mRecipeStep) {
        this.mRecipeStep = mRecipeStep;
    }

    public void setmChangeRecipeStepClickListener(
            ChangeRecipeStepClickListener mChangeRecipeStepClickListener) {
        this.mChangeRecipeStepClickListener = mChangeRecipeStepClickListener;
    }

    public TextView getmRecipeNextStepButton() {
        return mRecipeNextStepButton;
    }

    public TextView getmRecipePreviousStepButton() {
        return mRecipePreviousStepButton;
    }


    //--------------------------------------------------------------------------------|
    //                        Fragment --> Activity Comm                              |
    //--------------------------------------------------------------------------------|

    public void moveToPreviousRecipeStep() {
        if(mChangeRecipeStepClickListener != null) {
            mChangeRecipeStepClickListener.onClickPreviousRecipeStep();
        }
    }

    public void moveToNextRecipeStep() {
        if(mChangeRecipeStepClickListener != null) {
            mChangeRecipeStepClickListener.onClickNextRecipeStep();
        }
    }

    public interface ChangeRecipeStepClickListener {
        public void onClickNextRecipeStep();
        public void onClickPreviousRecipeStep();
    }


    //--------------------------------------------------------------------------------|
    //                        Activity --> Fragment Comm                              |
    //--------------------------------------------------------------------------------|

    public void updateUI() {
        mRecipeStepShortDescription.setText(mRecipeStep.getShortDescription());
        mRecipeStepFullDescription.setText(mRecipeStep.getDescription());
    }


    //--------------------------------------------------------------------------------|
    //                              Private Methods                                   |
    //--------------------------------------------------------------------------------|

    private void setButtonClickListeners() {

        mRecipeNextStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToNextRecipeStep();

            }
        });

        mRecipePreviousStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToPreviousRecipeStep();
            }
        });

    }

}
