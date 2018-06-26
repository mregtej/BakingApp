package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.mregtej.bakingapp.R;
import com.udacity.mregtej.bakingapp.datamodel.Step;
import com.udacity.mregtej.bakingapp.ui.utils.TextUtils;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepVideoDetailViewFragment extends Fragment {

    @BindView(R.id.iv_recipe_video_not_available) ImageView mVideoNotAvailableImageView;
    @BindView(R.id.ev_recipe_step_video) SimpleExoPlayerView mSimpleExoPlayerView;

    private static final String RECIPE_STEPS_SAVED_INSTANCE = "recipe-step";

    private SimpleExoPlayer player;

    private Step mRecipeStep;
    private Context mContext;

    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean shouldAutoPlay;
    private BandwidthMeter bandwidthMeter;

    public RecipeStepVideoDetailViewFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_detail_recipe_step_video, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        if(savedInstanceState != null) {
            mRecipeStep = savedInstanceState.getParcelable(RECIPE_STEPS_SAVED_INSTANCE);
        }

        // Set-up ExoPlayer
        shouldAutoPlay = true;
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(
                mContext, Util.getUserAgent(mContext,"BakingApp"),
                (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        // Return rootView
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECIPE_STEPS_SAVED_INSTANCE, mRecipeStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(player != null && player.isLoading()) {
                player.stop();
            }
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    //--------------------------------------------------------------------------------|
    //                            Getters / Setters                                   |
    //--------------------------------------------------------------------------------|

    public void setmRecipeStep(Step mRecipeStep) {
        this.mRecipeStep = mRecipeStep;
    }


    //--------------------------------------------------------------------------------|
    //                        Activity --> Fragment Comm                              |
    //--------------------------------------------------------------------------------|

    public void updateUI() {
        if (Util.SDK_INT > 23) {
            if(player != null && player.isLoading()) {
                player.stop();
            }
            initializePlayer();
        }
    }


    //--------------------------------------------------------------------------------|
    //                            ExoPlayer Methods                                   |
    //--------------------------------------------------------------------------------|

    private void initializePlayer() {

        mVideoNotAvailableImageView.setVisibility(View.GONE);
        mSimpleExoPlayerView.requestFocus();

        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        mSimpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(shouldAutoPlay);

        DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        String videoURL = handleVideoURL(mRecipeStep.getVideoURL());
        if(videoURL != null) {
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                    mediaDataSourceFactory, extractorsFactory, null, null);
            player.prepare(mediaSource);
        } else {
            Picasso
                    .with(mContext)
                    .load(R.drawable.ic_video_not_available)
                    .fit()
                    .centerCrop()
                    .into(mVideoNotAvailableImageView);
            mVideoNotAvailableImageView.setVisibility(View.VISIBLE);
        }

    }

    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    private String handleVideoURL(String url) {
        if(TextUtils.isEmpty(url)) { return null; }
        if (url.contains(".mp4") || url.contains(".3gp")) { return url; }
        return null;
    }

}
