package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class FAQFragment extends Fragment {

    private static final String TAG = FAQFragment.class.getSimpleName();

    private MainPageAdapter mMainPageAdapter;

    private TextView [] mTextView;

    private final int SIZE = 6;

    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        String TITLE_HANDLER = "FAQs"; getActivity().setTitle(TITLE_HANDLER); //set container title

        mTextView = new TextView[SIZE];
        //instantiate UI elements
        mTextView[0] = view.findViewById(R.id.why_BSL_uppercase);
        mTextView[1] = view.findViewById(R.id.why_mouth_signs);
        mTextView[2] = view.findViewById(R.id.sign_location);
        mTextView[3] = view.findViewById(R.id.sign_config);
        mTextView[4] = view.findViewById(R.id.sign_action);
        mTextView[5] = view.findViewById(R.id.sign_synonym);

        //Call helper methods
        readResourceFile();
        populateTextView();

        return view;
    }

    private void populateTextView()
    {
       for(int i = 0; i < mTextView.length; i++) {
           mTextView[i].setText(mMainPageAdapter.getFAQuestions(i));
       }
    }

    //Read text files where answers to questions have been reversed.
    private void readResourceFile() {
        AssetManager assetManager = getActivity().getAssets();

        InputStream input; // To load text file
        Scanner in; //To read through text file
        mMainPageAdapter = new MainPageAdapter();

        try {
            input = assetManager.open("FAQ.txt");
            in = new Scanner(input);

            while(in.hasNextLine()) {
                String word = in.nextLine();
                mMainPageAdapter.populateFAQuestionObjArray(word); //call controllor helper method.
            }
            in.close(); //close scanner and file.
        } catch (IOException e) {
            Log.e(TAG, "Exception Error " + e);
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentInteractionListener {
    }
}
