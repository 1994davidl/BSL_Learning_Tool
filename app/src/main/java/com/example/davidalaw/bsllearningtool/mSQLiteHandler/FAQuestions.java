package com.example.davidalaw.bsllearningtool.mSQLiteHandler;

/**
 * Created by DavidALaw on 04/08/2017.
 */

public class FAQuestions {

    //Initialise instance variables
    private String mFAQquestions;

    //Default constructor
    public FAQuestions() {

    }

    //Non default constructor
    public FAQuestions(String FAQquestions) {
        mFAQquestions = FAQquestions;
    }

    public String getFAQquestions() {
        return mFAQquestions;
    }

    public void setFAQquestions(String FAQquestions) {
        mFAQquestions = FAQquestions;
    }
}
