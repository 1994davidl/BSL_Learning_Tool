package com.example.davidalaw.bsllearningtool.mActivities;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.AssetManager;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mFragments.CategoryListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.FAQFragment;
import com.example.davidalaw.bsllearningtool.mFragments.FavouriteListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.ProgressFragment;
import com.example.davidalaw.bsllearningtool.mFragments.QuizFragment;
import com.example.davidalaw.bsllearningtool.mFragments.QuizMenuFragment;
import com.example.davidalaw.bsllearningtool.mFragments.RegionalMapFragment;
import com.example.davidalaw.bsllearningtool.mFragments.RegionalSignListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.ResourcesFragment;
import com.example.davidalaw.bsllearningtool.mFragments.SearchFragment;
import com.example.davidalaw.bsllearningtool.mFragments.SignListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.VideoViewFragment;
import com.example.davidalaw.bsllearningtool.mModel_Controller.DBHandler;
import com.example.davidalaw.bsllearningtool.mData.QuestionBank;
import com.example.davidalaw.bsllearningtool.mData.Regions;
import com.example.davidalaw.bsllearningtool.mData.SignData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * <h1>BSLearn: BSL learning tool</h1>
 * <p>
 * This program implements an android application that provide learning material for those that
 * wish to learn British Sign Language. The current java class is the main activity which incorporates
 * a Navigation Drawer Activity template. This mobile design is built on fragments. The purpose this is class
 * is therefore to navigate to the fragment which the user wishes to be displayed.
 * <p>
 * This application is part of the author MSc Summer Project.
 *
 * @author DavidALaw
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.OnFragmentInteractionListener,
        FavouriteListFragment.OnFragmentInteractionListener, CategoryListFragment.OnFragmentInteractionListener,
        VideoViewFragment.OnFragmentInteractionListener, SignListFragment.OnFragmentInteractionListener,
        RegionalMapFragment.OnFragmentInteractionListener, RegionalSignListFragment.OnFragmentInteractionListener,
        QuizMenuFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener,
        ProgressFragment.OnFragmentInteractionListener, ResourcesFragment.OnFragmentInteractionListener,
        FAQFragment.OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName(); //TAG name for debugging to console.

    private BottomNavigationView mBottomNavigationView;

    private DBHandler mDBHandler;

    //string to pass object of the fragment the end user has selected.
    private String mFragmentSelected;
    private Class fragmentClass = null;

    //Array list to store the list of categories shown on navigation drawer menu.
    private ArrayList<String> listCategory;

    //File path of SQLite database.
    private static final String
            DB_PATH = "data/data/com.example.davidalaw.bsllearningtool/databases/BSL_Learning_Tool";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        doDBCheck(); //Read Text to populate database.
        setMainPageDisplay(savedInstanceState);

        Intent intent = getIntent();
        mFragmentSelected = intent.getStringExtra("fragment");

        if (mFragmentSelected != null) {
            backFromSignMaterialActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        setBottomNavigationView();

    }

    /**
     * if the database file exist do not init text files again.
     */
    private void doDBCheck() {
        try {
            File file = new File(DB_PATH);
            //file.delete();

            //If db file already exist, do not read the textfiles again.
            //else if it first time installed then call helper methods.
            if (!file.exists()) {
                readInitRegionTxtFile();
                readInitFileAddData();
                reaadInitQuestionTxtFile();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Sets display of main screen upon on create.
     *
     * By default the CategoryListFragment will be shown to the end user when the app starts up.
     *
     * @param savedInstanceState the saved instance state
     */
    private void setMainPageDisplay(Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            Fragment fragment = null;
            fragmentClass = CategoryListFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }
    }

    /**
     * An action listener for the bottom navigation view.
     * A switch statement to find requested case
     * and display one of the three fragments.
     */
    private void setBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home_nav:
                        fragmentClass = CategoryListFragment.class;
                        break;
                    case R.id.favourites_nav:
                        fragmentClass = FavouriteListFragment.class;
                        break;
                    case R.id.quiz_menu_nav:
                        fragmentClass = QuizMenuFragment.class;
                        break;
                }

                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                return false;
            }
        });

    }

    /**
     * This helper method is only utilised when the user selects
     * one of the three options of the bottom navigation view
     * while in the SignMaterialActivity class.
     * <p>
     * It finds which intent string was passed back to the main activity
     * then navigates the user to that fragment.
     */
    private void backFromSignMaterialActivity() {
        Fragment fragment = null;
        switch (mFragmentSelected) {
            case "Home":
                fragmentClass = CategoryListFragment.class;
                break;
            case "Favourites":
                fragmentClass = FavouriteListFragment.class;
                break;
            default:
                fragmentClass = QuizMenuFragment.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    /**
     * Navigation drawer categories list.
     *
     * @return list list
     */
    private void navigationCategories() {
        listCategory = new ArrayList<>();
        mDBHandler = new DBHandler(this);
        Cursor cursor = mDBHandler.getAllData();
        listCategory = new ArrayList<>();

        //get the value from the database from column 1 (Category name)
        //if Arraylist already contains the category then do not add to display
        while (cursor.moveToNext()) {
            if (!listCategory.contains(cursor.getString(1))) {
                listCategory.add(cursor.getString(1));
            }
        }
    }

    /**
     * Share intent. New android OS versions enable sharing to social media sites, versions 18 to 20 do not.
     */
    private void shareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain"); //intent type
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BSLearn App: A BSL Learning Tool"); //Header of Share content
        shareIntent.putExtra(Intent.EXTRA_TEXT, "I am learning British Sign Language with the BSLearn application."
                + "\n\nThis application will be available to download at Play Store in the near future."); //Body of share content
        startActivity(Intent.createChooser(shareIntent, "Share via: ")); //Choose title header.
    }

    ////////////////////////////// Read Text files upon installation ////////////////////////////////////////

    /**
     * Read region txt file.
     */
    private void readInitRegionTxtFile() {
        AssetManager assetManager = getAssets(); //access asset folder
        mDBHandler = new DBHandler(this);

        InputStream input; // To load text file
        Scanner in; //To read through text file

        try {
            input = assetManager.open("Regions.txt");
            in = new Scanner(input);

            while (in.hasNextLine()) {

                String word = in.nextLine();
                Regions regions = new Regions(word);
                mDBHandler.addRegions(new Regions(regions.getRegion_name(), regions.getLongitude(),
                        regions.getLatitude()));
            }
            in.close(); //close scanner and file.
        } catch (IOException e) {
            Log.e(TAG, "Exception Error " + e);
            e.printStackTrace();
        }
    }

    /**
     * AssetManager used to retrieve the text file - each line of the text file is read then the
     * string object is send to the model class (SignData) for the line to be tokenised suitable
     * and then afterward inserted into the database
     */
    private void readInitFileAddData() {
        AssetManager assetManager = getAssets(); //access asset folder.
        mDBHandler = new DBHandler(this);
        InputStream input; // To load text file
        Scanner in; //To read through text file

        try {
            input = assetManager.open("SignList.txt");
            in = new Scanner(input);

            while (in.hasNextLine()) {
                String word = in.nextLine();
                SignData signData = new SignData(word);
                //populate db table row with sign
                mDBHandler.addSign(new SignData(signData.getCategoryName(), signData.getSignName(),
                        signData.getbSLOrder(), signData.getSignSynonym(), signData.getSignOccurs(),
                        signData.getSignShape(), signData.getSignConfig(), signData.getSignExpress(),
                        signData.getVideo_file_path(), signData.getFavourite(), signData.getRegion_fk()));
            }
            in.close(); //close scanner and file.
        } catch (IOException e) {
            Log.e(TAG, "Exception Error " + e);
            e.printStackTrace();
        }
    }

    private void reaadInitQuestionTxtFile() {
        AssetManager assetManager = getAssets();
        mDBHandler = new DBHandler(this);

        InputStream input; // To load text file
        Scanner in; //To read through text file
        try {
            input = assetManager.open("QuestionBank.txt");
            in = new Scanner(input);

            while(in.hasNextLine()) {
                String word = in.nextLine();
                QuestionBank questionBank = new QuestionBank(word);
                mDBHandler.addQuestion(new QuestionBank(questionBank.getChoiceA(),
                        questionBank.getChoiceB(), questionBank.getChoiceC(), questionBank.getChoiceD(),
                        questionBank.getCorrectAnswer(), questionBank.getSign_fk()));
            }
            in.close(); //close scanner and file.
        } catch (IOException e) {
            Log.e(TAG, "Exception Error " + e);
            e.printStackTrace();
        }
    }

    ///////////////////////////////////// Override methods ///////////////////////////////////////////

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        navigationCategories();
        int id = item.getItemId();

        Fragment fragment = null; //init fragment
        fragmentClass = null;

        String category = ""; //empty string, populate with category selected
        /*
         * Navigate to the fragment upon user request.
         */
        switch(id) {
            case R.id.app_bar_search:
                fragmentClass = SearchFragment.class;
                break;
            case  R.id.nav_Home:
                fragmentClass = CategoryListFragment.class;
                break;
            case R.id.nav_favourite:
                fragmentClass = FavouriteListFragment.class;
                break;
            case R.id.nav_fingerspelling:
                category = listCategory.get(0);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.nav_numbers:
                category = listCategory.get(1);
                fragmentClass = SignListFragment.class;
                break;
            case  R.id.nav_colours:
                category = listCategory.get(2);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.nav_greetings:
                category = listCategory.get(3);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.nav_cities:
                category = listCategory.get(4);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.nav_family_members:
                category = listCategory.get(5);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.nav_work:
                category = listCategory.get(6);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.nav_personal_info:
                category = listCategory.get(7);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.nav_day:
                category = listCategory.get(8);
                fragmentClass = SignListFragment.class;
                break;
            case R.id.regional_signs:
                fragmentClass = RegionalMapFragment.class;
                break;
            case R.id.nav_quiz:
                fragmentClass = QuizMenuFragment.class;
                break;
            case R.id.nav_progress:
                fragmentClass = ProgressFragment.class;
                break;
            case  R.id.nav_bsl_info:
                fragmentClass = ResourcesFragment.class;
                break;
            case  R.id.FAQ:
                fragmentClass = FAQFragment.class;
                break;
            case R.id.nav_share:
                shareIntent(); //share application
                fragmentClass = CategoryListFragment.class; //return to home screen after share content
                break;
        }

        try {
            fragment = (Fragment) (fragmentClass != null ? fragmentClass.newInstance() : null);
            Bundle bundle = new Bundle(); //store data
            bundle.putString("Category", category); //Add which of the categories were selected.
            assert fragment != null;
            fragment.setArguments(bundle); //add bundle argument

        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit(); //start transaction to navigation option selected

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * All fragment transactions are added to a stack before fragment transaction.
     * In the event of the user presses back then the back of the stack is popped to return to the previous
     * fragment that was displayed before the current fragment.
     */
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }


}
