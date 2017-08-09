package com.example.davidalaw.bsllearningtool.mModel_Controller;

import android.content.Context;
import android.database.Cursor;

import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import java.util.ArrayList;

/**
 * controllor/model class to retreive data from database and pass to view classes
 *
 * Created by DavidALaw on 07/08/2017.
 */

public class MainPageAdapter {

    private DBHandler mDBHandler;
    private ArrayList<String> mCategoryList, mSignsList, mFavouriteList;

    public MainPageAdapter() {

    }

    /**
     *
     * @param mContext
     * @return mCategoryList
     */
    public ArrayList collectDistinctCategories(Context mContext) {
        mCategoryList = new ArrayList();
        mDBHandler = new DBHandler(mContext);
        Cursor cursor = mDBHandler.getAllData();
        mCategoryList.add("All Signs");

        //get the value from the database from column 1 (Category name)
        //if Arraylist already contains the category then do not add to display
        while(cursor.moveToNext()) {
            if(!mCategoryList.contains(cursor.getString(1))) {
                mCategoryList.add(cursor.getString(1));
            }
        }
        return mCategoryList;
    }

    /**
     * Helper method to return the category selected
     *
     * @param index
     * @return
     */
    public String getSelectedCategory(int index) {
        return mCategoryList.get(index);
    }

    /**
     *
     * @param mContext
     * @param categorySelected
     * @return
     */
    public ArrayList collectSignsFromSelectedCategory(Context mContext, String categorySelected) {
        mSignsList = new ArrayList();
        mDBHandler = new DBHandler(mContext);
        Cursor cursor = mDBHandler.getAllData();

        while(cursor.moveToNext()) {
            //get the value from the database from column 2 (Sign Name)
            //then add it to the array list
            if(categorySelected == "All Signs") {
                mSignsList.add(cursor.getString(2));
                
            }
            if(categorySelected.equals(cursor.getString(1))) {
                if(!mSignsList.contains(cursor.getString(2))) {
                    mSignsList.add(cursor.getString(2));
                }
            }
        }
        return mSignsList;
    }

    /**
     *
     * @param index
     * @return
     */
    public String getSignSelected(int index) {
        return mSignsList.get(index);
    }

    /**
     *
     * @param mContext
     * @return
     */
    public ArrayList collectAllFavouriteSigns(Context mContext) {
        mFavouriteList = new ArrayList<>();
        mDBHandler = new DBHandler(mContext);
        Cursor cursor = mDBHandler.getAllData();

        //get the value from the database from column 1 (Category name)
        //if Arraylist already contains the category then do not add to display
        while(cursor.moveToNext()) {
            if(Integer.parseInt(cursor.getString(10)) > 0) {
               mFavouriteList.add(cursor.getString(2));
            }
        }
        return mFavouriteList;
    }

    public String getFavouriteSign(int index) {
        return mFavouriteList.get(index);
    }

}
