package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

public class ProgressFragment extends Fragment {

    private static final String TAG = ProgressFragment.class.getSimpleName();
    private TextView[] mTextViews;
    private DBHandler mDBHandler;

    private int totalScore = 0;
    private int totalRounds =0;
    private int bestRound = 0;
    private int num_of_rounds = 0;
    private int best = 0;

    private OnFragmentInteractionListener mListener;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        getActivity().setTitle("Progress: Statistics");

        int SIZE = 7;
        mTextViews = new TextView[SIZE];
        mTextViews[0] = view.findViewById(R.id.top_score);
        mTextViews[1] = view.findViewById(R.id.best_round);
        mTextViews[2] = view.findViewById(R.id.last_round_score);
        mTextViews[3] = view.findViewById(R.id.last_round_category);
        mTextViews[4] = view.findViewById(R.id.top_category);
        mTextViews[5] = view.findViewById(R.id.worst_category);
        mTextViews[6] = view.findViewById(R.id.attempted_quizzes);

        changeTextViewsUponTablePopulate();

        return view;
    }

    private void changeTextViewsUponTablePopulate() {

        mDBHandler = new DBHandler(getActivity());
        if (mDBHandler.checkProgressTablePopulated()) {
            totalScore();
            bestRound();
            lastRoundScore();
            topCategory();
            getAttemptedQuizCount();
            worstCategory();
        }
    }


    private void totalScore() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllProgress();

        while (cursor.moveToNext()) {
            totalScore += Integer.parseInt(cursor.getString(2));
            totalRounds +=  Integer.parseInt(cursor.getString(4));
        }
        mTextViews[0].setText(String.valueOf(totalScore) + "/" + String.valueOf(totalRounds));
    }

    private void bestRound() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllProgress();

        while (cursor.moveToNext()) {
            if (Integer.parseInt(cursor.getString(2)) > bestRound) {
                bestRound = Integer.parseInt(cursor.getString(2));
                num_of_rounds = Integer.parseInt(cursor.getString(4));
            }
        }
        mTextViews[1].setText(bestRound + "/"+num_of_rounds);
    }

    private void topCategory() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getDistinctSumValues();

        while (cursor.moveToNext()) {
            if (Integer.parseInt(cursor.getString(1)) > best) {
                best = Integer.parseInt(cursor.getString(1));
                mTextViews[4].setText(cursor.getString(0));
            }
        }
    }

    private void worstCategory() {

        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getDistinctSumValues();

        int worst = best;
        while (cursor.moveToNext()) {
            if (Integer.parseInt(cursor.getString(1)) < worst) {
                worst = Integer.parseInt(cursor.getString(1));
                mTextViews[5].setText(cursor.getString(0));
            }
        }
    }

    private void lastRoundScore() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllProgress();

        while (cursor.moveToNext()) {
            if (cursor.getCount() > 1) {
                cursor.moveToPosition(cursor.getCount() - 1);
                Log.d(TAG, "last entry " + cursor.getString(1));
                mTextViews[2].setText(cursor.getString(2) + "/" + cursor.getString(4));
                mTextViews[3].setText(cursor.getString(1));

            } else {
                mTextViews[2].setText(cursor.getString(2));
                mTextViews[3].setText(cursor.getString(1));
            }
        }
    }

    private void getAttemptedQuizCount() {
        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllProgress();
        mTextViews[6].setText(String.valueOf(cursor.getCount()));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

    }


}
