package com.example.android.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link EarthquakeItem} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<EarthquakeItem> extractEarthquakes(String sampleJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        if(sampleJSON.isEmpty())
            return null;
        ArrayList<EarthquakeItem> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject earthquakeJSON = new JSONObject(sampleJSON);
            JSONArray earthquakeItem = earthquakeJSON.getJSONArray("features");
            for(int i = 0 ; i < earthquakeItem.length() ; i++ ){
                JSONObject properties = earthquakeItem.getJSONObject(i)
                        .getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String url = properties.getString("url");
                String location = properties.getString("place");
                Date date = new Date();
                date.setTime(properties.getLong("time"));

                earthquakes.add(new EarthquakeItem(magnitude,location,date,url));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        Log.v("List is created","done");
        return earthquakes;
    }

    // urlString of API
    // return JSON of API String
    public static String RequestHttp(String urlString)throws IOException{

        String JSONResponse = "";
        URL url = createURL(urlString);

        if( url == null)
            return JSONResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            // HTTP Request
            // Create Object from HttpURLConnection from url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET"); // Request Type

            // send Request
            urlConnection.connect();


            // Check if website is valid
            if(urlConnection.getResponseCode() == 200) {

                // get stream of byte
                inputStream = urlConnection.getInputStream();
                // get JSON Stream
                JSONResponse = getJSONStream(inputStream);

            }
        }catch (IOException e){
            Log.e("Error ",e.toString());
        }finally {
            // disconnect request
            if(urlConnection != null)
                urlConnection.disconnect();
            // close stream
            if(inputStream != null)
                inputStream.close();
        }

        Log.v("Request HTTP","is done ");
        return JSONResponse;
    }

    // check if the url null
    // Create new Object from url then return it
    private static URL createURL(String urlString){
        URL url = null;
        try{
            url = new URL(urlString);
        }catch (MalformedURLException e){
            Log.e("Error ",e.toString());
        }
        Log.v("create URL","is called");
        return url;
    }

    private static String getJSONStream(InputStream inputStreams) throws  IOException{
        // To set stream lines
        StringBuilder output = new StringBuilder();

        if(inputStreams != null){
            // Convert Bytes Stream to characters Stream
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStreams, Charset.forName("UTF-8"));
            // Convert from characters Stream to String Stream
            BufferedReader bufferedReader =
                    new BufferedReader(inputStreamReader);
            // set Lines to StringBuilder
            String line;
            line = bufferedReader.readLine();
            while ( line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        Log.v("Convert Stream"," into JSON String done");
        return output.toString();
    }

}