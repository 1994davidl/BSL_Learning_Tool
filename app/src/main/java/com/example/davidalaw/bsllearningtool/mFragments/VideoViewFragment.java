package com.example.davidalaw.bsllearningtool.mFragments;



import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.SignMaterialActivity;
import com.example.davidalaw.bsllearningtool.Utils;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import net.protyposis.android.mediaplayer.MediaSource;
import net.protyposis.android.mediaplayer.MediaPlayer;
import net.protyposis.android.mediaplayer.VideoView;

public class VideoViewFragment extends Fragment implements android.widget.CompoundButton.OnCheckedChangeListener {

    private static final String TAG = VideoViewFragment.class.getSimpleName();

    private DBHandler mDBHandler;

    private MediaController.MediaPlayerControl mMediaPlayerControl;

    private ProgressBar mProgressBar;
    private VideoView mVideoView;
    private TextView mTextView;

    private String VideoURL;
    private String signSelected;

    private Uri mVideoURI;
    private int mVideoPosition;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    private MediaSource mMediaSource;
    private CheckBox mCheckBox1, mCheckBox2, mCheckBox3, mCheckBox4;

    //Constructor
    public VideoViewFragment(String signSelected) {
        this.signSelected = signSelected;
    }


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, container, false);
        getVideoURL(); //Get the video of the sign the user has selected

        mVideoView = (VideoView)view.findViewById(R.id.videoView);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mTextView =(TextView) view.findViewById(R.id.bsl_text);
        getBSLSignOrder();

        mMediaPlayerControl = mVideoView;
        mProgressBar.setVisibility(View.VISIBLE);

        mVideoURI = Uri.parse(VideoURL);
        mVideoPosition =0;
        mVideoPlaybackSpeed = 1;
        mVideoPlaying = true;

        checkBoxesStates(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mVideoView.isPlaying()) {
            initPlayer();
        }
    }



    private void initPlayer(){

        //Remove progress bar, begin media controllor process
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.GONE);
                mp.setPlaybackSpeed(1.0f);
                mp.setLooping(true); //Loop video continously

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
                mProgressBar.setVisibility(View.GONE);
                return true;
            }
        });

        /**
         * Get information of video status

        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mMediaPlayer, int i, int extra) {
                String infoListener = "";
                switch (i) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                        infoListener = "MEDIA_INFO_BUFFERING_END";
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        infoListener = "MEDIA_INFO_BUFFERING_START";
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                        infoListener = "MEDIA_INFO_VIDEO_RENDERING_START";
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        infoListener = "MEDIA_INFO_VIDEO_TRACK_LAGGING";
                        break;
                }
                Log.d(TAG, "onInfo " + infoListener);
                return false;
            }
        }); */

        /**
         *
         */
        mVideoView.setOnSeekListener(new MediaPlayer.OnSeekListener() {
            @Override
            public void onSeek(MediaPlayer mp) {
                Log.d(TAG, "onSeek");
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });

        /**
         *
         */
        mVideoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                Log.d(TAG, "onSeekComplete");
                mProgressBar.setVisibility(View.GONE);
            }
        });

        /**
         * Abstract method
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
                Log.e(TAG, "error loading video", e);
            }
        };

        if(mMediaSource == null) {
            // Convert uri to media source asynchronously to avoid UI blocking
            // It could take a while, e.g. if it's a DASH source and needs to be preprocessed
            Utils.uriToMediaSourceAsync(getActivity(), mVideoURI, mMediaSourceAsyncCallbackHandler);
            Log.d(TAG, "PLAYER URI: ." + mVideoURI);
        } else {
            // Media source is already here, just use it
            mMediaSourceAsyncCallbackHandler.onMediaSourceLoaded(mMediaSource);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void checkBoxesStates(View view) {
        mCheckBox1 = (CheckBox) view.findViewById(R.id.pointtwofivespeed);
        mCheckBox1.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox2 = (CheckBox) view.findViewById(R.id.pointfivespeed);
        mCheckBox2.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox3 = (CheckBox) view.findViewById(R.id.pointSevenFivespeed);
        mCheckBox3.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox4 = (CheckBox) view.findViewById(R.id.fullspeed);
        mCheckBox4.setOnCheckedChangeListener(VideoViewFragment.this);
        mCheckBox4.setChecked(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean state) {

        if(compoundButton.isChecked() == true) {
            if(compoundButton == mCheckBox1){
                mCheckBox2.setChecked(false);
                mCheckBox3.setChecked(false);
                mCheckBox4.setChecked(false);
                mCheckBox1.setChecked(true);
                mVideoView.setPlaybackSpeed(0.25f);
            } else if (compoundButton == mCheckBox2) {
                mCheckBox1.setChecked(false);
                mCheckBox3.setChecked(false);
                mCheckBox4.setChecked(false);
                mCheckBox2.setChecked(true);
                mVideoView.setPlaybackSpeed(0.50f);
            } else if (compoundButton == mCheckBox3) {
                mCheckBox1.setChecked(false);
                mCheckBox2.setChecked(false);
                mCheckBox4.setChecked(false);
                mCheckBox3.setChecked(true);
                mVideoView.setPlaybackSpeed(0.75f);
            } else {
                mCheckBox1.setChecked(false);
                mCheckBox2.setChecked(false);
                mCheckBox3.setChecked(false);
                mCheckBox4.setChecked(true);
                mVideoView.setPlaybackSpeed(1.00f);
            }
        } else {
            Log.d(TAG, "Speed undone " + compoundButton + " State: " + state);
            mVideoView.setPlaybackSpeed(1.00f);
        }
    }


    private void getVideoURL() {

        Log.d(TAG, "Populate BSL Notation View");
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllData();
        while (cursor.moveToNext()) {
            if (getSignSelected().equals(cursor.getString(2))) {
                if (cursor.getString(9).equals("http://youtube.com")) {
                    VideoURL = "http://content.jwplatform.com/videos/PSYPYEXC-6B5j5ITm.mp4";
                } else {
                    VideoURL = cursor.getString(9);
                }
            }
        }
    }

    private void getBSLSignOrder() {
        Log.d(TAG, "Populate BSL Notation View");
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllData();
        while (cursor.moveToNext()) {
            if (getSignSelected().equals(cursor.getString(2))) {
               mTextView.setText(cursor.getString(3));
            }
        }
    }

    public String getSignSelected() {
        return signSelected;
    }

    public void setSignSelected(String signSelected) {
        this.signSelected = signSelected;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
