package com.example.abhishek.bakingapp.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishek.bakingapp.ImageLink;
import com.example.abhishek.bakingapp.dataHolder.Steps;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoDisplayFragment extends Fragment implements ExoPlayer.EventListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public List<Steps> stepsList;

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    public TextView t;
    private TextView textview;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private ImageView right, left;
    private ConstraintLayout area;
    public int id;
    private ImageView iv;


    public VideoDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideoDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoDisplayFragment newInstance(String param1, String param2, List<Steps> stepsList) {
        VideoDisplayFragment fragment = new VideoDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.stepsList = stepsList;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_display, container, false);


        mPlayerView = view.findViewById(R.id.simple_exo_player_steps);
        textview = view.findViewById(R.id.not_selected);
        area = view.findViewById(R.id.step_change_area);
        t = view.findViewById(R.id.des_text_view);
        iv = view.findViewById(R.id.thumbnail_iv);

        if (!(mParam1.equals("") && mParam2.equals(""))) {
            textview.setVisibility(View.INVISIBLE);
            t.setVisibility(View.VISIBLE);
            t.setText(mParam2);

            if (mParam1.length() != 0) {
                mPlayerView.setVisibility(View.VISIBLE);
                initializeMediaSession();
                initializePlayer(Uri.parse(mParam1));
            } else {
                mPlayerView.setVisibility(View.INVISIBLE);
            }
        }

        if (stepsList == null) {
            area.setVisibility(View.GONE);
        } else {
            if (!stepsList.get(id).getThumbnailURL().equals("")) {
                Picasso.get().load(stepsList.get(id).getThumbnailURL()).into(iv);
                iv.setVisibility(View.VISIBLE);
            }
            right = view.findViewById(R.id.right_step);
            left = view.findViewById(R.id.left_step);
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (id != stepsList.size() - 1) {
                        id++;
                        releasePlayer();
                        if (stepsList.get(id).getVideoURL().length() != 0) {
                            mPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(stepsList.get(id).getVideoURL()));
                        } else {
                            mPlayerView.setVisibility(View.INVISIBLE);
                        }
                        t.setText(stepsList.get(id).getDescription());
                    }
                }
            });
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (id != 0) {
                        id--;
                        releasePlayer();
                        initializePlayer(Uri.parse(stepsList.get(id).getVideoURL()));
                        t.setText(stepsList.get(id).getDescription());
                    }
                }
            });
        }


        return view;
    }


    public void initializePlayer(Uri mediaUri) {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
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
            iv.setVisibility(View.GONE);
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
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

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
        mMediaSession.setCallback(new MySessionCallback());

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
