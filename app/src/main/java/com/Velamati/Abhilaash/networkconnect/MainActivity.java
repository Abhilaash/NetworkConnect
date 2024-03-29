package com.Velamati.Abhilaash.networkconnect;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.Velamati.Abhilaash.common.logger.Log;
import com.Velamati.Abhilaash.common.logger.LogFragment;
import com.Velamati.Abhilaash.common.logger.LogWrapper;
import com.Velamati.Abhilaash.common.logger.MessageOnlyLogFilter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends FragmentActivity {

    private JSONArray json = null;
    private JSONArray jsonairport;
    private ExpandableListView listview = null;
    private TextView textView;
    private ListView listView;
    private String myUrlNotam = "https://notamdemo.aim.nas.faa.gov/bdedev/dbjsonlink?key=VNOTAM_API&location=";
    private String myUrlAirport = "https://notamdemo.aim.nas.faa.gov/bdedev/dbjsonlink?key=AIRPORT_LIST&search=";
    private ArrayList<Airport> airports = new ArrayList<Airport>();
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        listView = (ListView) findViewById(R.id.list);
        //Initialize expandable list view to display notams.
        listview = (ExpandableListView) findViewById(R.id.listview);
        handleIntent(getIntent());
        // Initialize the logging framework.
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                onSearchRequested();

                return true;
            default:
                return false;
        }
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
        // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        AssetManager am = this.getAssets();
        InputStream caInput = new BufferedInputStream(am.open("notamdemo.aim.nas.faa.crt"));
        Certificate ca = null;
        try {
            if (cf != null) {
                ca = cf.generateCertificate(caInput);
            }
            if (ca != null) {
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            }
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            caInput.close();
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            if (keyStore != null) {
                keyStore.load(null, null);
            }
            keyStore.setCertificateEntry("ca", ca);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

// Create an SSLContext that uses our TrustManager
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

// Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(urlString);
//        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
//        urlConnection.setSSLSocketFactory(context.getSocketFactory());
//        InputStream in = urlConnection.getInputStream();
//        copyInputStreamToOutputStream(in, System.out);

        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(context.getSocketFactory());
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
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
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, headers, notam, listview);
        listview.setAdapter(listAdapter);
    }

    /**
     * Implementation of AsyncTask, to fetch the data in the background away from
     * the UI thread.
     */
    private class DownloadTask2 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return loadFromNetwork2(urls[0]);
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
                jsonairport = new JSONArray(result);
                try {
                    storedata();
                    showResults();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
    }

    /**
     * Initiates the fetch operation.
     */
    private String loadFromNetwork2(String urlString) throws IOException {
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
    private InputStream downloadUrlAirportlist(String urlString) throws IOException {
        // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        AssetManager am = this.getAssets();
        InputStream caInput = new BufferedInputStream(am.open("notamdemo.aim.nas.faa.crt"));
        Certificate ca = null;
        try {
            if (cf != null) {
                ca = cf.generateCertificate(caInput);
            }
            if (ca != null) {
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            }
        } catch (CertificateException e) {
            e.printStackTrace();
        } finally {
            caInput.close();
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            if (keyStore != null) {
                keyStore.load(null, null);
            }
            keyStore.setCertificateEntry("ca", ca);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = null;
        try {
            tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

// Create an SSLContext that uses our TrustManager
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            context.init(null, tmf.getTrustManagers(), null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

// Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(urlString);
//        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
//        urlConnection.setSSLSocketFactory(context.getSocketFactory());
//        InputStream in = urlConnection.getInputStream();
//        copyInputStreamToOutputStream(in, System.out);

        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.setSSLSocketFactory(context.getSocketFactory());
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
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
    private String readIt2(InputStream stream) throws IOException {
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

    private void storedata() throws ParseException, JSONException {
//        ArrayList<String> headers = new ArrayList<String>();
//        HashMap<String, Airport> airports = new HashMap<String, Airport>();
        for (int x = 0; x < jsonairport.length(); x++) {
            JSONObject j = jsonairport.getJSONObject(x);
//            headers.add(j.getString("arpt_") + ":" + j.getString("Airport ID"));
            airports.add(new Airport(j));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            Intent airportIntent = new Intent(this, SearchableResults.class);
            airportIntent.setData(intent.getData());
            startActivity(airportIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            String myUrlAirporttemp = myUrlAirport + query.toUpperCase();
            this.query = query;
            new DownloadTask2().execute(myUrlAirporttemp);
        }
    }

    private void showResults() {
        // Get the intent, verify the action and get the query
//        Uri uri = getIntent().getData();
//            Uri uri = Uri.parse(myUrlAirport + query);
//        Cursor cursor = managedQuery(uri, null, null, null, null);

        if (airports.size() < 1) {
            // There are no results
            textView.setText("No Results Found");
        } else {
//            for(int x = 0; x < jsonairport.length(); x++)
            // Specify the columns we want to display in the result
//            String[] from = new String[] {new String("arpt_id"), new String("arpt_name")};

            // Specify the corresponding layout elements where we want the columns to go
////            int[] to = new int[] { R.id.id, R.id.name };
//            for(int x = 0; x < data.length; x++){
//                try {
//                    data[x] = airports.get(x).getArptid() + "\n" + jsonairport.getJSONObject(x).getString("arpt_name");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
            // Create a arrayadapter adapter for the airports and apply them to the ListView
            AirportAdapter airportadapter = new AirportAdapter(this, airports);
            listView.setAdapter(airportadapter);

            // Define the on-click listener for the list items
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Build the Intent used to open WordActivity with a specific word Uri
                    Intent searchIntent = new Intent(getApplicationContext(), SearchableResults.class);
                    Uri data = Uri.withAppendedPath(DictionaryProvider.CONTENT_URI, String.valueOf(id));
                    searchIntent.setData(data);
                    startActivity(searchIntent);
                }
            });
        }
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