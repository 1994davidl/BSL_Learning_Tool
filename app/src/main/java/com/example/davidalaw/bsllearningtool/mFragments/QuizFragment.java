package com.example.davidalaw.bsllearningtool.mFragments;

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
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;
import com.example.davidalaw.bsllearningtool.mModel_Controller.Utils;
import com.example.davidalaw.bsllearningtool.mModel_Controller.DBHandler;

import net.protyposis.android.mediaplayer.MediaPlayer;
import net.protyposis.android.mediaplayer.MediaSource;
import net.protyposis.android.mediaplayer.VideoView;


/**
 * The type Quiz fragment.
 */
public class QuizFragment extends Fragment {

    private static final String TAG = QuizFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private MainPageAdapter mMainPageAdapter; //view controllor class

    //UI components
    private VideoView mVideoView;
    private RadioGroup mRadioGroup;
    private TextView mTextView;
    private Button mButton;

    //media player variables
    private String VideoURL;
    private Uri mVideoURI;
    private int mVideoPosition;
    private float mVideoPlaybackSpeed;
    private boolean mVideoPlaying;
    private MediaSource mMediaSource;
    private ProgressBar mProgressBar;

    //name of category selected
    private String categorySelected;

    //score & stats variables
    private int score = 0;
    private int numOfQuestions = 0;
    private int setNumberofQuestions = 1;
    private int number_wrong = 0;

    /**
     * Instantiates a new Quiz fragment.
     */
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

        categorySelected = getArguments().getString("Category"); // Category selected
        String number_of_questions = getArguments().getString("Question");
        setNumberofQuestions = Integer.parseInt(number_of_questions); //set numbers of questions for quiz.
        getActivity().setTitle(categorySelected + " Quiz"); //Title bar set to category selected

        //initialise GUI components
        mTextView = view.findViewById(R.id.score_number);
        mTextView.setText(String.valueOf(score) + "/" + String.valueOf(setNumberofQuestions));
        mVideoView = view.findViewById(R.id.videoView);
        mProgressBar = view.findViewById(R.id.progress);
       // mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mRadioGroup = view.findViewById(R.id.radiogroup);
        mButton = view.findViewById(R.id.next_button);

        //Call helper methods
        mMainPageAdapter = new MainPageAdapter();
        getCategorySelectedInfo();
        radioButtonGroupListener(view);
        videoSettingsOnCreate();

