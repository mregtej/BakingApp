package com.udacity.mregtej.bakingapp.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.C;
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

    //--------------------------------------------------------------------------------|
    //                                 Constants                                      |
    //--------------------------------------------------------------------------------|

    /** Key for storing the recipe steps in savedInstanceState */
    private static final String RECIPE_STEPS_SAVED_INSTANCE = "recipe-step";
    /** Key for storing the recipe step video playing status in savedInstanceState */
    private static final String RECIPE_VIDEO_IS_PLAYING_KEY = "recipe-video-is-playing";
    /** Key for storing the recipe step video time-position in savedInstanceState */
    private static final String RECIPE_VIDEO_POSITION_KEY = "recipe-video-position";


    //--------------------------------------------------------------------------------|
    //                                 Parameter                                      |
    //--------------------------------------------------------------------------------|

    /** Video not available & Thumbnail ImageView */
    @BindView(R.id.iv_recipe_video_not_available) ImageView mVideoNotAvailableImageView;
    /** Video frame */
    @BindView(R.id.ev_recipe_step_video) SimpleExoPlayerView mSimpleExoPlayerView;
    /** Video player */
    private SimpleExoPlayer player;
    /** Recipe Step */
    private Step mRecipeStep;
    /** App Context */
    private Context mContext;
    /** Video time-position */
    private long mVideoPosition;
    /** Video Window */
    private Timeline.Window window;
    /** Media data source (Factory) */
    private DataSource.Factory mediaDataSourceFactory;
    /** Video track selector */
    private DefaultTrackSelector trackSelector;
    /** Video auto-play status */
    private boolean shouldAutoPlay;
    /** Video Bandwidth */
    private BandwidthMeter bandwidthMeter;


    //--------------------------------------------------------------------------------|
    //                                 Constructor                                    |
    //--------------------------------------------------------------------------------|

    public RecipeStepVideoDetailViewFragment() { }


    //--------------------------------------------------------------------------------|
    //                              Override Methods                                  |
    //--------------------------------------------------------------------------------|

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Bind Views
        View rootView = inflater.inflate(R.layout.fragment_detail_recipe_step_video, container,
                false);
        ButterKnife.bind(this, rootView);
        mContext = rootView.getContext();

        // Initialize video position (min value)
        mVideoPosition = C.TIME_UNSET;

        if(savedInstanceState != null) {
            mRecipeStep = savedInstanceState.getParcelable(RECIPE_STEPS_SAVED_INSTANCE);
            mVideoPosition = savedInstanceState.getLong(RECIPE_VIDEO_POSITION_KEY, C.TIME_UNSET);
            shouldAutoPlay = savedInstanceState.getBoolean(RECIPE_VIDEO_IS_PLAYING_KEY, true);
        } else {
            // Set-up ExoPlayer
            shouldAutoPlay = true;
            mVideoPosition = C.TIME_UNSET;
        }

        // Set-up ExoPlayer
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, "BakingApp"),
                (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        // Return rootView
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(RECIPE_VIDEO_IS_PLAYING_KEY, player.getPlayWhenReady());
        outState.putLong(RECIPE_VIDEO_POSITION_KEY, mVideoPosition);
        outState.putParcelable(RECIPE_STEPS_SAVED_INSTANCE, mRecipeStep);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(player != null /* && player.isLoading() */) {
                player.stop();
            }
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mVideoPosition != C.TIME_UNSET) {
            player.seekTo(mVideoPosition);
        }
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player != null) {
            mVideoPosition = player.getCurrentPosition();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
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
            if(player != null /* && player.isLoading() */) {
                player.stop();
            }
            initializePlayer();
        }
    }


    //--------------------------------------------------------------------------------|
    //                               Private Methods                                  |
    //--------------------------------------------------------------------------------|

    /**
     * Loads a video media content.
     *
     * @param extractorsFactory
     */
    private void loadVideoMediaFile(final DefaultExtractorsFactory extractorsFactory) {
        long delayMillis = 100;
        // 1. Check video-url is not empty and is video content (mp4 and 3gp supported videos)
        final String videoURL = handleVideoURL(mRecipeStep.getVideoURL());
        if(videoURL != null) {
            // 2. Load Thumbnail (only if video has't not yet been played)
            if(mVideoPosition == C.TIME_UNSET) {
                handleVideoThumbnail();
                // Added some delay to display thumbnail
                delayMillis = 1000;
            }
            // 3. Prepare loading of video media source (
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                            mediaDataSourceFactory, extractorsFactory, null, null);
                    player.prepare(mediaSource);
                    mVideoNotAvailableImageView.setVisibility(View.INVISIBLE);
                }
            }, delayMillis);

        } else {
            // 4. There is no available video. Load default image
            Picasso
                    .with(mContext)
                    .load(R.drawable.ic_video_not_available)
                    .fit()
                    .centerCrop()
                    .into(mVideoNotAvailableImageView);
            mVideoNotAvailableImageView.setVisibility(View.VISIBLE);
        }
    }

    //--------------------------------------------------------------------------------|
    //                            ExoPlayer Methods                                   |
    //--------------------------------------------------------------------------------|

    /**
     * Initialize Video Player.
     */
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

        // Load video media content
        loadVideoMediaFile(extractorsFactory);

    }

    /**
     * Release Video Player.
     */
    private void releasePlayer() {
        if (player != null) {
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    /**
     * Handles Video-URL (checks if url contains a valid video media file).
     *
     * @param url   URL Media content
     * @return      Valid URL video file
     */
    private String handleVideoURL(String url) {
        if(TextUtils.isEmpty(url)) { return null; }
        if (url.contains(".mp4") || url.contains(".3gp")) { return url; }
        return null;
    }

    /**
     * Handles the loading of the Video Thumbnail
     */
    private void handleVideoThumbnail() {
        String thumbnailURL = mRecipeStep.getThumbnailURL();
        if (!TextUtils.isEmpty(thumbnailURL)) {
            Picasso
                    .with(mContext)
                    .load(thumbnailURL)
                    .fit()
                    .centerCrop()
                    .error(R.drawable.im_no_thumbnail)
                    .into(mVideoNotAvailableImageView);
        } else {
            // 2.1 Load default not available thumbnail image
            Picasso
                    .with(mContext)
                    .load(R.drawable.im_no_thumbnail)
                    .fit()
                    .centerCrop()
                    .into(mVideoNotAvailableImageView);
        }
        mVideoNotAvailableImageView.setVisibility(View.VISIBLE);
    }

}
