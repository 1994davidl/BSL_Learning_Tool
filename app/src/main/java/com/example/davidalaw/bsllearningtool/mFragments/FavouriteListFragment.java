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
import android.widget.TextView;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.SignMaterialActivity;
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;


public class FavouriteListFragment extends Fragment {

    private static final String TAG = FavouriteListFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    private MainPageAdapter mMainPageAdapter;

    private ListView mListView;

    private TextView mTextView;

    public FavouriteListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_favourite_list, container, false);
        mListView = (ListView) view.findViewById(R.id.favourite_list_view);
        mTextView = (TextView) view.findViewById(R.id.text_Favourite);

        displayListView(view);
        listViewActionListener();

        return view;
    }

    public void displayListView(View view) {
        mMainPageAdapter = new MainPageAdapter();

        if (mMainPageAdapter.collectAllFavouriteSigns(getContext()).isEmpty()) {
            mTextView.setText("No Current Favourites");
        } else {
            //create the list adapter
            ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, mMainPageAdapter.collectAllFavouriteSigns(getContext()));
            mListView.setAdapter(adapter);
        }
    }

    public void listViewActionListener() {
        //Implement a click action listener to move to SignMaterialActivity.
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), mMainPageAdapter.getFavouriteSign(i), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                intent.putExtra("sign",mMainPageAdapter.getFavouriteSign(i));
                Log.d(TAG, "Sign Selected: " + mMainPageAdapter.getFavouriteSign(i));
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
