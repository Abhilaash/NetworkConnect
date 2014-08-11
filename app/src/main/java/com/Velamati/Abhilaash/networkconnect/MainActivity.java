package com.Velamati.Abhilaash.networkconnect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

//import android.util.TypedValue;
//import java.io.Reader;
//import java.io.UnsupportedEncodingException;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 * <p/>
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    public static final String TAG = "Network Connect";

    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private LogFragment mLogFragment;
    private JSONArray json = null;
    private TextView textview = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        // Initialize text fragment that displays intro text.
        textview = (TextView) findViewById(R.id.textview);

        // Initialize the logging framework.
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.fetch_action:
                new DownloadTask().execute("http://www.antarice.com/concepts/vnotam/document.json");
//ISSUE#1
//                  JSONObject j;
//                for(int x = 0; x < json.length(); x++){
//                    try {
//                        j = json.getJSONObject(x);
//                        textview.append(j.getString("notamnumber") + "\n" + j.getString("notamtext") + "\n");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }


                return true;
//            // Clear the log view fragment.
//            case R.id.clear_action:
//                mLogFragment.getLogView().setText("");
//                return true;
//        }
//        return false;
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
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
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
            Log.i(TAG, result);
            try {
                json = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}