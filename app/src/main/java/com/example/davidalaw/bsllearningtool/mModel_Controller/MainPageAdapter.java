package com.example.davidalaw.bsllearningtool.mModel_Controller;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.davidalaw.bsllearningtool.mData.FAQuestions;
import com.example.davidalaw.bsllearningtool.mData.Resources;

import java.util.ArrayList;
import java.util.Collections;

/**
 * controllor/model class to retreive data from database and pass to view classes
 * <p>
 * Created by DavidALaw on 07/08/2017.
 */
public class MainPageAdapter {

    private static final String TAG = MainPageAdapter.class.getSimpleName();

    private DBHandler mDBHandler;

    private final Resources[ ] mResources; //object array
    private final FAQuestions [ ] mFAQuestionses; //object array

    private ArrayList<String> mCategoryList;
    private ArrayList<String> mSignsIDList;
    private ArrayList<String> mSignsList;
    private ArrayList<String> mFavouriteList;
    private ArrayList<String> mRegionList;
    private ArrayList<String> mLongitudeList;
    private ArrayList<String> mLatitudeList;
    private ArrayList<String> RegionSignsList;
    private ArrayList<String> mCategorySelectedQuestionsList;
    private ArrayList<String> mRadioButtonsList;
    private ArrayList<String> mProgressList;

    private String mQuizCategorySelected;



    /**
     * Instantiates a new Main page adapter.
     */
    public MainPageAdapter() {
        int SIZE = 6;
        mResources = new Resources[SIZE];
        mFAQuestionses = new FAQuestions [SIZE];
    }

    /////////////////////// CategoryListFragment //////////////////////////////

    /**
     * Collect distinct categories array list.
     *
     * @param mContext the m context
     * @return mCategoryList array list
     */
    public ArrayList collectDistinctCategories(Context mContext) {

        mCategoryList = new ArrayList();
        mDBHandler = new DBHandler(mContext);
        Cursor cursor = mDBHandler.getAllData();
        //get the value from the database from column 1 (Category name)
        while(cursor.moveToNext()) {
            //Do not display regions as a option in the categories section & is category already exist in
            //array list do not add duplicate.
            if(!mCategoryList.contains(cursor.getString(1)) && !cursor.getString(1).equals("Region")) {
                mCategoryList.add(cursor.getString(1));
            }
        }
        return mCategoryList;
    }

    /**
     * Helper method to return the category selected
     *
     * @param index the index
     * @return selected category
     */
    public String getSelectedCategory(int index) {
        return mCategoryList.get(index);
    }


    ///////////////////////////SearchFragment /////////////////////////////////////

    /**
     * Gets searchable signs.
     *
     * @param context the context
     * @return the searchable signs
     */
    public ArrayList getSearchableSigns(Context context) {
        mSignsList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();

        while(cursor.moveToNext()) {
            if(!mSignsList.contains(cursor.getString(2))) {
                mSignsList.add(cursor.getString(2));
            }
        }
    return mSignsList;
    }


    /**
     * As the search function will not show duplicate signs in a search. Therefore first sign is displayed only and therefore method is return the first time the sign is found.
     *
     * @param context
     * @param sign
     * @return
     */
    public void getSearchableSignsID (Context context, String sign) {
        mSignsIDList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllData();
        cursor.moveToLast();

        while(cursor.moveToPrevious()) {
            if(sign.equals(cursor.getString(2))) {
                mSignsIDList.add(cursor.getString(0));
                break;
            }
        }
    }



    ///////////////////////////// SignListFragment /////////////////////////////

    /**
     * Collect signs from selected category array list.
     *
     * @param mContext         the m context
     * @param categorySelected the category selected
     * @return array list
     */
    public ArrayList collectSignsFromSelectedCategory(Context mContext, String categorySelected) {
        mSignsList = new ArrayList();
        mSignsIDList = new ArrayList<>();
        mDBHandler = new DBHandler(mContext);
        Cursor cursor = mDBHandler.getAllData();

        while(cursor.moveToNext()) {

            //else get the value from the database from column 2 (Sign Name)
            //then add it to the array list

            if(categorySelected == "All Signs") {
                mSignsList.add(cursor.getString(2));

            }

            if(categorySelected.equals(cursor.getString(1))) {
                if(!mSignsList.contains(cursor.getString(2))) {
                    mSignsIDList.add(cursor.getString(0));
                    mSignsList.add(cursor.getString(2));
                }
            }
        }
        return mSignsList;
    }

