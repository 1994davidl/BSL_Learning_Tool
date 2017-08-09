package com.example.davidalaw.bsllearningtool;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.res.AssetManager;
import android.net.Uri;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.davidalaw.bsllearningtool.mFragments.CategoryListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.FAQFragment;
import com.example.davidalaw.bsllearningtool.mFragments.FavouriteListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.ProgressFragment;
import com.example.davidalaw.bsllearningtool.mFragments.QuizFragment;
import com.example.davidalaw.bsllearningtool.mFragments.QuizMenuFragment;
import com.example.davidalaw.bsllearningtool.mFragments.ResourcesFragment;
import com.example.davidalaw.bsllearningtool.mFragments.SearchFragment;
import com.example.davidalaw.bsllearningtool.mFragments.SignListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.VideoViewFragment;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.SignData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.OnFragmentInteractionListener,
        FavouriteListFragment.OnFragmentInteractionListener, CategoryListFragment.OnFragmentInteractionListener, VideoViewFragment.OnFragmentInteractionListener,
        SignListFragment.OnFragmentInteractionListener, QuizMenuFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener,
        ProgressFragment.OnFragmentInteractionListener, ResourcesFragment.OnFragmentInteractionListener, FAQFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final String DB_PATH = "data/data/com.example.davidalaw.bsllearningtool/databases/BSL_Learning_Tool";

    private Class fragmentClass = null;
    private NavigationView navigationView;
    private BottomNavigationView mBottomNavigationView;

    private SignData mSignData;
    private DBHandler mDBHandler;
    private String mFragmentSelected;
    private ArrayList<String> listCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        doDBCheck();
        //Read Text to populate database.
        setOpeningScreenDisplay(savedInstanceState);

        Intent intent = getIntent();
        mFragmentSelected = intent.getStringExtra("fragment");
        if (mFragmentSelected != null) {
            Log.d(TAG, "FRAGMENT SELECTED " + mFragmentSelected);
            backFromSignMaterialActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        setBottomNavigationView();

    }

    public void setOpeningScreenDisplay(Bundle savedInstanceState) {
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

    public void setBottomNavigationView() {
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

    public void backFromSignMaterialActivity() {

        Fragment fragment = null;
        if (mFragmentSelected.equals("Home")) {
            fragmentClass = CategoryListFragment.class;
        } else if (mFragmentSelected.equals("Favourites")) {
            fragmentClass = FavouriteListFragment.class;
        } else {
            fragmentClass = QuizMenuFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
    }

    //TODO: solve db reduplication
    private void doDBCheck() {
        try {
            File file = new File(DB_PATH);
            //file.delete();
            if (!file.exists()) {
                this.readFileAddData();
            } else {
                return;
            }
        } catch (Exception ex) {

        }
    }

    /**
     * All fragment transactions are added to the back stack before commit.
     */
    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * Navigate the user to the next UI fragment depending on what they've selected
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        navigationCategories();
        int id = item.getItemId();

        Fragment fragment = null;
        fragmentClass = null;

        String category = "";
        if (id == R.id.app_bar_search) {
            fragmentClass = SearchFragment.class;
        } else if (id == R.id.nav_Home) {
            fragmentClass = CategoryListFragment.class;
        } else if (id == R.id.nav_favourite) {
            fragmentClass = FavouriteListFragment.class;
        } else if (id == R.id.nav_fingerspelling) {
            category = listCategory.get(0);
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_numbers) {
            category = listCategory.get(1);
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_colours) {
            category = listCategory.get(2);
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_greetings) {
            category = listCategory.get(3);
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_cities) {
            category = listCategory.get(4);
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_family_members) {
            category = listCategory.get(5);
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_interest) {
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_food) {
            fragmentClass = SignListFragment.class;
        } else if (id == R.id.nav_quiz) {
            fragmentClass = QuizMenuFragment.class;
        } else if (id == R.id.nav_progress) {
            fragmentClass = ProgressFragment.class;
        } else if (id == R.id.nav_bsl_info) {
            fragmentClass = ResourcesFragment.class;
        }  else if (id == R.id.FAQ){
            fragmentClass = FAQFragment.class;
        }else if (id == R.id.nav_share) {

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Bundle bundle = new Bundle();
        bundle.putString("Category", category);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * AssetManager used to retrieve the text file - each line of the text file is read then the
     * string object is send to the model class (SignData) for the line to be tokenised suitable
     * and then afterward inserted into the database
     */
    public void readFileAddData() {
        AssetManager assetManager = getAssets();
        mDBHandler = new DBHandler(this);

        InputStream input; // To load text file
        Scanner in; //To read through text file

        try {
            input = assetManager.open("SignList.txt");
            in = new Scanner(input);

            while (in.hasNextLine()) {

                String word = in.nextLine();
                mSignData = new SignData(word);

                mDBHandler.addSign(new SignData(mSignData.getCategoryName(), mSignData.getSignName(),
                        mSignData.getbSLOrder(), mSignData.getSignSynonym(), mSignData.getSignOccurs(),
                        mSignData.getSignShape(), mSignData.getSignConfig(), mSignData.getSignExpress(),
                        mSignData.getVideo_file_path(), mSignData.getFavourite()));
            }
            in.close(); //close scanner and file.
        } catch (IOException e) {
            Log.e(TAG, "Exception Error " + e);
            e.printStackTrace();
        }
    }

    public List navigationCategories() {

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
        return listCategory;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        //mDBHandler.close();
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
