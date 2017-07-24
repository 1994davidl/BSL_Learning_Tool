package com.example.davidalaw.bsllearningtool.mSQLiteHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidALaw on 18/07/2017.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DBHandler";

    //Database information
    private static final int DATABASE_VERSION = 1;  // Database Version
    private static final String DATABASE_NAME = "BSL_Learning_Tool";  // Database Name
    private static final String TABLE_SIGN = "SIGNS"; // Sign material table name

    //Sign Tables Columns
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY_NAME="category_name";
    private static final String KEY_NAME = "sign_name";
    private static final String KEY_BSL_ORDER = "bsl_order";
    private static final String KEY_SIGN_SYNONYM ="sign_synonym";
    private static final String KEY_SIGN_OCCUR = "sign_occur";
    private static final String KEY_SIGN_SHAPE = "sign_shape";
    private static final String KEY_SIGN_CONFIG = "sign_config";
    private static final String KEY_SIGN_EXPRESS = "sign_express";

    private static final String KEY_VIDEO_PATH = "video_path";
    private static final String KEY_FAVOURITES="favourites";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * These is where we need to write create table statements.
     * This is called when database is created.
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SIGN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SIGN + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY_NAME + " TEXT," + KEY_NAME + " TEXT, " + KEY_BSL_ORDER + " TEXT," + KEY_SIGN_SYNONYM + " TEXT,"
                +  KEY_SIGN_OCCUR + " TEXT," + KEY_SIGN_SHAPE + " TEXT," + KEY_SIGN_CONFIG + " TEXT, " + KEY_SIGN_EXPRESS + " TEXT, " + KEY_VIDEO_PATH + " TEXT," + KEY_FAVOURITES + " INT" + ")";
        db.execSQL(CREATE_SIGN_TABLE);
    }

    /**
     * This method is called when database is upgraded like modifying the table structure,
     * adding constraints to database etc
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGN);
        // Creating tables again
        onCreate(db);
    }

    /**
     * This method is used to populate the Sign Table in the db
     *
     * @param sign
     */
    public void addSign (SignData sign) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CATEGORY_NAME, sign.getCategoryName()); //Category name
        values.put(KEY_NAME, sign.getSignName()); //Sign Name
        values.put(KEY_BSL_ORDER, sign.getbSLOrder()); //BSL order
        values.put(KEY_SIGN_SYNONYM, sign.getSignSynonym()); //Sign also means
        values.put(KEY_SIGN_OCCUR, sign.getSignOccurs());//sign occurs
        values.put(KEY_SIGN_SHAPE, sign.getSignShape());
        values.put(KEY_SIGN_CONFIG, sign.getSignConfig());
        values.put(KEY_SIGN_EXPRESS, sign.getSignExpress());
        values.put(KEY_VIDEO_PATH, sign.getVideo_file_path()); //video file path
        values.put(KEY_FAVOURITES, sign.getFavourite()); //favourite

        Log.d(TAG, "Add new Sign: Adding " + sign + " TO db TABLE: " + TABLE_SIGN);

        db.insert(TABLE_SIGN, null, values); //Insert rows
        db.close();
    }

    /**
     * Method will be used to collect a particular sign by it ID number.
     *
     * @param id
     * @return sign
     */
    public SignData getSign(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SIGN, new String[]{KEY_ID,  KEY_CATEGORY_NAME, KEY_NAME, KEY_BSL_ORDER,
                        KEY_SIGN_SYNONYM,KEY_SIGN_OCCUR, KEY_SIGN_SHAPE, KEY_SIGN_CONFIG, KEY_SIGN_EXPRESS, KEY_VIDEO_PATH,KEY_FAVOURITES}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        SignData sign = new SignData(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),
                cursor.getString(6), cursor.getString(7), cursor.getString(8),cursor.getString(9), Integer.parseInt(cursor.getString(10)));

        return sign;
    }

    /**
     * Getting All signs
     * This method inserts all the data in the Sign Table into a Array List
     *
     * @return signlist
     */
    public List<SignData> getAllSigns() {
        List<SignData> signList = new ArrayList<SignData>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SIGN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                SignData sign = new SignData();
                sign.setId(Integer.parseInt(cursor.getString(0)));
                sign.setCategoryName(cursor.getString(1));
                sign.setSignName(cursor.getString(2));
                sign.setbSLOrder(cursor.getString(3));
                sign.setSignSynonym(cursor.getString(4));
                sign.setSignOccurs(cursor.getString(5));
                sign.setSignShape(cursor.getString(6));
                sign.setSignConfig(cursor.getString(7));
                sign.setSignExpress(cursor.getString(8));
                sign.setVideo_file_path(cursor.getString(9));
                sign.setFavourite(Integer.parseInt(cursor.getString(10)));

                // Adding material to list
                signList.add(sign);
            } while (cursor.moveToNext());
        }

        // return contact list
        return signList;
    }


    /**
     * This method returns all the Sign Names which will be displayed in the ListView
     * of each category.
     *
     * @return cursor data
     */
    public Cursor getAllData() {

        String query = "SELECT * FROM " + TABLE_SIGN;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }



    /**
     * Method returns the total number of signs available to learn
     *
     * @return cursor.getCount()
     */
    public int getSignCount() {

        String countQuery = "SELECT * FROM " + TABLE_SIGN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    /**
     * Method is used to update an existing sign already populated in the table.
     *
     * @param sign
     * @return
     */
    public int updateSign(SignData sign) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, sign.getCategoryName());
        values.put(KEY_NAME, sign.getSignName());
        values.put(KEY_BSL_ORDER, sign.getbSLOrder());
        values.put(KEY_SIGN_SYNONYM, sign.getSignSynonym());
        values.put(KEY_SIGN_OCCUR, sign.getSignOccurs());
        values.put(KEY_SIGN_SHAPE, sign.getSignShape());
        values.put(KEY_SIGN_CONFIG, sign.getSignConfig());
        values.put(KEY_SIGN_CONFIG, sign.getSignExpress());
        values.put(KEY_VIDEO_PATH, sign.getVideo_file_path());
        values.put(KEY_FAVOURITES, sign.getFavourite());

        // updating row
        return db.update(TABLE_SIGN, values, KEY_ID + " = ?",
                new String[]{String.valueOf(sign.getId())});
    }

    public int updateSignFavourite(int id, String categoryName, String signName, String bSLOrder, String signSynonym,
                                   String signOccurs, String signShape, String signConfig, String signExpress,
                                   String video_file_path, int favourite ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY_NAME, categoryName);
        values.put(KEY_NAME, signName);
        values.put(KEY_BSL_ORDER, bSLOrder);
        values.put(KEY_SIGN_SYNONYM, signSynonym);
        values.put(KEY_SIGN_OCCUR, signOccurs);
        values.put(KEY_SIGN_SHAPE, signShape);
        values.put(KEY_SIGN_CONFIG, signConfig);
        values.put(KEY_SIGN_EXPRESS, signExpress);
        values.put(KEY_VIDEO_PATH, video_file_path);
        values.put(KEY_FAVOURITES, favourite);

        // updating row
        return db.update(TABLE_SIGN, values, KEY_ID + "=" + id, null);
    }


    public void deleteSign(SignData sign) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SIGN, KEY_ID + " = ?",
                new String[] { String.valueOf(sign.getId()) });
        db.close();
    }

    public ArrayList<String> getAllSignNames() {
        ArrayList<String> allNames = new ArrayList<String>();

        //Generate a select query
        String names = "SELECT DISTINCT " + KEY_NAME + " FROM " + TABLE_SIGN;

        //Execute query
        Cursor cursor = getReadableDatabase().rawQuery(names, null);

        //Do we have a result
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            //Iterate over the results as we have results
            while(!cursor.isAfterLast()) {
                //Add name to selection
                allNames.add(cursor.getString(0));

                cursor.moveToNext();
            }
        }
        //Close the cursor
        cursor.close();

    //Return allNames values
    return allNames;
    }
}
