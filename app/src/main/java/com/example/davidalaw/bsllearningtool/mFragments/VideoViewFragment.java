package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

public class VideoViewFragment extends Fragment {

    private static final String TAG = " VideoViewFragment";

    private DBHandler mDBHandler;

    private TextView mTextView;
    private String signSelected;

    public VideoViewFragment(String signSelected) {

        this.signSelected = signSelected;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_view, container, false);
        populateSignInfoView (view);

        return view;
    }

    private void populateSignInfoView (View view) {
        Log.d(TAG, "Populate Sign List View ");

        mTextView = (TextView) view.findViewById(R.id.textView);

        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllData();

        while(cursor.moveToNext()) {
            if(getSignSelected().equals(cursor.getString(2))) {
                mTextView.setText("\n\n"+cursor.getString(3));
            }

        }

    }

    public String getSignSelected() {
        return signSelected;
    }

    public void setSignSelected(String signSelected) {
        this.signSelected = signSelected;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
