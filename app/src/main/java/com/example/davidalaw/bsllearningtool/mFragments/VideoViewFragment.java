package com.example.davidalaw.bsllearningtool.mFragments;



import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.davidalaw.bsllearningtool.R;

import com.example.davidalaw.bsllearningtool.mModel_Controller.Utils;
import com.example.davidalaw.bsllearningtool.mModel_Controller.SignMaterialAdapter;

import net.protyposis.android.mediaplayer.MediaSource;
import net.protyposis.android.mediaplayer.MediaPlayer;
import net.protyposis.android.mediaplayer.VideoView;

/**
 *
 *
 *
 */
public class VideoViewFragment extends Fragment implements android.widget.CompoundButton.OnCheckedChangeListener {

    private static final String TAG = VideoViewFragment.class.getSimpleName();

    private SignMaterialAdapter mSignMaterialAdapter;

    private ProgressBar mProgressBar;
    private VideoView mVideoView;
    private TextView mTextView;

    private int mVideoPosition;
    private final int signSelected;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    private String mVideoURL;

    private CheckBox mCheckBox1, mCheckBox2, mCheckBox3, mCheckBox4;

    private Uri mVideoURI;
    private MediaSource mMediaSource;

    //Constructor
    public VideoViewFragment(int signSelected) {
        this.signSelected = signSelected;
    }


