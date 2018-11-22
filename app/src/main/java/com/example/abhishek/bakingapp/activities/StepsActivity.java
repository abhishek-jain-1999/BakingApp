package com.example.abhishek.bakingapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.abhishek.bakingapp.dataHolder.Steps;
import com.example.abhishek.bakingapp.fragment.VideoDisplayFragment;
import com.example.abhishek.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class StepsActivity extends AppCompatActivity implements ExoPlayer.EventListener {


    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private VideoDisplayFragment fragment;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        mPlayerView = findViewById(R.id.simple_exo_player_steps);

        Intent intent = getIntent();

        if (intent != null) {
            String s1[] = intent.getStringArrayExtra("VideoURL");
            String s2[] = intent.getStringArrayExtra("Description");
            String s3[] = intent.getStringArrayExtra("ShortDescription");
            String s4[] = intent.getStringArrayExtra("ThumbnailURL");
            List<Steps> list = createListStep(s1, s2, s3, s4);
            int id = intent.getIntExtra("Id", 0);

            setTitle(intent.getStringExtra("title"));

            if (list.get(id).getVideoURL() != null) {
                if (list.get(id).getVideoURL().length() != 0) {
                    if (mPlayerView != null) {
                        initializeMediaSession();
                        initializePlayer(Uri.parse(list.get(id).getVideoURL()));
                    } else {
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragment = VideoDisplayFragment.newInstance(list.get(id).getVideoURL(), list.get(id).getDescription(), list);
                        fragment.id = id;
                        fragmentTransaction.add(R.id.detail_contain_fragment, fragment);
                        fragmentTransaction.commit();
                    }
                }
            }
        }

    }

    private List<Steps> createListStep(String[] s, String[] s2, String[] s3, String[] s4) {
        List<Steps> list = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            list.add(new Steps(s[i], s2[i], i, s3[i], s4[i]));
        }
        return list;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
        super.onSaveInstanceState(outState);
    }


    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(this, TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new StepsActivity.MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }


    @Override
    protected void onPause() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        if (mExoPlayer != null) {
            if (!mExoPlayer.getPlayWhenReady()) {
                mExoPlayer.setPlayWhenReady(true);
            }
        }
        super.onPostResume();
    }

    @Override
    public void onDestroy() {
        releasePlayer();
        if (mMediaSession != null) {
            mMediaSession.setActive(false);
        }
        super.onDestroy();
    }


    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