    /**
     * Gets sign id selected.
     *
     * @param index the index
     * @return the sign id selected
     */
    public String getSignIDSelected(int index) {
        return mSignsIDList.get(index);
    }

    /**
     * Pass in the index of the listview and returns the signs to be passed to the SignMaterialActivity
     *
     * @param index the index
     * @return index of array list
     * @link Called in the SignListFragment on click item listener
     */
    public String getSignSelected(int index) {
        return mSignsList.get(index);
    }

    /////////////////// FavouriteListFragment //////////////////////////

    /**
     * Iterates through database table_sign rows collects those with which column favourite is greater than 0.
     *
     * @param mContext the m context
     * @return array list
     */
    public ArrayList collectAllFavouriteSigns(Context mContext) {
        mFavouriteList = new ArrayList<>();
        mSignsIDList = new ArrayList<>();
        mDBHandler = new DBHandler(mContext);
        Cursor cursor = mDBHandler.getAllData();

        //get the value from the database from column 1 (Category name)
        //if Arraylist already contains the category then do not add to display
        while(cursor.moveToNext()) {
            if(Integer.parseInt(cursor.getString(10)) > 0) {
               mFavouriteList.add(cursor.getString(2));
               mSignsIDList.add(cursor.getString(0));
            }
        }
        return mFavouriteList;
    }

    /**
     * Gets favourite sign.
     *
     * @param index the index
     * @return favourite sign
     */
    public String getFavouriteSign(int index) {
        return mFavouriteList.get(index);
    }


    //////////////////// RegionalMapFragment ///////////////////////

    /**
     * List all longitude array list.
     *
     * @param context the context
     * @return the array list
     */
    public void listAllLongitude(Context context) {
        mLongitudeList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllRegions();

        while(cursor.moveToNext()) {
            mLongitudeList.add(cursor.getString(3));
        }
    }

    /**
     * Gets longitude.
     *
     * @param index the index
     * @return the longitude
     */
    public String getLongitude(int index) {
        return mLongitudeList.get(index);
    }


    /**
     * Gets the number of regions that are in the region db table.
     *
     * @param context the context
     * @return the region count
     */
    public int getRegionCount(Context context) {
        int counter =0;
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllRegions();
        while(cursor.moveToNext()) {
            counter++;
        }
        return counter;
    }

    /**
     * List all lanitude array list.
     *
     * @param context the context
     * @return the array list
     */
    public void listAllLanitude(Context context) {

        mLatitudeList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllRegions();
        while(cursor.moveToNext()) {
            mLatitudeList.add(cursor.getString(2));
        }
    }

    /**
     * Gets latitude list.
     *
     * @param index the index
     * @return the latitude list
     */
    public String getLatitudeList(int index) {
        return mLatitudeList.get(index);
    }


    /**
     * collect List of all regions names.
     *
     * @param context the context
     * @return the array list
     */
    public void listAllRegions(Context context) {
        mRegionList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllRegions();
        while(cursor.moveToNext()) {
            mRegionList.add(cursor.getString(1));
        }
    }

    /**
     * Gets a single region name from the array list.
     *
     * @param index the index
     * @return the region name
     */
    public String getRegionName (int index) {
        return mRegionList.get(index);
    }


    /////////////////// RegionalSignListFragment /////////////////

    /**
     * Get the signs for the selected region. Returns signs within that region in an array list
     *
     * @param context the context
     * @param region  the region
     * @return region signs
     */
    public ArrayList getRegionSigns(Context context, String region) {
        mDBHandler = new DBHandler(context);
        RegionSignsList = new ArrayList<>();
        mSignsIDList = new ArrayList<>();
        Cursor cursor = mDBHandler.getRegionSigns();

        while(cursor.moveToNext()) {
            if(region.equals(cursor.getString(2))) {
                RegionSignsList.add(cursor.getString(1));
                mSignsIDList.add(cursor.getString(0));
            }
        }
        return RegionSignsList;
    }

