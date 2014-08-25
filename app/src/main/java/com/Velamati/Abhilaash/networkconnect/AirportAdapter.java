package com.Velamati.Abhilaash.networkconnect;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Abhilaash on 8/25/2014.
 */
public class AirportAdapter extends BaseAdapter {
    Context context;
    ArrayList<Airport> airports;

    public AirportAdapter(Context context, ArrayList<Airport> airportArrayList) {
        this.context = context;
        airports = airportArrayList;
    }

    @Override
    public int getCount() {
        return airports.size();
    }

    @Override
    public Object getItem(int position) {
        return airports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.activity_searchable_results, null);
        }
        String text = airports.get(position).toString();
        TextView textView = (TextView) convertView.findViewById(R.id.text);
        String[] parts = text.split(":");
        String arptid = parts[0];
        String arptname = parts[1];
        Typeface fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/roboto-1.2/Roboto_v1.2/Roboto/Roboto-Bold.ttf");
        Typeface fontreg = Typeface.createFromAsset(context.getAssets(), "fonts/roboto-1.2/Roboto_v1.2/Roboto/Roboto-Regular.ttf");
        // Create a new spannable with the two strings
        Spannable spannable = new SpannableString(arptid + "\n" + arptname);
// Set the custom typeface to span over a section of the spannable object
        spannable.setSpan( new CustomTypefaceSpan("roboto", fontbold), 0, arptid.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("roboto", fontreg), arptid.length(), arptid.length() + arptname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannable);
        return convertView;
    }
}
