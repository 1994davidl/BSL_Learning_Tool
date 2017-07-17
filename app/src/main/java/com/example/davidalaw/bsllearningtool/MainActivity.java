package com.example.davidalaw.bsllearningtool;

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
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchFragment.OnFragmentInteractionListener, CategoryListFragment.OnFragmentInteractionListener,
        TutorialSignsFragment.OnFragmentInteractionListener, TutorialListFragment.OnFragmentInteractionListener, QuizFragment.OnFragmentInteractionListener, ProgressFragment.OnFragmentInteractionListener  {

    private Class fragmentClass = null;
    private static List<String> categories;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
         * A external source provide a solution on Using Fragments with the Navigation Drawer Activity
         * Source: https://github.com/ChrisRisner/AndroidFragmentNavigationDrawer
         */
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void hideItem()
    {
        navigationView = (NavigationView) findViewById(R.id.nav_Home);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_Home).setVisible(false);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        fragmentClass = null;
        if (id == R.id.app_bar_search) {
            fragmentClass = SearchFragment.class;
        } else if (id == R.id.nav_Home) {
            fragmentClass = CategoryListFragment.class;
        } else if (id == R.id.nav_fingerspelling) {
            fragmentClass = TutorialListFragment.class;
        }else if (id == R.id.nav_numbers) {
            fragmentClass = TutorialListFragment.class;
        } else if (id == R.id.nav_colours) {
            fragmentClass = TutorialListFragment.class;
        } else if (id == R.id.nav_greetings) {
            fragmentClass = TutorialListFragment.class;
        } else if (id == R.id.nav_cities) {
            fragmentClass = TutorialListFragment.class;
        } else if (id == R.id.nav_work) {
            fragmentClass = TutorialListFragment.class;
        } else if (id == R.id.nav_interest) {
            fragmentClass = TutorialListFragment.class;
        } else if (id == R.id.nav_food) {
            fragmentClass = TutorialListFragment.class;
        } else if (id == R.id.nav_quiz) {
            fragmentClass = QuizFragment.class;
        } else if (id == R.id.nav_progress) {
            fragmentClass = ProgressFragment.class;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void readAvailableCategoriesTxt () {

        categories = new ArrayList<>();
        AssetManager assetManager = getAssets();

        // To load text file
        InputStream input;

        try {
            input = assetManager.open("categories.txt");
            int size = input.available();

            byte[] buffer = new byte[size];
            input.read(buffer);
            input.close();

            // byte buffer into a string
            String text = new String(buffer);
            categories.add(text); //add categories to List


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