        return view;
    }

    /**
     * initialise video settings.
     */
    private void videoSettingsOnCreate() {
        mProgressBar.setVisibility(View.VISIBLE);
        mVideoURI = Uri.parse(VideoURL);
        mVideoPosition = 0;
        mVideoPlaybackSpeed = 0.80f;
        mVideoPlaying = true;
        mRadioGroup.setEnabled(false);
        mButton.setEnabled(false);
    }


    /**
     * Radio button group listener.
     *
     * @param view the view
     */
    private void radioButtonGroupListener(View view) {

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfQuestions++; //increment no. of questions completed
                mVideoPlaying = false; //stop video
                mMediaSource = null; //media source reset

                mRadioGroup.setEnabled(false); //disable
                mButton.setEnabled(false); //disable

                RadioButton selectedRButton = (RadioButton) mRadioGroup.getChildAt(getSelectedRadioButtonID()); //get index of selected radio button
                if (selectedRButton.getText().equals(mMainPageAdapter.getQuestionAnswer(getContext()))) {
                    Toast.makeText(getActivity(), "Correct Answer: " + selectedRButton.getText(),
                            Toast.LENGTH_SHORT).show(); //display answer
                    score += 1; //increment score
                    mTextView.setText(String.valueOf(score) + " / " + String.valueOf(setNumberofQuestions)); //update score
                } else {
                    Toast.makeText(getActivity(), "Wrong Answer: " + selectedRButton.getText() +
                            "\n The answer was: " + mMainPageAdapter.getQuestionAnswer(getContext()), Toast.LENGTH_LONG).show(); //display answer solution
                    number_wrong++; //increment number of questions wrong.
                }

                //end quiz if number of questions equate the fixed num of question set by user.
                if (numOfQuestions == setNumberofQuestions) {
                    Toast.makeText(getActivity(), "Quiz Completed. You scored: " + score + " out of " + setNumberofQuestions,
                            Toast.LENGTH_SHORT).show();
                    writeProgressStateToDB(); //write results to db
                    endOfQuizMoveToProgressFrag(); //start fragment transaction
                }
                refreshView(); //display new videoview & radio buttons
            }
        });
    }

    /**
     * return id of selected radio button
     */
    private int getSelectedRadioButtonID(){
        int id = mRadioGroup.getCheckedRadioButtonId();
        View rb = mRadioGroup.findViewById(id);
        return mRadioGroup.indexOfChild(rb);
    }

    /**
     * End of quiz move to progress fragment.
     */
    private void endOfQuizMoveToProgressFrag() {

        //Open Sign List Fragment
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = ProgressFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit(); //open progress fragment
    }

    /**
     * Write progress state to db.
     */
    private void writeProgressStateToDB() {
        DBHandler DBHandler = new DBHandler(getActivity());
        DBHandler.AddProgress(categorySelected, score, number_wrong, setNumberofQuestions);
    }


    /**
     * Display new question and it video demonstration.
     */
    private void refreshView() {
        mMainPageAdapter.moveFrontQuestionToBack(); //Send first array list item to the back.
        getVideoURLToDisplay();
        videoSettingsOnCreate();
        initPlayer();
        displayRadioButtonsChoices();
    }

    /**
     * On resume. if video is not playing start media player init
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
        // begin media player process
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mProgressBar.setVisibility(View.GONE);
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
                mRadioGroup.setEnabled(true);
                mButton.setEnabled(true);
            }
        });

        //on complete replay the video
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.seekTo(mVideoPosition);
                mProgressBar.setVisibility(View.GONE);
                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);
                if (mVideoPlaying) {
                    mVideoView.start();
                }
            }
        });

        //Display toast message and disable the media controller in the event
        //that the video is unable to load
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mMediaPlayer, int what, int extra) {
                Toast.makeText(getActivity(),
                        "Sorry,video cannot be played",
                        Toast.LENGTH_LONG).show();
                mProgressBar.setVisibility(View.GONE);
                return true;
            }
        });

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
            Utils.uriToMediaSourceAsync(getActivity(), mVideoURI, mMediaSourceAsyncCallbackHandler);
        } else {
            // Media source is already here, just load it
            mMediaSourceAsyncCallbackHandler.onMediaSourceLoaded(mMediaSource);
        }

    }

    /**
     * Gets category selected info.
     */
    private void getCategorySelectedInfo() {
        mMainPageAdapter.getCategoryQuestions(getContext(), categorySelected); //get questions that are available for category selected
        mMainPageAdapter.shuffleQuestions(); //shuffle the questions.
        getVideoURLToDisplay(); //call helper method
        displayRadioButtonsChoices(); //display helper method.
    }

    //Get the video uri to be decoded by media player
    private void getVideoURLToDisplay() {
        VideoURL = mMainPageAdapter.getVideoURL(getContext()); //call controller helper method to get Video URL
    }

    /**
     * Call get all questions from DBhandler class and populate the radio buttons appropriately.
     */
    private void displayRadioButtonsChoices() {
        mMainPageAdapter.populateRadioButtonsQueue(getContext()); //populate the list of new radio button choices
        int num_of_choices = 4; //number of radio buttons
        //Set text of radio buttons
        for (int i = 0; i < num_of_choices; i++) {
            RadioButton RButton = (RadioButton) mRadioGroup.getChildAt(i);
            RButton.setText(mMainPageAdapter.getRadioButtonChild(i));
        }
    }

    /**
     * The interface On fragment interaction listener.
     */
    public interface OnFragmentInteractionListener {

    }

}
