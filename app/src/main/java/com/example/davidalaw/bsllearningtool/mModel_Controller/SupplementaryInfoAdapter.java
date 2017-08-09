package com.example.davidalaw.bsllearningtool.mModel_Controller;


import com.example.davidalaw.bsllearningtool.mSQLiteHandler.FAQuestions;
import com.example.davidalaw.bsllearningtool.mSQLiteHandler.Resources;

public class SupplementaryInfoAdapter {

    private Resources resources;

    private FAQuestions faQuestions;

    private Resources [ ] mResources; 

    private FAQuestions [ ] mFAQuestionses;

    private final int SIZE = 6;

    public SupplementaryInfoAdapter() {
        mResources = new Resources[SIZE];
        mFAQuestionses = new FAQuestions [SIZE];
    }

    /**
     *
     * @param line
     */
    public void populateResourcesObjArray(String line) {

        resources = new Resources(line);

        for(int i = 0; i < mResources.length; i++) {
            if(mResources[i] == null) {
                mResources[i] = resources;
            }
        }
    }

    /**
     *
     * @param line
     */
    public void populateFAQuestionObjArray(String line ) {

        faQuestions = new FAQuestions(line);

        for(int i = 0; i < mResources.length; i++) {
            if(mFAQuestionses[i] == null) {
                mFAQuestionses[i] = faQuestions;
                return;
            }
        }
    }

    public String getFAQuestions(int i) {
        return mFAQuestionses[i].getFAQquestions();
    }

    public String getInformation(int i) {
        return mResources[i].getResources();
    }

}
