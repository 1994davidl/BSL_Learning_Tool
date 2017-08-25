package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.Intent;
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
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;


/**
 * The type Search fragment.
 */
public class SearchFragment extends Fragment {

    private static final String TAG = SearchFragment.class.getSimpleName();

    private String mSignSelected;

    private MainPageAdapter mMainPageAdapter; //model class

    private  AutoCompleteTextView mAutoCompleteTextView; //suggest sign names that are similar to what the user is currently typing

    /**
     * Instantiates a new Search fragment.
     */
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_search, container, false);

        mAutoCompleteTextView = view.findViewById(R.id.autoCompleteTextView); //initialise listview

        setAutoCompleteTextView (); //get sign names from model class and display
        autoCompleteActionListener(); //Action listener move to SignMaterialActivity on click.
        return view;
    }

    private void setAutoCompleteTextView() {
        mMainPageAdapter = new MainPageAdapter();//instantiate ModelClass.
        //Create array adapater to make selection available
        ArrayAdapter<String> stringArrayAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                        mMainPageAdapter.getSearchableSigns(getContext()));
        //Make the adapter available through the autocomplete name;
        mAutoCompleteTextView.setAdapter(stringArrayAdapter);
    }

    /**
     * Auto complete action listener.
     */
    private void autoCompleteActionListener() {
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSignSelected = adapterView.getItemAtPosition(i).toString();
                mMainPageAdapter.getSearchableSignsID(getContext(),mSignSelected);
                Toast.makeText(getActivity(), mSignSelected, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                intent.putExtra("sign", mMainPageAdapter.getSignIDSelected(0));
                intent.putExtra("name", mSignSelected);
                Log.d(TAG,"Sign Selected: " + mSignSelected);
                startActivity(intent);
            }
        });

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

    /**
     * The interface On fragment interaction listener.
     */
    public interface OnFragmentInteractionListener {

    }
}
