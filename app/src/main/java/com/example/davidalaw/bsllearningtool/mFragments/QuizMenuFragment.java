package com.example.davidalaw.bsllearningtool.mFragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mModel_Controller.MainPageAdapter;


/**
 * The type Quiz menu fragment.
 */
public class QuizMenuFragment extends Fragment {

    private static final String TAG = QuizMenuFragment.class.getSimpleName();

    private MainPageAdapter mMainPageAdapter; //view controllor class

    //initialise UI elements
    private ListView listview;
    private Button button;
    private NumberPicker numberPicker;

    //instance variables
    private int selected_num_picker =1;
    private String selectedFromList;

    /**
     * Instantiates a new Quiz menu fragment.
     */
    public QuizMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_menu, container, false);
        String TITLE_HANDLER = "Quiz"; getActivity().setTitle(TITLE_HANDLER); //set title of container

        //instantiate ui elements
        listview = view.findViewById(R.id.quiz_categories_list);
        button = view.findViewById(R.id.start_button);
        button.setEnabled(false); //disable button til user selects a category

        //call helper method
        displayCategoryOptions(); //display category list.
        listViewListener(); //list view action listener - obtain selected item
        buttonListener(); //display alert dialog

        return view ;
    }

    /*
     * Click action listener for the list view.
     *
     * display toast message which indicates to user of item they have selected
     */
    public void listViewListener () {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedFromList = listview.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), "Category selected: " + selectedFromList, Toast.LENGTH_SHORT).show();
                button.setEnabled(true); //enabled to progress to next step
            }
        });

    }

    /*
     * Display alerg dialog if button is clicked
     */
    public void buttonListener() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlertDialogListener();
            }
        });
    }

    /**
     * alert dialog action listener.
     *
     *
     *
     * @return the alert dialog
     */
    private void mAlertDialogListener() {
        View mdialog = getActivity().getLayoutInflater().inflate(R.layout.quiz_dialog, null);
        numberPicker = mdialog.findViewById(R.id.num_picker); //ui element

        numberPicker.setMinValue(1); //set minimum num of questions to 1
        numberPicker.setMaxValue(mMainPageAdapter.getCategoryQuestionsCounter(getContext(), selectedFromList)); //set maximum num of questions to 10

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        alertBuilder.setView(mdialog);

        //if set is selected, stored variables and dismiss dialog
        alertBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selected_num_picker = numberPicker.getValue();
                dialogInterface.dismiss(); //dismiss the dialog
                openQuizFragment(selected_num_picker); //call new fragment helper method
            }
        });

        //cancel dialog - back to quiz menu
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); //dimiss, back to listview display
            }
        });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show(); //display dialog
    }

    /**
     * Start fragment transaction.
     *
     * Send the name of category selected and the number of questions selected to the QuizFragment
     *
     * @param numberPicked
     */
    private void openQuizFragment(int numberPicked) {

        //Open Sign List Fragment
        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = QuizFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("Category", selectedFromList); //stored category name selected
            bundle.putString("Question",  String.valueOf(numberPicked)); //stored number of questions selected
            fragment.setArguments(bundle);

        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit(); //Start quiz
    }

    /**
     * Display the different available category that the user can quiz themselves on.
     */
    private void displayCategoryOptions() {
        mMainPageAdapter = new MainPageAdapter();
        //create the list adapter
        ListAdapter adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1,mMainPageAdapter.getQuizOptionalCategories(getContext()));
        listview.setAdapter(adapter);
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
