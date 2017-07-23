package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.SignMaterialActivity;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    private OnFragmentInteractionListener mListener;

    private String mSignSelected;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_search, container, false);

        AutoCompleteTextView autoComplete = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        DBHandler mDBHandler = new DBHandler(getActivity());
        //Get a list of all names
        final ArrayList<String> allSignNames = mDBHandler.getAllSignNames();
        //Create array adapater to make selection available
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allSignNames);
        //Make the adapter available through the autocomplete name;
        autoComplete.setAdapter(stringArrayAdapter);


        mSignSelected = getArguments().getString("Category");
        Log.d(TAG, "Category Selected: StrText " + mSignSelected);

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                mSignSelected = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), mSignSelected, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                intent.putExtra("sign", mSignSelected);
                Log.d(TAG,"Sign Selected: " + mSignSelected);
                startActivity(intent);
            }
        });


        return view;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
