/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<EarthquakeItem>>{

    public static final String LOG_TAG =
            EarthquakeActivity.class.getName();
    private EarthquakeAdapter adapter;
    private static final String URL_SITE =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&limit=10";
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private ProgressBar progressBar;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private TextView emptyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        /** Project View **/
        // Create Adapter object
         adapter = new EarthquakeAdapter(EarthquakeActivity.this,
                 new ArrayList<EarthquakeItem>());

        // Set Adapter object to List
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        earthquakeListView.setAdapter(adapter);

        /** Project System **/
        // Set Listener to ListView Item
        // this code executes when ListView item is clicked
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthquakeItem ob = adapter.getItem(i);
                openSite(ob.getUrl());
            }
        });

        // Set Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Set Empty View for project
        // be visible when the ListView is invisible
        emptyText = (TextView) findViewById(R.id.text_view_empty);
        earthquakeListView.setEmptyView(emptyText);

        // Check Network Connection
        connectivityManager = (ConnectivityManager)
                getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected() ) {

            Log.v("backGround Thread", "is called");
            // start Background Thread
            // Request Networking
            // get API JSON
            // Set data to list
            LoaderManager loaderManager = getLoaderManager();
            // EARTHQUAKE_LOADER_ID thread's ID
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        }
        else{
            emptyText.setText("No Network Connection");
            progressBar.setVisibility(View.GONE);
        }
        Log.v("Main thread","is finished");
    }

    // @Param URL String of item website
    private void openSite(String url){
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY,url);
        startActivity(intent);
        Log.v("set Url for items ","Intent is done");
    }

    // implementation of Loader interface methods
    // Called if no loader Object existed
    @Override
    public Loader<ArrayList<EarthquakeItem>> onCreateLoader(int i, Bundle bundle) {
        Log.v("onCreateLoader","is called");

        // Called loader Constructor
        // Create New Loader Object
        return new EarthquakeLoader(EarthquakeActivity.this,URL_SITE);
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<EarthquakeItem>> loader, ArrayList<EarthquakeItem> earthquakeItems) {
        // clear to update adapter list
        adapter.clear();
        // check if list is null
        if(earthquakeItems != null && !(earthquakeItems.isEmpty())) {
            Log.v("onLoadFinished","is called");
            progressBar.setVisibility(View.GONE);
            // set List to adapter
            adapter.addAll(earthquakeItems);

        }
        else {
            progressBar.setVisibility(View.GONE);
            emptyText.setText("No Earthquake is found");
        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthquakeItem>> loader) {
        // when loader is removed
        Log.v("onLoaderReset","is called");
        adapter.clear();
    }
}
