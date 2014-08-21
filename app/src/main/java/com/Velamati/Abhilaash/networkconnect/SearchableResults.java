package com.Velamati.Abhilaash.networkconnect;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;


public class SearchableResults extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_searchable_results);
        // Get the intent, verify the action and get the query
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
             doMySearch(query);
        }
    }

    public String doMySearch(String query) {
        String Url = MainActivity.myUrl;
        Url = "https://notamdemo.aim.nas.faa.gov/bdedev/dbjsonlink?key=VNOTAM_API&location=" + query;
        MainActivity.myUrl = Url;
        setTitle(Url);
        return Url;
    }
}
