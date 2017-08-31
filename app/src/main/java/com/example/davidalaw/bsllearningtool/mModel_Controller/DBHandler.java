package com.example.davidalaw.bsllearningtool.mModel_Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.davidalaw.bsllearningtool.mData.QuestionBank;
import com.example.davidalaw.bsllearningtool.mData.Regions;
import com.example.davidalaw.bsllearningtool.mData.SignData;


/**
 * The type Db handler.
 *
 * @author DavidALaw
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "DBHandler";

    //Database information
    private static final int DATABASE_VERSION = 1;  // Database Version
    private static final String DATABASE_NAME = "BSL_Learning_Tool";  // Database Name
    private static final String TABLE_SIGN = "SIGNS"; // Sign material table name
    private static final String TABLE_QUIZ = "QUESTIONS_BANK";
    private static final String TABLE_PROGRESS = "PROGRESS";
    private static final String TABLE_REGION = "REGION";

    //Region tables columns
    private static final String KEY_REGION_ID = "id";
    private static final String KEY_REGION = "region";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    //Sign Tables Columns
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY_NAME = "category_name";
    private static final String KEY_NAME = "sign_name";
    private static final String KEY_BSL_ORDER = "bsl_order";
    private static final String KEY_SIGN_SYNONYM = "sign_synonym";
    private static final String KEY_SIGN_OCCUR = "sign_occur";
    private static final String KEY_SIGN_SHAPE = "sign_shape";
    private static final String KEY_SIGN_CONFIG = "sign_config";
    private static final String KEY_SIGN_EXPRESS = "sign_express";
    private static final String KEY_VIDEO_PATH = "video_path";
    private static final String KEY_FAVOURITES = "favourites";
    private static final String KEY_REGION_FK ="region_id";

    //Quiz Tables Columns
    private static final String KEY_QUIZ_ID = "id";
    private static final String KEY_SIGN_FK = "sign_id";
    private static final String KEY_CHOICE_A = "choice_A";
    private static final String KEY_CHOICE_B = "choice_B";
    private static final String KEY_CHOICE_C = "choice_C";
    private static final String KEY_CHOICE_D = "choice_D";
    private static final String KEY_CORRECT_ANSWER = "correct_answer";

    //Progress tables columns
    private static final String KEY_PROGRESS_ID = "id";
    private static final String KEY_CATEGORY_QUIZ = "category_name";
    private static final String KEY_SCORE = "score";
    private static final String KEY_NUM_WRONG = "number_wrong";
    private static final String KEY_NUM_ROUNDS = "number_rounds";

    /**
     * Instantiates a new Db handler.
     *
     * @param context the context
     */
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * These is where we need to write create table statements.
     * This is called when database is created.
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_REGION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_REGION +
                "(" + KEY_REGION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_REGION + " TEXT, "
                + KEY_LATITUDE + " REAL, "
                + KEY_LONGITUDE + " REAL " + ")";
        db.execSQL(CREATE_REGION_TABLE);

        String CREATE_SIGN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SIGN +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY_NAME + " TEXT,"
                + KEY_NAME + " TEXT, "
                + KEY_BSL_ORDER + " TEXT,"
                + KEY_SIGN_SYNONYM + " TEXT,"
                + KEY_SIGN_OCCUR + " TEXT,"
                + KEY_SIGN_SHAPE + " TEXT,"
                + KEY_SIGN_CONFIG + " TEXT, "
                + KEY_SIGN_EXPRESS + " TEXT, "
                + KEY_VIDEO_PATH + " TEXT,"
                + KEY_FAVOURITES + " INTEGER, "
                + KEY_REGION_FK + " INTEGER, FOREIGN KEY(" + KEY_REGION_FK + ") REFERENCES " + TABLE_REGION+"("+KEY_REGION_ID+"))";
        db.execSQL(CREATE_SIGN_TABLE);

        String CREATE_QUIZ_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_QUIZ +
                "(" + KEY_QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CHOICE_A + " TEXT,"
                + KEY_CHOICE_B + " TEXT,"
                + KEY_CHOICE_C + " TEXT,"
                + KEY_CHOICE_D + " TEXT, "
                + KEY_CORRECT_ANSWER + " TEXT, "
                + KEY_SIGN_FK + " INTEGER, FOREIGN KEY(" + KEY_SIGN_FK + ") REFERENCES "+ TABLE_SIGN+"("+KEY_ID+"))";
        db.execSQL(CREATE_QUIZ_TABLE);

        String CREATE_PROGRESS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PROGRESS +
                "(" + KEY_PROGRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY_QUIZ + " TEXT, "
                + KEY_SCORE + " INTEGER, "
                + KEY_NUM_WRONG + " INTEGER, "
                + KEY_NUM_ROUNDS + " INTEGER " + ")";
        db.execSQL(CREATE_PROGRESS_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_REGION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGN); // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PROGRESS);
        onCreate(db); // Creating tables again
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_REGION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGN);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_QUIZ);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PROGRESS);
        onCreate(db);
    }

    /**
     * Add regions.
     *
     * @param region the region
     */
    public void addRegions(Regions region) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_REGION, region.getRegion_name());
        values.put(KEY_LONGITUDE, region.getLongitude());
        values.put(KEY_LATITUDE, region.getLatitude());
        Log.d(TAG, "Add a region: Adding" + region + " To db table " + TABLE_REGION);

        db.insert(TABLE_REGION, null, values);
        db.close();
    }

    /**
     * Gets all regions.
     *
     * @return the all regions
     */
    public Cursor getAllRegions() {
        String query = "SELECT * FROM " + TABLE_REGION;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


    /**
     * Gets region signs.
     *
     * @return the region signs
     */
    public Cursor getRegionSigns() {
        String query = "SELECT s."+KEY_ID +", s."+ KEY_NAME + ", r." + KEY_REGION  +" FROM " + TABLE_SIGN + " s JOIN "
                + TABLE_REGION +" r ON s."+KEY_REGION_FK + " = r." + KEY_REGION_ID;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    /**
     * This method is used to populate the Sign Table in the db
     *
     * @param sign the sign
     */
    public void addSign(SignData sign) {
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
        values.put(KEY_REGION_FK, sign.getRegion_fk());

        Log.d(TAG, "Add new Sign: Adding " + sign + " TO db TABLE: " + TABLE_SIGN);

        db.insert(TABLE_SIGN, null, values); //Insert rows
        db.close();
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
     * In the event that the user favourites a sign,
     * then that sign row in the db table must be updated.
     *
     * @param id              the id
     * @param categoryName    the category name
     * @param signName        the sign name
     * @param bSLOrder        the bsl sign order
     * @param signSynonym     the sign synonym
     * @param signOccurs      the sign occurs
     * @param signShape       the sign shape
     * @param signConfig      the sign config
     * @param signExpress     the sign express
     * @param video_file_path the video file path
     * @param favourite       the favourite
     * @param region          the region
     * @return int int
     * @link
     */
    public void updateSignFavourite(int id, String categoryName, String signName, String bSLOrder, String signSynonym,
                                   String signOccurs, String signShape, String signConfig, String signExpress,
                                   String video_file_path, int favourite, int region) {
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
        values.put(KEY_REGION_FK, region);

        // updating row
       db.update(TABLE_SIGN, values, KEY_ID + "=" + id, null);
    }

    /**
     * Add progress.
     *
     * @param category   the category
     * @param score      the score
     * @param wrong      the number of wrong answers
     * @param num_rounds the number rounds
     */
    public void AddProgress(String category, int score, int wrong, int num_rounds) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CATEGORY_QUIZ,category);
        values.put(KEY_SCORE, score);
        values.put(KEY_NUM_WRONG, wrong);
        values.put(KEY_NUM_ROUNDS, num_rounds);

        db.insert(TABLE_PROGRESS, null, values);
        db.close();
    }

    /**
     * Gets all progress.
     *
     * @return the all progress
     */
    public Cursor getAllProgress() {
        String query = "SELECT * FROM " + TABLE_PROGRESS;
        Cursor cursor = getReadableDatabase().rawQuery(query,null);
        return cursor;
    }

    /**
     * Check the table is populated i.e the user has taken a quiz.
     * Return true if the progress table is populated.
     *
     * @return boolean value
     */
    public boolean checkProgressTablePopulated(){

        String query = "SELECT COUNT(*) FROM " + TABLE_PROGRESS;
        Cursor cursor = getReadableDatabase().rawQuery(query,null);

        cursor.moveToFirst();
        int counter = cursor.getInt(0);
        //more than one row then display results.
        cursor.close();
        return counter > 0;
    }

    /**
     * Populate the question bank DB table
     *
     * @param questionBank the question bank
     */
    public void addQuestion(QuestionBank questionBank) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHOICE_A, questionBank.getChoiceA());
        values.put(KEY_CHOICE_B, questionBank.getChoiceB());
        values.put(KEY_CHOICE_C, questionBank.getChoiceC());
        values.put(KEY_CHOICE_D, questionBank.getChoiceD());
        values.put(KEY_CORRECT_ANSWER, questionBank.getCorrectAnswer());
        values.put(KEY_SIGN_FK,questionBank.getSign_fk());
        Log.d(TAG, "Add questions to Question Bank " + questionBank + " TO db TABLE: " + TABLE_QUIZ);

        db.insert(TABLE_QUIZ, null, values);
        db.close();
    }

    /**
     * Gets distinct sum values.
     *
     * @return cursor distinct sum values
     */
    public Cursor getDistinctSumValues() {

        String query = " SELECT DISTINCT " + KEY_CATEGORY_QUIZ + " , SUM("+ KEY_SCORE +") FROM "
                + TABLE_PROGRESS + " GROUP BY " + KEY_CATEGORY_QUIZ;
        Cursor cursor = getReadableDatabase().rawQuery(query,null);
        return cursor;
    }

    public Cursor joinQuestionAndSignsTable(){
        String query = "SELECT * FROM "+ TABLE_SIGN +" s JOIN "+ TABLE_QUIZ +" q ON s." + KEY_ID + " = q." + KEY_SIGN_FK;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }
}
