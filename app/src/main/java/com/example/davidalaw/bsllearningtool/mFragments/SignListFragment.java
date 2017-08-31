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

import com.example.davidalaw.bsllearningtool.mActivities.SignMaterialActivity;
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;
import com.example.davidalaw.bsllearningtool.R;


/**
 * The type Sign list fragment.
 */
public class SignListFragment extends Fragment {

    private static final String TAG = SignListFragment.class.getSimpleName();
    private static String categorySelected;

    private MainPageAdapter mMainPageAdapter;

    private ListView mListView;

    /**
     * Instantiates a new Sign list fragment.
     */
    public SignListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_list, container, false);

        String INTENTSTRING = "Category"; categorySelected = getArguments().getString(INTENTSTRING); //get category selected from bundle argument
        getActivity().setTitle(categorySelected); //set title to the category selected

        mListView = view.findViewById(R.id.sign_list_view);

        //Call Helper methods
        populateSignListView();
        listViewActionListener();

        return view;
    }


    /**
     * Populate sign list view.
     */
    private void populateSignListView() {
        Log.d(TAG, "Populate Sign List View ");
        mMainPageAdapter = new MainPageAdapter();

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1,
                mMainPageAdapter.collectSignsFromSelectedCategory(getContext(), categorySelected));
        mListView.setAdapter(adapter); //create the list view adapter
    }


    /**
     * List view action listener.
     */
    private void listViewActionListener() {
        //Implement a click action listener to move to another fragment.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), mMainPageAdapter.getSignSelected(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                intent.putExtra("sign", mMainPageAdapter.getSignIDSelected(i));
                intent.putExtra("name", mMainPageAdapter.getSignSelected(i));
                Log.d(TAG, "Sign Selected: " + mMainPageAdapter.getSignIDSelected(i));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            OnFragmentInteractionListener listener = (OnFragmentInteractionListener) context;
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
