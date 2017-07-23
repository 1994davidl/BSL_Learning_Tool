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
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import java.util.ArrayList;
/**
 * Created by DavidALaw on 21/07/2017.
 */

public class SignInformationFragment extends Fragment {

    private static final String TAG = "SignInformationFragment";

    private TextView mBSLtextview, mEnglishTextView, mSignSynonymTextView;
    private DBHandler mDBHandler;
    private String signSelected;

    public SignInformationFragment() {
    }

    public SignInformationFragment(String signSelected) {
        this.signSelected = signSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_info, container, false);
        populateSignInfoView(view);
        return view;
    }

    private void populateSignInfoView (View view) {
        Log.d(TAG, "Populate Sign List View ");

        mBSLtextview = (TextView) view.findViewById(R.id.BSL_sign_order);
        mEnglishTextView = (TextView) view.findViewById(R.id.english_name);
        mSignSynonymTextView = (TextView) view.findViewById(R.id.similar_signs);

        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllData();

        while(cursor.moveToNext()) {
            if(getSignSelected().equals(cursor.getString(2))) {
                mBSLtextview.setText(cursor.getString(3));
                mEnglishTextView.setText(cursor.getString(2));
                mSignSynonymTextView.setText(cursor.getString(4));
            }
        }
    }

    public String getSignSelected() {
        return signSelected;
    }

    public void setSignSelected(String signSelected) {
        this.signSelected = signSelected;
    }
}
