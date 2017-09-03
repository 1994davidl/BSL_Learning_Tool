package com.example.davidalaw.bsllearningtool.mData;

/**
 * Created by DavidALaw on 04/08/2017.
 */
public class FAQuestions {

    //Initialise instance variables
    private String mFAQ;

    /**
     * Instantiates a new Fa questions.
     */
//Default constructor
    public FAQuestions() {
    }

    /**
     * Instantiates a new Fa questions.
     *
     * @param FAQ the fa qquestions
     */
//Non default constructor
    public FAQuestions(String FAQ) {
        mFAQ = FAQ;
    }

    /**
     * Gets fAQs
     *
     * @return the faq
     */
    public String getFAQ() {
        return mFAQ;
    }

    /**
     * Sets fa qquestions.
     *
     * @param FAQ the fa qquestions
     */
    public void setFAQ(String FAQ) {
        mFAQ = FAQ;
    }
}
