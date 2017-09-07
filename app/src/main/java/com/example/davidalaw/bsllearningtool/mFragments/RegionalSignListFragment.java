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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mActivities.SignMaterialActivity;
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;


/**
 * The type Regional sign list fragment.
 */
public class RegionalSignListFragment extends Fragment {

    private static final String TAG = RegionalSignListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private MainPageAdapter mMainPageAdapter;

    private String regionSelected;

    private ListView mListView;

    /**
     * Instantiates a new Regional sign list fragment.
     */
    public RegionalSignListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_regional_sign_list, container, false);

        String BUNDLE_STRING = "Region";
        regionSelected = getArguments().getString(BUNDLE_STRING); getActivity().setTitle(regionSelected); //set title to region selected name

        mListView = view.findViewById(R.id.region_signs_list); //instantiate ui element

        //call helper methods
        populateRegionSignList();
        setListViewListener();

        return view;
    }

    /**
     * Populate & display region sign list.
     */
    private void populateRegionSignList() {
        mMainPageAdapter = new MainPageAdapter();

        //create the list adapter
        ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                mMainPageAdapter.getRegionSigns(getContext(), regionSelected)); //populate
        mListView.setAdapter(adapter); //display list
    }

    /**
     * Sets list view listener.
     */
    private void setListViewListener() {

        //Action listener - intent to pass string objects to SignMaterialActivity
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), regionSelected + " : " +  mMainPageAdapter.getRegionSingleSign(i),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                intent.putExtra("sign", mMainPageAdapter.getSignIDSelected(i)); //store id
                intent.putExtra("name", mMainPageAdapter.getRegionSingleSign(i)); //store sign name
                Log.d(TAG, "Sign Selected: " + mMainPageAdapter.getRegionSingleSign(i));
                startActivity(intent); //move to sign material activity
            }
        });
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
     * The interface On fragment interaction listener.
     */
    public interface OnFragmentInteractionListener {

    }
}
