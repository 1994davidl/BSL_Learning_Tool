package com.example.davidalaw.bsllearningtool.mFragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.davidalaw.bsllearningtool.mModel_Controller.Utils;
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
    private VideoView mVideoView;
    private RadioGroup mRadioGroup;

    private final int SIZE = 4;

    private Class fragmentClass = null;

    private TextView mTextView;
    private Button mButton;
    private RadioButton mRButton;

    private String VideoURL, mAnswer;

    private Uri mVideoURI;
    private int mVideoPosition;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    private MediaSource mMediaSource;

    private String categorySelected, number_of_questions;
    private ArrayList<String> mArrayList;

    private int score = 0;
    private int numOfQuestions = 0;
    private int setNumberofQuestions = 1;
    private int number_wrong = 0;


    public QuizFragment() {
        // Required empty public constructor
    }

    /**
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

        categorySelected = getArguments().getString("Category");

        number_of_questions = getArguments().getString("Question");


        Log.d(TAG, " CATEGORY SELECTED " + categorySelected + " QUESTIONS SELECTED " + number_of_questions);
        setNumberofQuestions = Integer.parseInt(number_of_questions);

        getCategorySelectedInfo();

        //initialise GUI components
        mTextView = (TextView) view.findViewById(R.id.score_number);
        mTextView.setText(String.valueOf(score) + "/" + String.valueOf(setNumberofQuestions));
        mVideoView = (VideoView) view.findViewById(R.id.videoView);
        mButton = (Button) view.findViewById(R.id.next_button);

        //Call helper methods
        radioButtonGroupListener(view);
        populateRadioButtons();
        videoSettingsOnCreate();

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
     * @param view
     */
    public void radioButtonGroupListener(View view) {

        mRadioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfQuestions++;
                mVideoPlaying = false;
                mMediaSource = null;

                RadioButton selectedRButton = (RadioButton) mRadioGroup.getChildAt(getSelectedRadioButtonID());

                if (selectedRButton.getText().equals(getAnswer())) {
                    Toast.makeText(getActivity(), "Correct Answer: " + selectedRButton.getText(),
                            Toast.LENGTH_SHORT).show();
                    score += 1;
                    mTextView.setText(String.valueOf(score) + " / " + String.valueOf(setNumberofQuestions));
                } else {
                    Toast.makeText(getActivity(), "Wrong Answer: " + selectedRButton.getText() +
                            "\n The answer was: " + getAnswer(), Toast.LENGTH_LONG).show();
                    number_wrong++;
                }

                if (numOfQuestions == setNumberofQuestions) {
                    Toast.makeText(getActivity(), "Quiz Completed. You scored: " + score + " out of " + setNumberofQuestions,
                            Toast.LENGTH_SHORT).show();
                    writeProgressStateToDB();
                    endOfQuizMoveToProgressFrag();
                }
                refreshView();
            }
        });
    }

    public int getSelectedRadioButtonID(){
        int id = mRadioGroup.getCheckedRadioButtonId();
        View rb = mRadioGroup.findViewById(id);
        int radioID = mRadioGroup.indexOfChild(rb);

        return radioID;
    }

    public void endOfQuizMoveToProgressFrag() {
        ProgressFragment progress = new ProgressFragment();
        //Open Sign List Fragment
        Fragment fragment = null;
        fragmentClass = null;
        fragmentClass = ProgressFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
    }

    public void writeProgressStateToDB() {
        mDBHandler = new DBHandler(getActivity());
        mDBHandler.AddProgress(categorySelected, score, number_wrong);
    }




    /**
     *
     */
    private void refreshView() {
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
                mVideoPlaying = true;
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
                VideoURL = cursor.getString(2);
                Log.d(TAG, "Video URL to be shown: " + VideoURL);
            }
        }
        return VideoURL;
    }

    /**
     * Quick & easy way to shuffle the array list using Java collections
     */
    private void shuffleQuestions() {
        Collections.shuffle(mArrayList);
        for(int i = 0; i < mArrayList.size(); i++)
        Log.d(TAG, "Questions Shuffled: " + mArrayList.get(i));
    }

    /**
     * Call get all questions from DBhandler class and populate the radio buttons appropriately.
     */
    private void populateRadioButtons() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllQuestions();
        while (cursor.moveToNext()) {
            if (mArrayList.get(0).equals(cursor.getString(0))) {
                for (int i = 0; i < 4; i++) {
                    mRButton = (RadioButton) mRadioGroup.getChildAt(i);
                    mRButton.setText(cursor.getString(i + 3));
                }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
