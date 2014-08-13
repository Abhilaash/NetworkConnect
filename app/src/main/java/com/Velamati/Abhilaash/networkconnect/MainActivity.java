package com.Velamati.Abhilaash.networkconnect;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.Velamati.Abhilaash.common.logger.Log;
import com.Velamati.Abhilaash.common.logger.LogFragment;
import com.Velamati.Abhilaash.common.logger.LogWrapper;
import com.Velamati.Abhilaash.common.logger.MessageOnlyLogFilter;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 * <p/>
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private ArrayList<String> values;
    private ArrayList<String> headers;
    private LogFragment mLogFragment;
    private JSONArray json = null;
    private HashMap<String, ArrayList<String>> hm;
    //    private TextView textview = null;
    private ExpandableListView listview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize list view to put the textview into.
        listview = (ExpandableListView) findViewById(R.id.listview);

        // Initialize text fragment that displays intro text.
//        textview = (TextView) findViewById(R.id.textview);

        // Initialize the logging framework.
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.options_menu, menu);
        new DownloadTask().execute("http://www.antarice.com/concepts/vnotam/document.json");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * Implementation of AsyncTask, to fetch the data in the background away from
     * the UI thread.
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadFromNetwork(urls[0]);
            } catch (IOException e) {
                return "Connection Error";
            }
        }

        /**
         * Uses the logging framework to display the output of the fetch
         * operation in the log fragment.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                json = new JSONArray(result);
                display();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Initiates the fetch operation.
     */
    private String loadFromNetwork(String urlString) throws IOException {
        InputStream stream = null;
        String str = "";

        try {
            stream = downloadUrl(urlString);
            str = readIt(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    private void display()
    {
        headers = new ArrayList<String>();
        hm = new HashMap<String, ArrayList<String>>();
        for (int x = 0; x < json.length(); x++) {
            try {
                headers.add(json.getJSONObject(x).getString("notamnumber") + "\n" + json.getJSONObject(x).getString("notamtext"));
                values = new ArrayList<String>();
                Iterator<String> y = json.getJSONObject(x).keys();
                while(y.hasNext()) {
                    String str = y.next();
                    if(!headers.contains(json.getJSONObject(x).getString(str)))
                        values.add(json.getJSONObject(x).getString(str));
                }
                hm.put(headers.get(x), values);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, headers, hm);
        listview.setAdapter(listAdapter);
        listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                 Toast.makeText(getApplicationContext(), "Group Clicked " + headers.get(groupPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        listview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        headers.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        listview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        headers.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), headers.get(groupPosition) + " : "+ hm.get(headers.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    /**
     * Given a string representation of a URL, sets up a connection and gets
     * an input stream.
     *
     * @param urlString A string representation of a URL.
     * @return An InputStream retrieved from a successful HttpURLConnection.
     * @throws java.io.IOException
     */
    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        return conn.getInputStream();
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream InputStream containing HTML from targeted site.
     * @return String concatenated according to len parameter.
     * @throws java.io.IOException
     * @throws java.io.UnsupportedEncodingException
     */
    private String readIt(InputStream stream) throws IOException {
        String a = "";
        InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
        if (reader.ready()) {
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            while (line != null) {
                a += line + "\n";
                line = br.readLine();
            }
            br.close();
        }
        return a;
    }

    /**
     * Create a chain of targets that will receive log data
     */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);
//////ISSUE#1
//        JSONObject j;
//        for (int x = 0; x < json.length(); x++) {
//            try {
//                j = json.getJSONObject(x);
//                textview.append(j.getString("notamnumber") + "\n" + j.getString("notamtext") + "\n");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}

//         for(int x = 0; x < json.length(); x++)
//         {
//             JSONObject obj = null;
//             try {
//                 obj = json.getJSONObject(x);
//                 if (obj != null) {
//                     textview.append(obj.getString("notamtext") + "\n" + (obj.getString("notamnumber")) + "\n");
//                 }
//             } catch (JSONException e) {
//                 e.printStackTrace();
//             }
//         }