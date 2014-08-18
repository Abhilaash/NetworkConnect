package com.Velamati.Abhilaash.networkconnect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.Velamati.Abhilaash.common.logger.Log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ExpandableListView exp;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, Notam> listDataChild;
    private HashMap<String, String> bmstrings;
    private HashMap<String, Bitmap> bitmapHashMap = new HashMap<String, Bitmap>();

    public ExpandableListAdapter(Context _context, List<String> _listDataHeader, HashMap<String, Notam>_listChildData, ExpandableListView exp) {
        this.context = _context;
        this.listDataHeader = _listDataHeader;
        this.listDataChild = _listChildData;
        bmstrings = new HashMap<String, String>();
        for(Notam notam : listDataChild.values())
        {
            if(notam.getUrl() != null)
                bmstrings.put(notam.getOnlyEventid(), notam.getUrl());
        }
        this.exp = exp;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... eventid) {
            if(!bitmapHashMap.containsKey(eventid[0])) {
                Bitmap bm = null;
                if (bmstrings.containsKey(eventid[0])) {
                    String urldisplay = bmstrings.get(eventid[0]);
                    try {
                        InputStream in = new java.net.URL(urldisplay).openStream();
                        bm = BitmapFactory.decodeStream(in);
                    } catch (Exception e) {
                        Log.e("Error", e.getMessage());
                        e.printStackTrace();
                    }
                }
                bitmapHashMap.put(eventid[0], bm);
                return bm;
            }
            return bitmapHashMap.get(eventid[0]);
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition));
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = getChild(groupPosition, childPosition).toString();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.ListItem);
        Typeface fontreg = Typeface.createFromAsset(context.getAssets(), "fonts/roboto-1.2/Roboto_v1.2/Roboto/Roboto-Regular.ttf");
        // Create a new spannable with the two strings
        txtListChild.setTypeface(fontreg);
        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        TextView ListHeader = (TextView) convertView.findViewById(R.id.ListHeader);
        String[] parts = headerTitle.split(":");
        String eventid = parts[0];
        String htnum = parts[1];
        String httext = parts[2];
        Typeface fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/roboto-1.2/Roboto_v1.2/Roboto/Roboto-Bold.ttf");
        Typeface fontreg = Typeface.createFromAsset(context.getAssets(), "fonts/roboto-1.2/Roboto_v1.2/Roboto/Roboto-Regular.ttf");
        // Create a new spannable with the two strings
        Spannable spannable = new SpannableString(htnum + "\n" + httext);
// Set the custom typeface to span over a section of the spannable object
        spannable.setSpan( new CustomTypefaceSpan("roboto", fontbold), 0, htnum.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new CustomTypefaceSpan("roboto", fontreg), htnum.length(), htnum.length() + httext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ListHeader.setText(spannable);
        new DownloadImageTask((ImageView) convertView.findViewById(R.id.imageView)).execute(eventid);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}