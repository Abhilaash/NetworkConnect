package com.Velamati.Abhilaash.networkconnect;

import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.SearchView;

import com.Velamati.Abhilaash.common.logger.Log;
import com.Velamati.Abhilaash.common.logger.LogFragment;
import com.Velamati.Abhilaash.common.logger.LogWrapper;
import com.Velamati.Abhilaash.common.logger.MessageOnlyLogFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 * <p/>
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    private JSONArray json = null;
    private ExpandableListView listview = null;
//    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize expandable list view to display notams.
        listview = (ExpandableListView) findViewById(R.id.listview);

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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
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
            } catch (ParseException e) {
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

    private void display() throws ParseException, JSONException {
        ArrayList<String> headers = new ArrayList<String>();
        HashMap<String, Notam> notam = new HashMap<String, Notam>();
        for (int x = 0; x < json.length(); x++) {
                JSONObject j = json.getJSONObject(x);
                headers.add(j.getString("eventid") + ":" + j.getString("notamnumber") + ":" + j.getString("notamtext"));
                notam.put(headers.get(x), new Notam(j));
        }
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, headers, notam);
        listview.setAdapter(listAdapter);
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
        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        LogFragment mLogFragment = (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
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


//        HashMap<String, ArrayList<String>> hm = new HashMap<String, ArrayList<String>>();
//        HashMap<String, String> bm = new HashMap<String, String>();
//Iterator<String> y = json.getJSONObject(x).keys();
//while(y.hasNext()) {
//        String str = y.next();
//        JSONObject j = json.getJSONObject(x);
//        if(!j.getString(str).equals("")
//        && !str.equalsIgnoreCase("eventid")
//        && !str.equalsIgnoreCase("notamnumber")
//        && !str.equalsIgnoreCase("notamtext")
//        && !str.equalsIgnoreCase("affectedfeature"))
//        if(!str.contains("image"))
//        values.add(str + ": " + j.getString(str));
//        else{
//        bm.put(j.getString("eventid"), j.getString("image"));
//        }
//        }