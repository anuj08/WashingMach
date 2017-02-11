package com.example.android.washingmachtest;

/**
 * Created by anuj on 10/1/17.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by anuj on 2/1/17.
 */

public class VMLoader extends AsyncTaskLoader<ArrayList<WashingMachine>> {

    private final String LOG_TAG = VMLoader.class.getName();

    private String mUrl = null;

    VMLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }


    @Override
    public ArrayList<WashingMachine> loadInBackground() {
        if (mUrl == null)
            return null;

        //Log.v(LOG_TAG, "loadInBackground called");
        ArrayList<WashingMachine> washingMachines = QueryUtils.extractEarthquakes(mUrl);

        return washingMachines;
    }

    @Override
    protected void onStartLoading() {
        //Log.v(LOG_TAG, "forceLoad called");
        forceLoad();
    }
}
