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
 * This is the second activity of the project. This activity and it corresponding fragments displays
 * the different learning resources for a single sign which has been selected by the end user.
 *
 * Which consists of three main fragments:
 *          - SignInformationFragment
 *          - VideoViewFragment
 *          - BSLNotationSystem
 *
 * The primary navigation design pattern implemented is an tab layout.
 * Which is created by TabbedPageAdapter
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
        sign_selected_id = Integer.valueOf(intent.getStringExtra("sign")); //obtain sign id
        sign_name = intent.getStringExtra("name"); //obtain sign name
        Log.d(TAG, "Sign Selected: " + sign_selected_id);

        mTabbedPageAdapter = new TabbedPageAdapter(getSupportFragmentManager());
        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        setUpViewPager(viewPager);

        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(sign_name); //set title of container

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

    /**
     * back pressed Image button action listener.
     * Upon request the user while be sent to the previous screen they were on.
     */
    private void backButtonActionListner(){
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * Share Image button action listener.
     *
     * Subject of share is the title of the app with short slogan.
     *
     * The main text of the share content is the name of sign that the user is sharing followed
     * by the video link which the receiver can watch online.
     */
    private void shareButtonActionListener() {

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignMaterialAdapter = new SignMaterialAdapter(); //init model controller class
                Context context = SignMaterialActivity.this;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BSLearn App: A BSL Learning Tool"); //Subject header
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm learning British Sign Language.\nSign: " + sign_name
                        + "\nVideo Link: " +  mSignMaterialAdapter.getVideoURL(context, sign_selected_id)); //main body
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
        mTabbedPageAdapter.addFragment(new SignInformationFragment(sign_selected_id), "Sign Info"); //Tab 1 (left)
        mTabbedPageAdapter.addFragment(new VideoViewFragment(sign_selected_id), "Video"); //Tab 2 (middle)
        mTabbedPageAdapter.addFragment(new BSLNotationFragment(sign_selected_id), "Notation"); //Tab 3 (right)
        viewPager.setAdapter(mTabbedPageAdapter); //create tabs
    }

    /**
     *  Another navigation design pattern implemented is the bottom navigation view.
     *
     *  intent method is called with the transfer from signmaterialactivity to mainactivity begins.
     *
     *  The title of navigation option is place in an the putExtra() intent method to store the fragment name
     *  the user wishes to navigate to.
     *
     *  Afterwards, the transaction back to the mainactivity is started with it argument.
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
     * Change sign data favourite. update favourite status in model/controller class.
     *
     * @param state the state
     */
    private void changeSignDataFavourite(boolean state) {
        mSignMaterialAdapter = new SignMaterialAdapter(); //init model controller class
        int favourite = 0;
        if (state) {
            favourite = 1;
            mSignMaterialAdapter.changeFavouriteState(this, sign_selected_id, favourite);
        } else {
            mSignMaterialAdapter.changeFavouriteState(this, sign_selected_id, favourite);
        }
    }


    /**
     *
     */
    private void checkstate() {
        mCheckBox = (CheckBox) findViewById(R.id.favourite);
        mSignMaterialAdapter = new SignMaterialAdapter();
        mCheckBox.setChecked(mSignMaterialAdapter.checkStateofFavourite(this,sign_selected_id));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_material, menu);
        return true;
    }

    /**
     * Action listeners for the checkbox (favourite) button. If check box state changes the process to change the icon
     * is begin and the database it updated.
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