    /**
     * OnCreateView initialises GUI components and call helper methods
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, container, false);

        mVideoView = view.findViewById(R.id.videoView);
        mProgressBar = view.findViewById(R.id.progress);
        mTextView = view.findViewById(R.id.bsl_text);

        getVideoURL();//Get the video of the sign the user has selected
        getBSLSignOrder(); //Display the BSL interpretation in text form
        videoplaySettingsOnCreate(); //video prerequisites
        checkBoxesStates(view); //video speed adjustments intilialise gui components.

        return view;
    }

    /**
     * Preconfigurations the video settings.
     */
    private void videoplaySettingsOnCreate(){
        mProgressBar.setVisibility(View.VISIBLE);
        mVideoURI = Uri.parse(mVideoURL); //Video URL
        mVideoPosition = 0; //Position video will start playing at 0 (start of video)
        mVideoPlaybackSpeed = 1; //the speed of the video
        mVideoPlaying = true; //video is to be played immediately once fully loaded.
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!mVideoView.isPlaying()) {
            initPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mVideoView.isPlaying()) {
            initPlayer(); //initialise media player
        }
    }

    /**
     * Initialise video player.
     */
    private void initPlayer(){

        //Remove progress bar, begin media playback process
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.GONE);
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
            }
        });

        //replay the video on completion.
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.seekTo(mVideoPosition); //start at position 0
                mProgressBar.setVisibility(View.GONE);
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
                if (mVideoPlaying) {
                    mVideoView.start();
                }
            }
        });

        //video stops after share intent is called. click on video to resume play.
        mVideoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mVideoView.isPlaying()) {
                    mVideoView.start(); //
                }
            }
        });


        //Display toast message and disable the media controller in the event
        //that the video is unable to load
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mMediaPlayer, int what, int extra) {
                Toast.makeText(getActivity(),
                        "Cannot play the video for " + getSignSelected() + "Please try another sign",
                        Toast.LENGTH_LONG).show();

                mProgressBar.setVisibility(View.GONE); // remove progress bar.
                return true;
            }
        });

        //
        mVideoView.setOnSeekListener(new MediaPlayer.OnSeekListener() {
            @Override
            public void onSeek(MediaPlayer mp) {
                Log.d(TAG, "onSeek");
                mProgressBar.setVisibility(View.VISIBLE); //Still to load
            }
        });

        /*
          Upon completion of video load, then the progress bar will no longer display.
         */
        mVideoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, "onSeekComplete");
                mProgressBar.setVisibility(View.GONE); //finished seek.
            }
        });

        /*
          Abstract method that fetches and decodes the URI into Mediasource by calling util helper methods.
         */
        Utils.MediaSourceAsyncCallbackHandler mMediaSourceAsyncCallbackHandler = new Utils.MediaSourceAsyncCallbackHandler() {
            @Override
            public void onMediaSourceLoaded(MediaSource mediaSource) {
                mMediaSource = mediaSource;
                mVideoView.setVideoSource(mediaSource);
                mVideoView.seekTo(mVideoPosition);
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
                if (mVideoPlaying) {
                    mVideoView.start();
                }
            }

            @Override
            public void onException(Exception e) {
                Toast.makeText(getActivity(),
                        "Cannot play the video for " + getSignSelected() + "Please try another sign",
                        Toast.LENGTH_LONG).show();Log.e(TAG, "error loading video", e);
            }
        };

        if(mMediaSource == null) {
            // Convert uri to media source asynchronously to avoid UI blocking
            Utils.uriToMediaSourceAsync(getActivity(), mVideoURI, mMediaSourceAsyncCallbackHandler);
            Log.d(TAG, "PLAYER URI: ." + mVideoURI);
        } else {
            // Media source is already here, just use it
            mMediaSourceAsyncCallbackHandler.onMediaSourceLoaded(mMediaSource);
        }
    }

    /**
     * Initialises the checkbox for the playback speeds.
     *
     * @param view
     */
    private void checkBoxesStates(View view) {
        mCheckBox1 = view.findViewById(R.id.pointtwofivespeed);
        mCheckBox1.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox2 = view.findViewById(R.id.pointfivespeed);
        mCheckBox2.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox3 = view.findViewById(R.id.pointSevenFivespeed);
        mCheckBox3.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox4 = view.findViewById(R.id.fullspeed);
        mCheckBox4.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox4.setChecked(true);
    }

    /**
     * Adjust the speed of the video playback while also changing the state of the checkboxes accordingly.
     *
     * @param compoundButton
     * @param state
     */
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean state) {

        if(compoundButton.isChecked()) {
            if(compoundButton == mCheckBox1){
                mCheckBox2.setChecked(false);
                mCheckBox3.setChecked(false);
                mCheckBox4.setChecked(false);
                mCheckBox1.setChecked(true); //display tick
                mVideoPlaybackSpeed = 0.25f; //change playback speed to 0.25x if checkbox 1 clicked
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
            } else if (compoundButton == mCheckBox2) {
                mCheckBox1.setChecked(false);
                mCheckBox3.setChecked(false);
                mCheckBox4.setChecked(false);
                mCheckBox2.setChecked(true); //display tick
                mVideoPlaybackSpeed = 0.5f; //change playback speed to 0.5x if checkbox 2 clicked
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
            } else if (compoundButton == mCheckBox3) {
                mCheckBox1.setChecked(false);
                mCheckBox2.setChecked(false);
                mCheckBox4.setChecked(false);
                mCheckBox3.setChecked(true); //display tick
                mVideoPlaybackSpeed = 0.75f; //change playback speed to 0.5 if checkbox 3 clicked
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
            } else {
                mCheckBox1.setChecked(false);
                mCheckBox2.setChecked(false);
                mCheckBox3.setChecked(false);
                mCheckBox4.setChecked(true); //display tick
                mVideoPlaybackSpeed = 1.00f; //set to original speed of 1.0 if checkbox 4 clicked.
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
            }
        } else {
            Log.d(TAG, "Speed undone " + compoundButton + " State: " + state);
            mVideoView.setPlaybackSpeed(1.00f); //if no checkbox is checked, set speed to orginal speed.
        }
    }

    /**
     * Fetch the video URL from the model class to be converted by the external library into
     * a media source.
     */
    private void getVideoURL() {
        mSignMaterialAdapter = new SignMaterialAdapter();
        mVideoURL = mSignMaterialAdapter.getVideoURL(getContext(), signSelected);
    }

    /**
     * Populate the textview below the video which display the BSL gloss of the sign.
     */
    private void getBSLSignOrder() {
        Log.d(TAG, "Populate BSL Notation View");
        mSignMaterialAdapter = new SignMaterialAdapter();
        mTextView.setText(mSignMaterialAdapter.getBSLOrderName(getContext(), signSelected));
    }

    /* Get the sign selected by the end user */
    private int getSignSelected() {
        return signSelected;
    }

    public interface OnFragmentInteractionListener {
    }

}
