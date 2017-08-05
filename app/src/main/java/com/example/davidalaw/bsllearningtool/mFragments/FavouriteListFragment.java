package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import java.util.ArrayList;
import java.util.List;


public class FavouriteListFragment extends Fragment {

    private static final String TAG = FavouriteListFragment.class.getSimpleName();
    private final String empty ="No Favourite Signs";

    private OnFragmentInteractionListener mListener;
    private ListView mListView;
    private TextView mTextView;
    private DBHandler mDBHandler;
    private List <String> mListData;
    private String signSelected;

    private View view;

    public FavouriteListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_favourite_list, container, false);
        mListView = (ListView) view.findViewById(R.id.favourite_list_view);
        mTextView = (TextView) view.findViewById(R.id.text_Favourite);

        displayListView(view);

            //Implement a click action listener to move to SignMaterialActivity.
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getActivity(), mListData.get(i), Toast.LENGTH_SHORT).show();
                    signSelected = mListData.get(i);
                    Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                    intent.putExtra("sign", signSelected);
                    Log.d(TAG, "Sign Selected: " + signSelected);
                    startActivity(intent);
                }

            });
        return view;
    }



    public List displayListView(View view) {
        mListView = (ListView) view.findViewById(R.id.favourite_list_view);

        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllData();

        mListData = new ArrayList<>();

        //get the value from the database from column 1 (Category name)
        //if Arraylist already contains the category then do not add to display
        while(cursor.moveToNext()) {
            if(Integer.parseInt(cursor.getString(10)) > 0) {
                mListData.add(cursor.getString(2));
                /*if(!mListData.contains(cursor.getString(2))) {

                }*/
            }
        }

            //create the list adapter
            ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, mListData);
            mListView.setAdapter(adapter);

        return mListData;
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

    @Override
    public void onResume() {
        super.onResume();
        displayListView(view);

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
