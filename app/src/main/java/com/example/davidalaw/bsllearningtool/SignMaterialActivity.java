package com.example.davidalaw.bsllearningtool;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.davidalaw.bsllearningtool.mAdapters.SectionPageAdapter;
import com.example.davidalaw.bsllearningtool.mFragments.BSLNotationFragment;
import com.example.davidalaw.bsllearningtool.mFragments.FavouriteListFragment;
import com.example.davidalaw.bsllearningtool.mFragments.SignInformationFragment;
import com.example.davidalaw.bsllearningtool.mFragments.VideoViewFragment;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.SignData;

import org.w3c.dom.Text;

public class SignMaterialActivity extends AppCompatActivity {

    private static final String TAG = "FragmentsMainActivity";
    private SectionPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private DBHandler mDBHandler;;
    private static String signSelected;

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

        mSectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);

        mTextView = (TextView) findViewById(R.id.toolbar_title);
        mTextView.setText(signSelected);


        setUpViewPager(mViewPager);

        mCheckBox = (CheckBox) findViewById(R.id.favourite);
        mCheckBox.setOnCheckedChangeListener(new myCheckBoxChangeClicker());
        checkstate();


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPageAdapter adapter = new SectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new VideoViewFragment(signSelected), "Video");
        adapter.addFragment(new SignInformationFragment(signSelected), "Sign Info");
        adapter.addFragment(new BSLNotationFragment(signSelected), "Notation");
        viewPager.setAdapter(adapter);

    }


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
