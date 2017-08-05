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

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class FAQFragment extends Fragment {

    private static final String TAG = FAQFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private ResourcesAdapter mResourcesAdapter;

    private TextView mTextView1, mTextView2;

    public FAQFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        mTextView1 = (TextView) view.findViewById(R.id.why_BSL_uppercase);
        mTextView2 = (TextView) view.findViewById(R.id.why_mouth_signs);
        readResourceFile();
        populateTextView();


        return view;
    }

    public void populateTextView() {
        mTextView1.setText(mResourcesAdapter.getFAQuestions(0));
        mTextView2.setText(mResourcesAdapter.getFAQuestions(1));


    }

    public void readResourceFile () {
        AssetManager assetManager = getActivity().getAssets();

        InputStream input; // To load text file
        Scanner in; //To read through text file
        mResourcesAdapter = new ResourcesAdapter();

        try {
            input = assetManager.open("FAQ.txt");
            in = new Scanner(input);

            while(in.hasNextLine()) {
                String word = in.nextLine();
                mResourcesAdapter.populateFAQuestionObjArray(word);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
