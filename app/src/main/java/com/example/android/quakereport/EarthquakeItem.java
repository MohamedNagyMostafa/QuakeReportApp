package com.example.android.quakereport;

/**
 * Created by mohamed nagy on 8/29/2016.
 */

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EarthquakeItem {

    private Date date;
    private String location;
    private double magnitude;
    private SimpleDateFormat format;
    private String url;

    public EarthquakeItem(double magnitude,String location,Date date,String url){
        this.date = date;
        this.location = location;
        this.magnitude = magnitude;
        this.url = url;
        Log.v("new Item list","is created");
    }

    public String getDate(){
         format = new SimpleDateFormat("MMM dd, yyyy");
        return format.format(date);
    }

    public String getTime(){
        format = new SimpleDateFormat("h:mm a");
        return format.format(date);
    }

    public String getUrl(){ return url;}
    public String getLocation(){    return location;}

    public double getMagnitude() { return magnitude;}
}
