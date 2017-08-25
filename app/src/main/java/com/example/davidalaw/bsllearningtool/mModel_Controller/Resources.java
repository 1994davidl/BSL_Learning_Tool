package com.example.davidalaw.bsllearningtool.mModel_Controller;

/**
 * Created by DavidALaw on 01/08/2017.
 */

public class Resources {

    //Initialise instance variables
    private String mResources;

    //Default constructor
    public Resources() {}

    //Non default constructor
    public Resources(String resources) {
        mResources = resources;
    }

    public String getResources() {
        return mResources;
    }

    public void setResources(String resources) {
        mResources = resources;
    }
}
