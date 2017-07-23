package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;
import com.example.davidalaw.bsllearningtool.R;

import java.util.ArrayList;


public class SignListFragment extends Fragment {

    private static final String TAG = "SignListFragment";
    private OnFragmentInteractionListener mListener;

    private DBHandler mDBHandler;
    private ListView listview;

    private static String categorySelected, signSelected;
    private ArrayList<String> listData;
    private Class fragmentClass = null;

    public SignListFragment() {
        // Required empty public constructor
    }



    public SignListFragment(String mCategorySelected) {
        this.categorySelected = mCategorySelected;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_list, container, false);

        categorySelected= getArguments().getString("Category");
        Log.d(TAG, "Category Selected: StrText " + categorySelected);
        populateSignListView(view);

        listview = (ListView) view.findViewById(R.id.sign_list_view);

        //Implement a click action listener to move to another fragment.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), listData.get(i), Toast.LENGTH_SHORT).show();

                signSelected = listData.get(i);

                Intent intent = new Intent(getActivity(), SignMaterialActivity.class);
                intent.putExtra("sign", signSelected);
                Log.d(TAG,"Sign Selected: " + signSelected);
                startActivity(intent);


            }
        });



        return view;
    }

            private void populateSignListView (View view) {
                Log.d(TAG, "Populate Sign List View ");

                ListView mListView = (ListView) view.findViewById(R.id.sign_list_view);

                mDBHandler = new DBHandler(getActivity());
                Cursor cursor = mDBHandler.getAllData();

                listData = new ArrayList<>();

                while(cursor.moveToNext()) {
                    //get the value from the database from column 2 (Sign Name)
                    //then add it to the array list
                    if(categorySelected.equals(cursor.getString(1))) {
                        listData.add(cursor.getString(2));
                    } else {
                        continue;
                    }

                }
                //create the list adapter
                ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, listData);
                mListView.setAdapter(adapter);
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
