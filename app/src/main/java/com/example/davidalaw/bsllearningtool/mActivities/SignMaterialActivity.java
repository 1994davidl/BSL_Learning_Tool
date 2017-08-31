package com.example.davidalaw.bsllearningtool.mActivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.R;
import com.example.davidalaw.bsllearningtool.mFragments.BSLNotationFragment;
import com.example.davidalaw.bsllearningtool.mFragments.SignInformationFragment;
import com.example.davidalaw.bsllearningtool.mFragments.VideoViewFragment;
import com.example.davidalaw.bsllearningtool.mModel_Controller.SignMaterialAdapter;
import com.example.davidalaw.bsllearningtool.mModel_Controller.TabbedPageAdapter;
import com.example.davidalaw.bsllearningtool.mModel_Controller.DBHandler;


/**
 * The type Sign material activity.
 */
public class SignMaterialActivity extends AppCompatActivity {

    private static final String TAG = "FragmentsMainActivity";
    private static String fragmentSelected, sign_name;
    private TabbedPageAdapter mTabbedPageAdapter;
    private SignMaterialAdapter mSignMaterialAdapter;
    private int sign_selected_id;

    private DBHandler mDBHandler;
    private BottomNavigationView mBottomNavigationView;

    private CheckBox mCheckBox;
    private ImageButton mShareButton, mBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_material);

        Intent intent = getIntent();
        sign_selected_id = Integer.valueOf(intent.getStringExtra("sign"));
        sign_name = intent.getStringExtra("name");
        Log.d(TAG, "Sign Selected: " + sign_selected_id);

        mTabbedPageAdapter = new TabbedPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(viewPager);

        //set title of container

        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(sign_name);

        //Favourite button
        mCheckBox = (CheckBox) findViewById(R.id.favourite);
        mCheckBox.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        checkstate();

        //Back button initialised and action listener called.
        mBackButton = (ImageButton) findViewById(R.id.back);
        backButtonActionListner();

        //share button initialised and action listener called.
        mShareButton = (ImageButton) findViewById(R.id.share);
        shareButtonActionListener();

        TabLayout toptabLayout = (TabLayout) findViewById(R.id.tabs);
        toptabLayout.setupWithViewPager(viewPager);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        setBottomNavigationView();

    }

    private void backButtonActionListner(){

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * Image button action listener.
     */
    private void shareButtonActionListener() {

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignMaterialAdapter = new SignMaterialAdapter();
                Context context = SignMaterialActivity.this;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BSLearn App: A BSL Learning Tool");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm learning British Sign Language.\nSign: " + sign_name
                        + "\nVideo Link: " +  mSignMaterialAdapter.getVideoURL(context, sign_selected_id));
                startActivity(Intent.createChooser(shareIntent, "Share via: "));
            }
        });
    }


    /**
     * Sets up view pager.
     *
     * @param viewPager the view pager
     */
    private void setUpViewPager(ViewPager viewPager) {
        mTabbedPageAdapter = new TabbedPageAdapter(getSupportFragmentManager());
        mTabbedPageAdapter.addFragment(new SignInformationFragment(sign_selected_id), "Sign Info");
        mTabbedPageAdapter.addFragment(new VideoViewFragment(sign_selected_id), "Video");
        mTabbedPageAdapter.addFragment(new BSLNotationFragment(sign_selected_id), "Notation");
        viewPager.setAdapter(mTabbedPageAdapter);
    }

    /**
     * Sets bottom navigation view.
     */
    private void setBottomNavigationView() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent = new Intent(SignMaterialActivity.this, MainActivity.class);

                switch (item.getItemId()) {
                    case R.id.home_nav:
                        fragmentSelected = (String) item.getTitle();
                        intent.putExtra("fragment", fragmentSelected);
                        Log.d(TAG, "Fragment Selected: " + fragmentSelected);
                        break;
                    case R.id.favourites_nav:
                        checkstate();
                        fragmentSelected = (String) item.getTitle();
                        intent.putExtra("fragment", fragmentSelected);
                        Log.d(TAG, "Fragment Selected: " + fragmentSelected);
                        break;
                    case R.id.quiz_menu_nav:
                        fragmentSelected = (String) item.getTitle();
                        intent.putExtra("fragment", fragmentSelected);
                        Log.d(TAG, "Fragment Selected: " + fragmentSelected);
                        break;
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return false;
            }
        });

    }

    /**
     * Change sign data favourite.
     *
     * @param state the state
     */
    private void changeSignDataFavourite(boolean state) {
        mDBHandler = new DBHandler(this);
        Cursor cursor = mDBHandler.getAllData();

        int favourite = 0;
        if (state) {
            favourite = 1;
        }

        while (cursor.moveToNext()) {
            if (sign_selected_id == Integer.valueOf(cursor.getString(0))) {
                mDBHandler.updateSignFavourite(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9),
                        favourite, Integer.parseInt(cursor.getString(11)));
            }
        }
    }


    /**
     * Checkstate.
     */
    private void checkstate() {
        mCheckBox = (CheckBox) findViewById(R.id.favourite);
        mDBHandler = new DBHandler(this);
        Cursor cursor = mDBHandler.getAllData();
        while (cursor.moveToNext()) {

            if (sign_selected_id == Integer.valueOf(cursor.getString(0)) && Integer.parseInt(cursor.getString(10)) == 1) {
                mCheckBox.setChecked(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_material, menu);
        return true;
    }

    /**
     * Action listeners for the checkbox (favourite) button
     */
    private class myCheckBoxChangeClicker implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean state) {

            if (compoundButton.isChecked() == true) {
                Log.d(TAG, "Sign Favourited: " + sign_selected_id + " State: " + state);
                changeSignDataFavourite(state);
            } else {
                Log.d(TAG, "Sign favourite removed: " + sign_selected_id + " State: " + state);
                changeSignDataFavourite(state);
            }
        }
    }


}