    /**
     * Gets region single sign.
     *
     * @param index the index
     * @return the region single sign
     */
    public String getRegionSingleSign(int index) {
        return RegionSignsList.get(index);
    }

    //////////////////// QuizMenuFragment ///////////////////////////

    /**
     * Gets the distinct optional categories available to be quizzed on.
     *
     * @param context the context
     * @return the quiz optional categories
     */
    public ArrayList getQuizOptionalCategories(Context context) {
        mDBHandler = new DBHandler(context);
        ArrayList<String> quizCategoriesList = new ArrayList<>();
        Cursor cursor = mDBHandler.joinQuestionAndSignsTable();

        while(cursor.moveToNext()) {
           if(!quizCategoriesList.contains(cursor.getString(1))) {
               quizCategoriesList.add(cursor.getString(1));
           }
        }
        return quizCategoriesList;
    }

    /**
     * Gets total number of available questions .
     *
     * @param context          the context
     * @param categorySelected the category selected
     * @return the category questions counter
     */
    public int getCategoryQuestionsCounter(Context context, String categorySelected) {
        int counter =0;
        mCategorySelectedQuestionsList = new ArrayList<>();
        mQuizCategorySelected = categorySelected;
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.joinQuestionAndSignsTable();

        while(cursor.moveToNext()) {
            if(cursor.getString(1).equals(mQuizCategorySelected)) {
                counter ++;
            }
        }

        //Limit number of questions to max of 10.
        if(counter > 10) {
            counter =10;
        }
        return counter;
    }


    ////////////////////// QuizFragment /////////////////////////////

    /**
     * Gets selected category questions.
     *
     * @param context          the context
     * @param categorySelected the category selected
     */
    public void getCategoryQuestions(Context context, String categorySelected) {
        mCategorySelectedQuestionsList = new ArrayList<>();
        mQuizCategorySelected = categorySelected;
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.joinQuestionAndSignsTable();

        while(cursor.moveToNext()) {
            if(cursor.getString(1).equals(mQuizCategorySelected)) {
                mCategorySelectedQuestionsList.add(cursor.getString(0));
            }
        }
    }

    /**
     * Shuffle questions.
     */
    public void shuffleQuestions() {
        Collections.shuffle(mCategorySelectedQuestionsList);
    }

    /**
     * Move front question to back, move second question to front.
     */
    public void moveFrontQuestionToBack(){
        Collections.rotate(mCategorySelectedQuestionsList, -1);
    }

    /**
     * Gets video url.
     *
     * @param context the context
     * @return the video url
     */
    public String getVideoURL(Context context) {
        String videoURL = "";
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.joinQuestionAndSignsTable();

        while(cursor.moveToNext()) {
          if(mCategorySelectedQuestionsList.get(0).equals(cursor.getString(0))) {
              videoURL = cursor.getString(9);
          }
        }
        return videoURL;
    }

    /**
     * Populate radio buttons queue.
     *
     * @param context the context
     */
    public void populateRadioButtonsQueue (Context context) {
        mRadioButtonsList = new ArrayList<>();
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.joinQuestionAndSignsTable();

        while(cursor.moveToNext()) {
            if(mCategorySelectedQuestionsList.get(0).equals(cursor.getString(0))) {
                int j = 13; // column number of choiceA in db sql query
                for(int i = 0; i < 4; i++) {
                    mRadioButtonsList.add(cursor.getString(j));
                    j++;
                }
            }
        }
    }

    /**
     * Gets radio button child.
     *
     * @param index the index
     * @return the radio button child
     */
    public String getRadioButtonChild(int index) {
        return mRadioButtonsList.get(index);
    }

    /**
     * Gets question answer.
     *
     * @param context the context
     * @return the question answer
     */
    public String getQuestionAnswer (Context context) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.joinQuestionAndSignsTable();
        String answer = "";

