package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
import android.graphics.Color;
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
import com.udacity.mregtej.bakingapp.ui.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDataDetailViewFragment extends Fragment {

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Key for storing the recipe steps in savedInstanceState */
    private static final String RECIPE_STEPS_SAVED_INSTANCE = "recipe-step";


    //--------------------------------------------------------------------------------|
    //                                 Parameters                                     |
    //--------------------------------------------------------------------------------|

    /** Activity Context */
    private Context mContext;
    /** Recipe (ModelData) */
    private Step mRecipeStep;
    /** Change Recipe Step (next/previous step) onClickListener */
    private ChangeRecipeStepClickListener mChangeRecipeStepClickListener;
    /** Recipe Short Description TextView */
    @BindView(R.id.tv_recipe_step_short_description) TextView mRecipeStepShortDescription;
    /** Recipe Full Description TextView */
    @BindView(R.id.tv_recipe_step_full_description) TextView mRecipeStepFullDescription;
    /** Recipe Next Step Button */
    @BindView(R.id.bt_next_recipe_step) TextView mRecipeNextStepButton;
    /** Recipe Previous Step Button */
    @BindView(R.id.bt_previous_recipe_step) TextView mRecipePreviousStepButton;


    //--------------------------------------------------------------------------------|
    //                                 Constructor                                    |
    //--------------------------------------------------------------------------------|

    public RecipeStepDataDetailViewFragment() { }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

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
        updateUI();
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

    /**
     * Update UI Fragment elements
     */
    public void updateUI() {
        if(TextUtils.isEmpty(mRecipeStep.getShortDescription())) {
            mRecipeStepShortDescription.setText(getString(R.string.empty_description));
            mRecipeStepShortDescription.setTextColor(Color.parseColor("#ff0000"));
        } else {
            mRecipeStepShortDescription.setText(mRecipeStep.getShortDescription());
            mRecipeStepShortDescription.setTextColor(Color.parseColor("#000000"));
        }
        if(TextUtils.isEmpty(mRecipeStep.getDescription())) {
            mRecipeStepFullDescription.setText(getString(R.string.empty_description));
            mRecipeStepFullDescription.setTextColor(Color.parseColor("#ff0000"));
        } else {
            mRecipeStepFullDescription.setText(mRecipeStep.getDescription());
            mRecipeStepFullDescription.setTextColor(Color.parseColor("#000000"));
        }
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
