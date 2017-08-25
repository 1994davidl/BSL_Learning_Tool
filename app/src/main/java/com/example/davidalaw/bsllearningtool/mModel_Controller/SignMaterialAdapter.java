package com.example.davidalaw.bsllearningtool.mModel_Controller;

import android.content.Context;
import android.database.Cursor;

import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import java.util.ArrayList;

/**
 * Created by DavidALaw on 07/08/2017.
 */
public class SignMaterialAdapter {

    private final ArrayList<String> mSignInfoList;
    private final ArrayList<String> mBSLNotationList;
    private DBHandler mDBHandler;


    /**
     * Instantiates a new Sign material adapter.
     */
    public SignMaterialAdapter() {
        mSignInfoList = new ArrayList<>();
        mBSLNotationList = new ArrayList<>();
    }

    /////////////////// SignMaterialActivity /////////////////////////





    //////////////////// SignInfoFragment ////////////////////////////

    /**
     * Populate sign info fragment array list.
     *
     * @param context      the context
     * @param signSelected the sign selected
     * @return the array list
     */
    public void populateSignInfoFrag (Context context, int signSelected) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (signSelected == Integer.valueOf(cursor.getString(0))) {
                for (int i = 2; i <= 4; i++) {
                    mSignInfoList.add(i - 2, cursor.getString(i));
                }
            }
        }
    }

    /**
     * Gets sign info.
     *
     * @param index the index
     * @return the sign info
     */
    public String getSignInfo(int index) {
        return mSignInfoList.get(index);
    }

    ///////////////////// BSLNotationFragment ////////////////////////////////

    /**
     * Populate bsl stokoe notation system array list.
     *
     * @param context      the context
     * @param signSelected the sign selected
     * @return the array list
     */
    public void populateBSLNotation(Context context, int signSelected) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (signSelected == Integer.valueOf(cursor.getString(0))) {
                for(int i = 5; i <= 8;i++) {
                    mBSLNotationList.add(i-5, cursor.getString(i));
                }
            }
        }
    }

    /**
     * Gets bsl notation.
     *
     * @param index the index
     * @return the bsl notation
     */
    public String getBSLNotation(int index) {
        return mBSLNotationList.get(index);
    }

    //////////////////////////// VideoViewFragment ///////////////////////////////

    /**
     * Get video url string.
     *
     * @param context the context
     * @param sign    the sign
     * @return the string
     */
    public String getVideoURL(Context context, int sign){
        String videoURL = "";
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (sign == Integer.valueOf(cursor.getString(0))) {
                videoURL = cursor.getString(9);
            }
        }
        return videoURL;
    }

    public String getBSLOrderName (Context context, int signid) {
        String name = "";
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (signid == Integer.valueOf(cursor.getString(0))) {
                name = cursor.getString(3);
            }
        }
        return name;
    }

}
