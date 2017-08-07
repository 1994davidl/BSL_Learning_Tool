package com.example.davidalaw.bsllearningtool;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.mAdapters.TabbedPageAdapter;
import com.example.davidalaw.bsllearningtool.mFragments.BSLNotationFragment;
import com.example.davidalaw.bsllearningtool.mFragments.FavouriteListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.SignInformationFragment;
import com.example.davidalaw.bsllearningtool.mFragments.VideoViewFragment;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

public class SignMaterialActivity extends AppCompatActivity {

    private static final String TAG = "FragmentsMainActivity";
    private TabbedPageAdapter mTabbedPageAdapter;
    private ViewPager mViewPager;
    private DBHandler mDBHandler;;
    private static String signSelected, fragmentSelected;

    private TabLayout mToptabLayout;
    private BottomNavigationView mBottomNavigationView;

    private Class fragmentClass = null;
    private TextView mTextView;
    private CheckBox mCheckBox;

    private FavouriteListFragment mFavouriteListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_material);

        Intent intent = getIntent();
        signSelected = intent.getStringExtra("sign");
        Log.d(TAG, "Sign Selected pass: " + signSelected);

        mTabbedPageAdapter = new TabbedPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mTextView = (TextView) findViewById(R.id.toolbar_title);
        mTextView.setText(signSelected);

        setUpViewPager(mViewPager);

        mCheckBox = (CheckBox) findViewById(R.id.favourite);
        mCheckBox.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        checkstate();

        mToptabLayout = (TabLayout) findViewById(R.id.tabs);
        mToptabLayout.setupWithViewPager(mViewPager);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        setBottomNavigationView();

    }

    private void setUpViewPager(ViewPager viewPager) {
            mTabbedPageAdapter = new TabbedPageAdapter(getSupportFragmentManager());
            mTabbedPageAdapter.addFragment(new SignInformationFragment(signSelected), "Sign Info");
            mTabbedPageAdapter.addFragment(new VideoViewFragment(signSelected), "Video");
            mTabbedPageAdapter.addFragment(new BSLNotationFragment(signSelected), "Notation");
            viewPager.setAdapter(mTabbedPageAdapter);

    }

    public void setBottomNavigationView(){

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent = new Intent(SignMaterialActivity.this, MainActivity.class);

                Fragment fragment = null;
                switch(item.getItemId()) {
                    case R.id.home_nav:
                        fragmentSelected = (String) item.getTitle();
                        intent.putExtra("fragment", fragmentSelected);
                        Log.d(TAG,"Fragment Selected: " + fragmentSelected);
                        break;
                    case R.id.favourites_nav:
                        checkstate();
                        fragmentSelected = (String) item.getTitle();
                        intent.putExtra("fragment", fragmentSelected);
                        Log.d(TAG,"Fragment Selected: " + fragmentSelected);
                        break;
                    case R.id.about_us_nav:
                        fragmentSelected = (String) item.getTitle();
                        intent.putExtra("fragment", fragmentSelected);
                        Log.d(TAG,"Fragment Selected: " + fragmentSelected);
                        break;
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
                return false;
            }
        });

    }

    /**
     * Action listeners for the checkbox (favourite) button
     */
    class myCheckBoxChangeClicker implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean state) {

            if(compoundButton.isChecked() == true) {
                Log.d(TAG, "Sign Favourited: " + signSelected + " State: " + state);
                changeSignDataFavourite(state);
            } else {
                Log.d(TAG, "Sign favourite removed: " + signSelected + " State: " + state);
                changeSignDataFavourite(state);
            }
        }
    }

    public void changeSignDataFavourite(boolean state) {
        mDBHandler = new DBHandler(this);
        Cursor cursor = mDBHandler.getAllData();

        int favourite = 0;
        if(state == true) {
            favourite = 1;
        }

        while(cursor.moveToNext()) {
            if(signSelected.equals(cursor.getString(2))) {
               mDBHandler.updateSignFavourite(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                       cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                       cursor.getString(6), cursor.getString(7), cursor.getString(8),cursor.getString(9),favourite);
            }
        }
    }

    public void checkstate() {
        mCheckBox = (CheckBox) findViewById(R.id.favourite);
        mDBHandler = new DBHandler(this);
        Cursor cursor = mDBHandler.getAllData();
        while(cursor.moveToNext()) {
            if (signSelected.equals(cursor.getString(2)) && Integer.parseInt(cursor.getString(10)) == 1) {
                mCheckBox.setChecked(true);
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_material, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
