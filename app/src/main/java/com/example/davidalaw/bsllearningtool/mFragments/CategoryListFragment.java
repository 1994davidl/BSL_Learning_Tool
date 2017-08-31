package com.example.davidalaw.bsllearningtool.mFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;
import com.example.davidalaw.bsllearningtool.R;

public class CategoryListFragment extends Fragment {

    private static final String TAG = CategoryListFragment.class.getSimpleName();

    private MainPageAdapter mMainPageAdapter;

    private Class fragmentClass = null;

    private ListView listview;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category_list, container, false);

        String appName = "BSLearn"; getActivity().setTitle(appName);

        listview = view.findViewById(R.id.category_list_view);

        displayListView(view);
        listViewActionListener();

        return view;
    }

    /**
     * This method primary use is to get all data from the db table: Sign
     * then only display the Category names.
     *
     * @param view
     */
    private void displayListView(View view) {
        listview = view.findViewById(R.id.category_list_view);
        mMainPageAdapter = new MainPageAdapter();

        //create the list adapter
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_activated_1, mMainPageAdapter.collectDistinctCategories(getContext()));
        listview.setAdapter(adapter);
    }

    /**
     * Action listener to listen to user request when they select a chosen category
     * and open the sign list fragment.
     */
    private void listViewActionListener() {
        //Implement a click action listener to move to another fragment.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), mMainPageAdapter.getSelectedCategory(i),
                        Toast.LENGTH_SHORT).show();


                //Open Sign List Fragment
                Fragment fragment = null;
                fragmentClass = null;
                fragmentClass = SignListFragment.class;

                try {
                    //get selected category name and pass it to Signlistfragment Class
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putString("Category", mMainPageAdapter.getSelectedCategory(i));
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent,
                        fragment).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryListFragment.OnFragmentInteractionListener) {
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    public interface OnFragmentInteractionListener {

    }

}
