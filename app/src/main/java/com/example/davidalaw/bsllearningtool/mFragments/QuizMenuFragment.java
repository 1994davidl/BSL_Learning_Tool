package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.QuestionBank;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;


public class QuizMenuFragment extends Fragment {

    private static final String TAG = QuizMenuFragment.class.getSimpleName();

    private DBHandler mDBHandler;
    private ListView listview;
    private Spinner mSpinner;
    private Button mButton;
    private ArrayList<String> mListData, spinnerList;
    private Class fragmentClass = null;
    private String selectedFromList, numberSelected;
    private QuestionBank mQuestionBank;

    private OnFragmentInteractionListener mListener;

    public QuizMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz_menu, container, false);
        readFileAddQuestions();
        displayCategoryOptions(view);
        populateDropDown(view);

        listview = (ListView) view.findViewById(R.id.quiz_categories_list);
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mButton = (Button) view.findViewById(R.id.start_button);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFromList = listview.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), "Category selected: " + selectedFromList, Toast.LENGTH_SHORT).show();
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                numberSelected = mSpinner.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                numberSelected = mSpinner.toString();
            }
        });

        mButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                QuizFragment mQuiz = new QuizFragment();

                //Open Sign List Fragment
                Fragment fragment = null;
                fragmentClass = null;
                fragmentClass = QuizFragment.class;

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //get selected category name and pass it to Quiz fragment Class
                Bundle bundle1 = new Bundle();
                bundle1.putString("Category", selectedFromList);
                fragment.setArguments(bundle1);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

                return false;
            }
        });


        return view ;
    }

    public void populateDropDown(View view) {
        Spinner sItems = (Spinner) view.findViewById(R.id.spinner);
        spinnerList = new ArrayList<>();
        for(int i =5; i <=10; i++) {
            spinnerList.add(Integer.toString(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sItems.setAdapter(adapter);
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

    private void displayCategoryOptions(View view) {
        listview = (ListView) view.findViewById(R.id.quiz_categories_list);

        mDBHandler = new DBHandler(getActivity());
        Cursor cursor = mDBHandler.getAllQuestions();

        mListData = new ArrayList<>();

        //get the value from the database from column 1 (Category name)
        //if Arraylist already contains the category then do not add to display
        while (cursor.moveToNext()) {
            if (!mListData.contains(cursor.getString(1))) {
                mListData.add(cursor.getString(1));
            }
        }
        //create the list adapter
        ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1, mListData);
        listview.setAdapter(adapter);
    }

    public void readFileAddQuestions () {
        AssetManager assetManager = getActivity().getAssets();
        mDBHandler = new DBHandler(getActivity());

        InputStream input; // To load text file
        Scanner in; //To read through text file

        try {
            input = assetManager.open("QuestionBank.txt");
            in = new Scanner(input);

            while(in.hasNextLine()) {

                String word = in.nextLine();
                mQuestionBank = new QuestionBank(word);

                mDBHandler.addQuestion(new QuestionBank(mQuestionBank.getCategory(), mQuestionBank.getVideoURI(),
                        mQuestionBank.getAnswerA(), mQuestionBank.getAnswerB(), mQuestionBank.getAnswerC(), mQuestionBank.getAnswerD(),
                        mQuestionBank.getCorrectAnswer()));
            }
            in.close(); //close scanner and file.
        } catch (IOException e) {
            Log.e(TAG, "Exception Error " + e);
            e.printStackTrace();
        }
    }
}
