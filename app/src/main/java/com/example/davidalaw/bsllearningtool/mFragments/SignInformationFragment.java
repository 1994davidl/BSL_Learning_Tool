package com.example.davidalaw.bsllearningtool.mFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mModel_Controller.SignMaterialAdapter;

/**
 * Created by DavidALaw on 21/07/2017.
 */

public class SignInformationFragment extends Fragment {

    private static final String TAG = SignInformationFragment.class.getSimpleName();

    private TextView mTextView [];

    private static final int SIZE = 3;

    private int signSelected;

    public SignInformationFragment() {
    }

    public SignInformationFragment(int signSelected) {
        this.signSelected = signSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_info, container, false);

        mTextView = new TextView[SIZE];
        //instantiate ui elements
        mTextView[0] = view.findViewById(R.id.english_name);
        mTextView[1] = view.findViewById(R.id.BSL_sign_order);
        mTextView[2] = view.findViewById(R.id.similar_signs);

        //call helper method
        populateSignInfoView();

        return view;
    }


    /**
     * Populate list view
     */
    private void populateSignInfoView () {
        Log.d(TAG, "Populate Sign List View ");
        SignMaterialAdapter signMaterialAdapter = new SignMaterialAdapter();
        signMaterialAdapter.populateSignInfoFrag(getContext(), signSelected);

        for(int i =0; i < mTextView.length; i++) {
            mTextView[i].setText(signMaterialAdapter.getSignInfo(i));
        }
    }
}
