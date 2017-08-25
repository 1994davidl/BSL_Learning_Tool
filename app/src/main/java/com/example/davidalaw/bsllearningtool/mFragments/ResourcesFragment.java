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


public class ResourcesFragment extends Fragment {


    private static final String TAG = ResourcesFragment.class.getSimpleName();

    private MainPageAdapter mMainPageAdapter;

    private TextView [ ] mTextView;


    public ResourcesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resources, container, false);
        String titleHandler = "BSL & Deaf Awareness";
        getActivity().setTitle(titleHandler);

        mTextView = new TextView[4];
        mTextView[0] = view.findViewById(R.id.what_BSL);
        mTextView[1] = view.findViewById(R.id.what_fingerspelling);
        mTextView[2] = view.findViewById(R.id.text_deaf_culture);
        mTextView[3] = view.findViewById(R.id.text_misconceptions);


        readInitResourceFile();
        populateTextView();
        return view;
    }

    private void populateTextView() {
        for(int i = 0; i < mTextView.length; i++) {
            mTextView[i].setText(mMainPageAdapter.getInformation(i));
        }

    }

    private void readInitResourceFile() {
        AssetManager assetManager = getActivity().getAssets(); //collect txt file from Asset Folder

        InputStream input; // To load text file
        Scanner in; //To read through text file
        mMainPageAdapter = new MainPageAdapter();

        try {
            input = assetManager.open("Resources.txt");
            in = new Scanner(input);

            while(in.hasNextLine()) {
                String word = in.nextLine();
                mMainPageAdapter.populateResourcesObjArray(word); //pass to model class
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
