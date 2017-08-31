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
public class BSLNotationFragment  extends Fragment {

    private static final String TAG = BSLNotationFragment.class.getSimpleName();

    private TextView [] mTextView;

    private int signSelected;

    public BSLNotationFragment() {
    }

    public BSLNotationFragment(int signSelected) {
        this.signSelected = signSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bsl_notation_fragment, container, false);

        int SIZE = 4;
        mTextView = new TextView[SIZE];
        mTextView[0] = view.findViewById(R.id.sign_occur_text);
        mTextView[1]= view.findViewById(R.id.sign_shape_text);
        mTextView[2] = view.findViewById(R.id.signs_action_text);
        mTextView[3] = view.findViewById(R.id.expression_text);
        populateSignInfoView();

        return view;
    }


    private void populateSignInfoView () {
        Log.d(TAG, "Populate BSL Notation View ");
        SignMaterialAdapter signMaterialAdapter = new SignMaterialAdapter();
        signMaterialAdapter.populateBSLNotation(getContext(), signSelected);

        for(int i = 0; i < mTextView.length; i++) {
            mTextView[i].setText(signMaterialAdapter.getBSLNotation(i));
        }
    }


}
