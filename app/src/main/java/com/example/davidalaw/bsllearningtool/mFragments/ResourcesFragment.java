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
import com.example.davidalaw.bsllearningtool.mAdapters.ResourcesAdapter;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.QuestionBank;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;


public class ResourcesFragment extends Fragment {


    private static final String TAG = ResourcesFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private ResourcesAdapter mResourcesAdapter;

    private TextView mWhatBSL;

    public ResourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resources, container, false);
        readResourceFile();


        mWhatBSL = (TextView) view.findViewById(R.id.what_BSL);


        populateTextView();

        return view;
    }

    public void populateTextView() {

        mWhatBSL.setText(mResourcesAdapter.getInformation(0));

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


    public void readResourceFile () {
        AssetManager assetManager = getActivity().getAssets();

        InputStream input; // To load text file
        Scanner in; //To read through text file
        mResourcesAdapter = new ResourcesAdapter();

        try {
            input = assetManager.open("resources.txt");
            in = new Scanner(input);

            while(in.hasNextLine()) {

                String word = in.nextLine();
                mResourcesAdapter.populateResourcesObjArray(word);
            }
            in.close(); //close scanner and file.
        } catch (IOException e) {
            Log.e(TAG, "Exception Error " + e);
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
