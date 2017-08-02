package com.example.davidalaw.bsllearningtool.mAdapters;


public class ResourcesAdapter {

    Resources resources;

    private Resources [ ] mResources;
    private final int SIZE = 5;

    public ResourcesAdapter() {
        mResources = new Resources[SIZE];
    }

    public void populateResourcesObjArray(String line) {

        resources = new Resources(line);

        for(int i = 0; i < mResources.length; i++) {
            if(mResources[i] == null) {
                mResources[i] = resources;
            }
        }
    }

    public String getInformation(int i) {
        return mResources[i].getResources();
    }

}
