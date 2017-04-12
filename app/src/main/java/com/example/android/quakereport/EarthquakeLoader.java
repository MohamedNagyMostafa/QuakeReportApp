package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mohamed nagy on 9/2/2016.
 */
public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthquakeItem>> {

    private String url;

    @Override
    protected void onStartLoading() {
        // Launch Background Thread
        forceLoad();
        Log.v("onStartLoading","is called");
    }

    public EarthquakeLoader(Context context, String url) {
        super(context);
        this.url = url;
        Log.v("Loader constructor","is called");
    }

    @Override
    public ArrayList<EarthquakeItem> loadInBackground() {

        try {
            // RequestHttp method return JSON API String
            // extractEarthquakes set data to items
            // return list of items
            ArrayList<EarthquakeItem> earthquakeItems =
                    QueryUtils.extractEarthquakes(QueryUtils.RequestHttp(url));
            Log.v("loadInBackGround","is called");

            return earthquakeItems;
        }catch (IOException e) {
            Log.i("Request State ", "invalid");
        }
        return null;
    }

}
