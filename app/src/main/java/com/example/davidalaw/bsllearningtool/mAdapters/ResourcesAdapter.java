package com.example.davidalaw.bsllearningtool.mAdapters;


public class ResourcesAdapter {

    Resources resources;
    FAQuestions faQuestions;

    private Resources [ ] mResources;
    private FAQuestions [ ] mFAQuestionses;
    private final int SIZE = 5;

    public ResourcesAdapter() {
        mResources = new Resources[SIZE];
        mFAQuestionses = new FAQuestions [SIZE];
    }

    public void populateResourcesObjArray(String line) {

        resources = new Resources(line);

        for(int i = 0; i < mResources.length; i++) {
            if(mResources[i] == null) {
                mResources[i] = resources;
            }
        }
    }

    public void populateFAQuestionObjArray(String line ) {

        faQuestions = new FAQuestions(line);

        for(int i = 0; i < mResources.length; i++) {
            if(mFAQuestionses[i] == null) {
                mFAQuestionses[i] = faQuestions;
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
