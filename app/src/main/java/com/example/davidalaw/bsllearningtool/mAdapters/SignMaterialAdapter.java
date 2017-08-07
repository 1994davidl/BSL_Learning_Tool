package com.example.davidalaw.bsllearningtool.mAdapters;

import android.content.Context;
import android.database.Cursor;

import com.example.davidalaw.bsllearningtool.mFragments.SignInformationFragment;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.DBHandler;

import java.util.ArrayList;

/**
 * Created by DavidALaw on 07/08/2017.
 */

public class SignMaterialAdapter {

    private ArrayList<String> mSignInfoList, mBSLNotationList;

    private DBHandler mDBHandler;


    public SignMaterialAdapter() {
    }

    public ArrayList populateSignInfoFrag (Context context, String signSelected) {
        mSignInfoList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (signSelected.equals(cursor.getString(2))) {
                for (int i = 2; i <= 4; i++) {
                    mSignInfoList.add(i - 2, cursor.getString(i));
                }
            }
        }
        return mSignInfoList;
    }

    public String getSignInfo(int index) {
        return mSignInfoList.get(index);
    }

    public ArrayList populateBSLNotation(Context context, String signSelected) {
        mBSLNotationList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (signSelected.equals(cursor.getString(2))) {
                for(int i = 5; i <= 8;i++) {
                    mBSLNotationList.add(i-5, cursor.getString(i));
                }
            }
        }
        return mBSLNotationList;
    }

    public String getBSLNotation(int index) {
        return mBSLNotationList.get(index);
    }


    public String getVideoURL(Context context, String sign){
        String videoURL = "http://content.jwplatform.com/videos/PSYPYEXC-6B5j5ITm.mp4";
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            if (sign.equals(cursor.getString(2))) {
                if (!cursor.getString(9).equals("http://youtube.com")) {
                    videoURL = cursor.getString(9);
                }
            }
        }
        return videoURL.toString();
    }

    public String getBSLSignOrder(Context context, String sign) {
        String bslOrder = "";
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();
        while (cursor.moveToNext()) {
            if (sign.equals(cursor.getString(2))) {
                bslOrder = cursor.getString(3);
            }
        }
        return bslOrder.toString();
    }

}
