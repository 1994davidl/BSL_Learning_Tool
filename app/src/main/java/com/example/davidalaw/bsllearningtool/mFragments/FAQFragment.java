package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mModel_Controller.SupplementaryInfoAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class FAQFragment extends Fragment {

    private static final String TAG = FAQFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private SupplementaryInfoAdapter mSupplementaryInfoAdapter;

    private TextView [] mTextView;

    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        mTextView = new TextView[6];
        mTextView[0] = (TextView) view.findViewById(R.id.why_BSL_uppercase);
        mTextView[1] = (TextView) view.findViewById(R.id.why_mouth_signs);
        mTextView[2] = (TextView) view.findViewById(R.id.sign_location);
        mTextView[3] = (TextView) view.findViewById(R.id.sign_config);
        mTextView[4] = (TextView) view.findViewById(R.id.sign_action);
        mTextView[5] = (TextView) view.findViewById(R.id.sign_synonym);

        readResourceFile();
        populateTextView();

        return view;
    }

    public void populateTextView()
    {
       for(int i = 0; i < mTextView.length; i++) {
           mTextView[i].setText(mSupplementaryInfoAdapter.getFAQuestions(i));
       }
    }

    public void readResourceFile () {
        AssetManager assetManager = getActivity().getAssets();

        InputStream input; // To load text file
        Scanner in; //To read through text file
        mSupplementaryInfoAdapter = new SupplementaryInfoAdapter();

        try {
            input = assetManager.open("FAQ.txt");
            in = new Scanner(input);

            while(in.hasNextLine()) {
                String word = in.nextLine();
                mSupplementaryInfoAdapter.populateFAQuestionObjArray(word);
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