        while(cursor.moveToNext()) {
            if(mCategorySelectedQuestionsList.get(0).equals(cursor.getString(0))){
                answer = cursor.getString(17);
            }
        }
        return answer;
    }


    //////////////////// ProgressFragment /////////////////////////
    private int totalScore = 0;
    private int totalRounds =0;
    private int bestRound = 0;
    private int num_of_rounds = 0;
    private int best = 0;

    public void getallProgressInfo(Context context) {
        mProgressList = new ArrayList<>();
        Log.d(TAG, "working ");
        mProgressList.add(getTotalScore(context));
        mProgressList.add(getBestRound(context));
        mProgressList.add(getLastRoundScore(context));
        mProgressList.add(getTopCategory(context));
        mProgressList.add(getCategoryToImproveOn(context));
        mProgressList.add(getTotalNumberofQuizAttempts(context));

    }

    public String getProgressList(int index) {
        return mProgressList.get(index);
    }

    private String getTotalScore(Context context) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllProgress();
        String totalS = "";

        while(cursor.moveToNext()) {
            totalScore += Integer.parseInt(cursor.getString(2));
            totalRounds +=  Integer.parseInt(cursor.getString(4));
            totalS =String.valueOf(totalScore) + "/" + String.valueOf(totalRounds);
        }
        return totalS;
    }
    /**
     * Get the quiz attempt where the user scored the best round.
     *
     * @param context
     */
    private String getBestRound (Context context) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllProgress();
        String bestR = "";

        while (cursor.moveToNext()) {
            if (Integer.parseInt(cursor.getString(2)) > bestRound) {
                bestRound = Integer.parseInt(cursor.getString(2));
                num_of_rounds = Integer.parseInt(cursor.getString(4));
                bestR = String.valueOf(bestRound) + "/" + String.valueOf(num_of_rounds);
            }
        }
        return bestR;
    }

    private String getLastRoundScore(Context context) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllProgress();
        String lastR = "";

        while (cursor.moveToNext()) {
            if (cursor.getCount() > 1) {
                cursor.moveToPosition(cursor.getCount() - 1); //move to last round
                lastR = cursor.getString(2) + "/" + cursor.getString(4);

            } else {
                lastR = cursor.getString(2) + "/" + cursor.getString(4);
            }
        }
        return lastR;
    }



    private String getTopCategory(Context context) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getDistinctSumValues();
        String topCategory = "";

        while (cursor.moveToNext()) {
            if (Integer.parseInt(cursor.getString(1)) > best) {
                best = Integer.parseInt(cursor.getString(1));
                topCategory = cursor.getString(0);
            }
        }
        return topCategory;
    }

    private String getCategoryToImproveOn (Context context) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getDistinctSumValues();
        String worstCategory = "";
        int worst = best;
        while (cursor.moveToNext()) {
            if (Integer.parseInt(cursor.getString(1)) < worst) {
                worst = Integer.parseInt(cursor.getString(1));
                worstCategory = cursor.getString(0);
            }
        }
        return worstCategory;
    }

    private String getTotalNumberofQuizAttempts (Context context) {
        mDBHandler = new DBHandler(context);
        Cursor cursor = mDBHandler.getAllProgress();
        String count= "";
        count = String.valueOf(cursor.getCount());
        return count;
    }


    /////////////////// Resources & FAQ Model ////////////////////

    /**
     * Populate frequently asked question object array.
     *
     * @param line the line
     */
    public void populateFAQuestionObjArray(String line ) {
        FAQuestions faQuestions = new FAQuestions(line);
        for(int i = 0; i < mResources.length; i++) {
            if(mFAQuestionses[i] == null) {
                mFAQuestionses[i] = faQuestions;
                return;
            }
        }
    }

    /**
     * Gets FAQs.
     *
     * @param index the index
     * @return the FAQ
     */
    public String getFAQuestions(int index) {
        return mFAQuestionses[index].getFAQ();
    }

    /**
     * Populate resources object array.
     *
     * @param line the line
     */
    public void populateResourcesObjArray(String line) {
        Resources resources = new Resources(line);
        for(int i = 0; i < mResources.length; i++) {
            if(mResources[i] == null) {
                mResources[i] = resources;
                return;
            }
        }
    }

    /**
     * Gets index of information.
     *
     * @param index the
     * @return the information
     */
    public String getInformation(int index) {
        return mResources[index].getResources();
    }

}
