package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by mohamed nagy on 8/29/2016.
 */
public class EarthquakeAdapter extends ArrayAdapter<EarthquakeItem> {

    private final String OF = " of ";
    private String locationOffSet;
    private String primaryLocation;
    private final DecimalFormat DECIMAL_FORMAT
            = new DecimalFormat("0.0");

    public EarthquakeAdapter(Context context, ArrayList<EarthquakeItem> arr) {
        super(context, 0, arr);
        Log.v("Adapter Constructor","is called");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemRecycle = convertView;
        EarthquakeItem ob = getItem(position);

        if(itemRecycle == null)
            itemRecycle = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item,parent,false);

        TextView textMag = (TextView)
                itemRecycle.findViewById(R.id.earthquake_mag_text);
        TextView textDate = (TextView)
                itemRecycle.findViewById(R.id.earthquake_date_text);
        TextView textPlace = (TextView)
                itemRecycle.findViewById(R.id.earthquake_place_text);
        TextView textKmPlace = (TextView)
                itemRecycle.findViewById(R.id.earthquake_kmPlace_text);
        TextView textTime = (TextView)
                itemRecycle.findViewById(R.id.earthquake_time_text);
        // split String Location
        String location = ob.getLocation();

        if(location.contains(OF)){
            locationOffSet = location.substring(0,location.indexOf(OF) + 3);
            primaryLocation = location.substring(location.indexOf(OF) + 4);
        }
        else{
            locationOffSet = getContext().getString(R.string.near_of);
            primaryLocation = location;
        }

        textMag.setText(DECIMAL_FORMAT.format(ob.getMagnitude()));
        textDate.setText(ob.getDate());
        textTime.setText(ob.getTime());
        textKmPlace.setText(locationOffSet);
        textPlace.setText(primaryLocation);

        GradientDrawable shape = (GradientDrawable) textMag.getBackground();
        int magnitudeColor = getMagnitudeColor(ob.getMagnitude());

        shape.setColor(magnitudeColor);
        /** Listener **/
        Log.v("Recycle ","done");
        return itemRecycle;
    }

    public int getMagnitudeColor(double mag){
        int color;
        switch ((int)Math.floor(mag)){
            case 0 :
            case 1 :
                color= R.color.oneMag;
                break;

            case 2 :
                color= R.color.twoMag;
                break;

            case 3 :
                color= R.color.threeMag;
                break;

            case 4 :
                color= R.color.fourMag;
                break;

            case 5 :
                color= R.color.fiveMag;
                break;

            case 6 :
                color= R.color.sixMag;
                break;

            case 7 :
                color= R.color.sevenMag;
                break;

            case 8 :
                color= R.color.eightMag;
                break;

            case 9 :
                color= R.color.nineMag;
                break;

            default:
                color= R.color.tenMagPlus;

        }
        return ContextCompat.getColor(getContext(),color);
    }

}
