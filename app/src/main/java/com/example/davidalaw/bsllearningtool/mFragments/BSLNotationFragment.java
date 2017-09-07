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
 * BSL Notation view class. It utilise the Stokoe notation system
 *
 * This view consists of four separate text views. The name of the sign is passed in and then the
 * controllor class helper method is called to collect the necessary data to populate text views.
 *
 */
public class BSLNotationFragment  extends Fragment {

    private TextView [] mTextView; //GUI Components

    private int signSelected; //name of size

    private final int SIZE = 4; //number of notation parameters

    public BSLNotationFragment() {
    }

    //Constructor - sign selected it passed to get appropriate info from controllor class.
    public BSLNotationFragment(int signSelected) {
        this.signSelected = signSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bsl_notation_fragment, container, false);

        mTextView = new TextView[SIZE];

        //init ui components to xml IDs
        mTextView[0] = view.findViewById(R.id.sign_occur_text);
        mTextView[1]= view.findViewById(R.id.sign_shape_text);
        mTextView[2] = view.findViewById(R.id.signs_action_text);
        mTextView[3] = view.findViewById(R.id.expression_text);

        populateSignInfoView(); //call helper method to populate text views

        return view;
    }


    private void populateSignInfoView () {
        SignMaterialAdapter signMaterialAdapter = new SignMaterialAdapter(); //init view controllor class
        signMaterialAdapter.populateBSLNotation(getContext(), signSelected);  //call controller helper method

        for(int i = 0; i < mTextView.length; i++) {
            mTextView[i].setText(signMaterialAdapter.getBSLNotation(i)); //populate
        }
    }
}
