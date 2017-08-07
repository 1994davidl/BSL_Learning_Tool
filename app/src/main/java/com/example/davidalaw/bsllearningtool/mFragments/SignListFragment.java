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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.SignMaterialActivity;
import com.example.davidalaw.bsllearningtool.mAdapters.MainPageAdapter;
import com.example.davidalaw.bsllearningtool.R;


public class SignListFragment extends Fragment {

    private static final String TAG = SignListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private MainPageAdapter mMainPageAdapter;

    private ListView mListView;

    private static String categorySelected;

    public SignListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_list, container, false);
        categorySelected = getArguments().getString("Category");

        mListView= (ListView) view.findViewById(R.id.sign_list_view);
        populateSignListView();
        listViewActionListener();

        return view;
    }

    private void populateSignListView () {
        Log.d(TAG, "Populate Sign List View ");
        mMainPageAdapter = new MainPageAdapter();
        //create the list adapter
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, mMainPageAdapter.collectSignsFromSelectedCategory(getContext(), categorySelected));
        mListView.setAdapter(adapter);
    }

    public void listViewActionListener() {
        //Implement a click action listener to move to another fragment.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), mMainPageAdapter.getSignSelected(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                intent.putExtra("sign",  mMainPageAdapter.getSignSelected(i));
                Log.d(TAG,"Sign Selected: " +  mMainPageAdapter.getSignSelected(i));
                startActivity(intent);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
