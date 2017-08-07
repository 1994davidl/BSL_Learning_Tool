package com.example.davidalaw.bsllearningtool.mFragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mAdapters.Utils;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import net.protyposis.android.mediaplayer.MediaPlayer;
import net.protyposis.android.mediaplayer.MediaSource;
import net.protyposis.android.mediaplayer.VideoView;

import java.util.ArrayList;
import java.util.Collections;

public class QuizFragment extends Fragment {

    private static final String TAG = QuizFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;


    private DBHandler mDBHandler;
    private ProgressBar mProgressBar;
    private VideoView mVideoView;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton3, mRadioButton4;
    private TextView mTextView;
    private Button mButton;

    private String VideoURL, mAnswer;


    private Uri mVideoURI;
    private int mVideoPosition;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    private MediaSource mMediaSource;


    private String categorySelected;
    private ArrayList<String> mArrayList;

    public QuizFragment() {
        // Required empty public constructor
    }

    /**
     *
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        categorySelected = getArguments().getString("Category"); Log.d(TAG, " CATEGORY SELECTED " + categorySelected);
        getCategorySelectedInfo();

        //initialise GUI components
        mTextView = (TextView) view.findViewById(R.id.score_number); mTextView.setText("0");
        mVideoView = (VideoView) view.findViewById(R.id.videoView);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
            mRadioButton1 = (RadioButton) view.findViewById(R.id.choiceA);
            mRadioButton2 = (RadioButton) view.findViewById(R.id.choiceB);
            mRadioButton3 = (RadioButton) view.findViewById(R.id.choiceC);
            mRadioButton4 = (RadioButton) view.findViewById(R.id.choiceD);
        mButton = (Button) view.findViewById(R.id.next_button);

        //Call helper methods
        populateRadioButtons();
        videoSettingsOnCreate();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                mVideoPlaying = false;
                mMediaSource = null;
                if(mRadioGroup.isSelected()){
                    Log.d(TAG, "onClick: " + mRadioGroup.isSelected());
                }
                refreshView();
            }
        });

        return view;
    }

    /**
     *
     */
    private void videoSettingsOnCreate() {
        mVideoURI = Uri.parse(VideoURL);
        mVideoPosition = 0;
        mVideoPlaybackSpeed = 1;
        mVideoPlaying = true;
    }

    /**
     *
     */
    private void refreshView ()
    {
        Collections.rotate(mArrayList, -1); //Send first array list item to the back.
        getVideoURL();
        videoSettingsOnCreate();
        initPlayer();
        populateRadioButtons();
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        if (!mVideoView.isPlaying()) {
            initPlayer();
        }
    }

    /**
     *
     */
    private void initPlayer() {
        //Remove progress bar, begin media controllor process
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true); //Loop video continously
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

        if (mMediaSource == null) {
            // Convert uri to media source asynchronously to avoid UI blocking
            // It could take a while, e.g. if it's a DASH source and needs to be preprocessed
            Utils.uriToMediaSourceAsync(getActivity(), mVideoURI, mMediaSourceAsyncCallbackHandler);
            Log.d(TAG, "PLAYER URI: ." + mVideoURI);
        } else {
            // Media source is already here, just use it
            mMediaSourceAsyncCallbackHandler.onMediaSourceLoaded(mMediaSource);
        }
    }

    /**
     *
     */
    private void getCategorySelectedInfo() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllQuestions();

        mArrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            if (categorySelected.equals(cursor.getString(1))) {
                mArrayList.add(cursor.getString(0));
            }
        }
        shuffleQuestions();
        getVideoURL();
    }

    /**
     *
     */
    private String getVideoURL() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllQuestions();

        while (cursor.moveToNext()) {
            if (mArrayList.get(0).equals(cursor.getString(0))) {
                VideoURL = cursor.getString(2); Log.d(TAG, "Video URL to be shown: " + VideoURL);
            }
        }
        return VideoURL;
    }

    /**
     * Quick & easy way to shuffle the array list using Java collections
     */
    private void shuffleQuestions() {
        Collections.shuffle(mArrayList);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Call get all questions from DBhandler class and populate the radio buttons appropriately.
     */
    private void populateRadioButtons() {

        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllQuestions();
        while (cursor.moveToNext()) {
            if (mArrayList.get(0).equals(cursor.getString(0))) {
                mRadioButton1.setText(cursor.getString(3));
                mRadioButton2.setText(cursor.getString(4));
                mRadioButton3.setText(cursor.getString(5));
                mRadioButton4.setText(cursor.getString(6));
                return;
            }
        }
    }


    public String getAnswer() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllQuestions();

        while (cursor.moveToNext()) {
            if (mArrayList.get(0).equals(cursor.getString(0))) {
                mAnswer = cursor.getString(7);
            }
        }
        return mAnswer;
    }


}
