package com.example.davidalaw.bsllearningtool.mFragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mAdapters.SignMaterialAdapter;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;


/**
 * Created by DavidALaw on 21/07/2017.
 */

public class BSLNotationFragment  extends Fragment {

    private static final String TAG = BSLNotationFragment.class.getSimpleName();

    private SignMaterialAdapter mSignMaterialAdapter;

    private TextView [] mTextView;

    private final int SIZE = 4;

    private String signSelected;

    public BSLNotationFragment() {
    }

    public BSLNotationFragment(String signSelected) {
        this.signSelected = signSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bsl_notation_fragment, container, false);

        mTextView = new TextView[SIZE];
        mTextView[0] = (TextView) view.findViewById(R.id.sign_occur_text);
        mTextView[1]= (TextView) view.findViewById(R.id.sign_shape_text);
        mTextView[2] = (TextView) view.findViewById(R.id.signs_action_text);
        mTextView[3] = (TextView) view.findViewById(R.id.expression_text);
        populateSignInfoView();

        return view;
    }


    private void populateSignInfoView () {
        Log.d(TAG, "Populate BSL Notation View ");
        mSignMaterialAdapter = new SignMaterialAdapter();
        mSignMaterialAdapter.populateBSLNotation(getContext(), signSelected);

        for(int i = 0; i < mTextView.length; i++) {
            mTextView[i].setText(mSignMaterialAdapter.getBSLNotation(i));
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

}
