package com.example.davidalaw.bsllearningtool.mModel_Controller;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

/**
 * Controller Class of Sign Material Activity view
 *
 *
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

    public void changeFavouriteState(Context context, int sign_selected_id, int favourite) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();
        while (cursor.moveToNext()) {
            if (sign_selected_id == Integer.valueOf(cursor.getString(0))) {
                mDBHandler.updateSignFavourite(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                        cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9),
                        favourite, Integer.parseInt(cursor.getString(11)));
            }
        }
    }

    public boolean checkStateofFavourite(Context context, int sign_selected_id) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();
        Boolean favourite = false;
        while (cursor.moveToNext()) {
            if (sign_selected_id == Integer.valueOf(cursor.getString(0)) && Integer.parseInt(cursor.getString(10)) == 1) {
                favourite = true;
            }
        }
        return favourite;
    }

    //////////////////// SignInformationFragment ////////////////////////////

    /**
     * Populate sign info fragment array list.
     *
     * @param context      the context
     * @param signid the sign selected
     * @return the array list
     */
    public void populateSignInfoFrag (Context context, int signid) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();
        int start_attribute = 2; //attribute value 2 ---> sign name
        int end_attribute = 4; //attribute value 4 ---> sign synonym

        while (cursor.moveToNext()) {
            if (signid == Integer.valueOf(cursor.getString(0))) {
                for (int i = start_attribute; i <= end_attribute; i++) {
                    mSignInfoList.add(i - start_attribute, cursor.getString(i));
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
        int start_attribute = 5; //attribute 5 of sign table ---> sign occur
        int end_attribute = 8; //attribute 8 of sign table --> sign expression

        while (cursor.moveToNext()) {
            if (signSelected == Integer.valueOf(cursor.getString(0))) {

                for(int i = start_attribute; i <= end_attribute;i++) {
                    mBSLNotationList.add(i-start_attribute, cursor.getString(i));
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
     * @param signid    the sign
     * @return the string
     */
    public String getVideoURL(Context context, int signid){
        String videoURL = "";
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while (cursor.moveToNext()) {
            //return string of Video_http attribute if passed sign_id matches sign_id attribute
            if (signid == Integer.valueOf(cursor.getString(0))) {
                videoURL = cursor.getString(9);
            }
        }
        return videoURL;
    }

    /**
     * Return string of the BSL sign order with has the sign id
     *
     * @param context
     * @param signid
     * @return
     */
    public String getBSLOrderName (Context context, int signid) {
        String name = ""; //empty string

        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData(); //call helper method

        while (cursor.moveToNext()) {
            //If sign id matches sign_id attribute then return string with the value of bsl_order attribute
            if (signid == Integer.valueOf(cursor.getString(0))) {
                name = cursor.getString(3);
            }
        }
        return name;
    }

}
