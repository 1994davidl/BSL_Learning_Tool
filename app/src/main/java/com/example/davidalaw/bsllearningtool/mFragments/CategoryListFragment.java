package com.example.davidalaw.bsllearningtool.mFragments;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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

import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;
import com.example.davidalaw.bsllearningtool.MainActivity;
import com.example.davidalaw.bsllearningtool.R;

import java.util.ArrayList;

public class CategoryListFragment extends Fragment {

    private static final String TAG = "CategoryListFragment";

    private OnFragmentInteractionListener mListener;

    private Class fragmentClass = null;
    private DBHandler mDBHandler;
    private ListView listview;
    private ArrayList<String> listData;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list, container, false);
        this.displayListView(view); //helper method to display category content

        listview = (ListView) view.findViewById(R.id.category_list_view);

        //Implement a click action listener to move to another fragment.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), listData.get(i), Toast.LENGTH_SHORT).show();
                SignListFragment mSign = new SignListFragment();

                //Open Sign List Fragment
                Fragment fragment = null;
                fragmentClass = null;
                fragmentClass = SignListFragment.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //get selected category name and pass it to signlistfragment Class
                String category = listData.get(i);
                Bundle bundle = new Bundle();
                bundle.putString("Category", category);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

            }
        });

        return view;
    }

    /**
     * This method primary use is to get all data from the db table: Sign
     * then only display the Category names.
     *
     * @param view
     */
    private void displayListView(View view) {
                listview = (ListView) view.findViewById(R.id.category_list_view);
                listview.setCacheColorHint(Color.BLACK);

                mDBHandler = new DBHandler(getActivity());
                Cursor cursor = mDBHandler.getAllData();

                listData = new ArrayList<>();

                //get the value from the database from column 1 (Category name)
                //if Arraylist already contains the category then do not add to display
                while(cursor.moveToNext()) {
                    if(!listData.contains(cursor.getString(1))) {
                        listData.add(cursor.getString(1));
                    }
                }

                //create the list adapter
                ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_activated_1, listData);
                listview.setAdapter(adapter);
            }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryListFragment.OnFragmentInteractionListener) {
            mListener = (CategoryListFragment.OnFragmentInteractionListener) context;
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
