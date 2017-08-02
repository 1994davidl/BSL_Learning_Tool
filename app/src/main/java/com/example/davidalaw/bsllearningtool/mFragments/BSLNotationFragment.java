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


/**
 * Created by DavidALaw on 21/07/2017.
 */

public class BSLNotationFragment  extends Fragment {

    private static final String TAG ="BSLNotionFragment";

    private TextView mSignOccursText, mSignShapeText, mSignConfigText, mSignExpressText;
    private DBHandler mDBHandler;
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
        populateSignInfoView(view);

        return view;
    }


    private void populateSignInfoView (View view) {
        Log.d(TAG, "Populate BSL Notation View ");

        mSignOccursText = (TextView) view.findViewById(R.id.sign_occur_text);
        mSignShapeText = (TextView) view.findViewById(R.id.sign_shape_text);
        mSignConfigText = (TextView) view.findViewById(R.id.signs_action_text);
        mSignExpressText = (TextView) view.findViewById(R.id.expression_text);


        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllData();

        while(cursor.moveToNext()) {
            if(getSignSelected().equals(cursor.getString(2))) {
                mSignOccursText.setText(cursor.getString(5));
                mSignShapeText.setText(cursor.getString(6));
                mSignConfigText.setText(cursor.getString(7));
                mSignExpressText.setText(cursor.getString(8));
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public String getSignSelected() {
        return signSelected;
    }

    public void setSignSelected(String signSelected) {
        this.signSelected = signSelected;
    }
}
