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
import com.example.davidalaw.bsllearningtool.mModel_Controller.DBHandler;
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;

public class ProgressFragment extends Fragment {

    private TextView[] mTextViews;

    private MainPageAdapter mMainPageAdapter; //view controllor class

    private final int SIZE = 6;

    private OnFragmentInteractionListener mListener;

    public ProgressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        getActivity().setTitle("Progress: Statistics"); //set title

        mTextViews = new TextView[SIZE];

        //instantiate ui elements
        mTextViews[0] = view.findViewById(R.id.top_score);
        mTextViews[1] = view.findViewById(R.id.best_round);
        mTextViews[2] = view.findViewById(R.id.last_round_score);
        mTextViews[3] = view.findViewById(R.id.top_category);
        mTextViews[4] = view.findViewById(R.id.worst_category);
        mTextViews[5] = view.findViewById(R.id.attempted_quizzes);

        mMainPageAdapter = new MainPageAdapter(); //create object

        //called helper method
        changeTextViewsUponTablePopulate();

        return view;
    }

    //Check that progress table is populated. else make no changes to template.
    private void changeTextViewsUponTablePopulate() {

        if(mMainPageAdapter.checkifProgressTablePopulated(getContext())) {
            DisplayProgressInformation(); //call helper method
        }
    }

    /**
     * Populate text views elements.
     */
    private void DisplayProgressInformation () {

        mMainPageAdapter.getallProgressInfo(getContext());

        for(int i =0; i < mTextViews.length; i++) {
            mTextViews[i].setText(mMainPageAdapter.getProgressList(i));
        }
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
