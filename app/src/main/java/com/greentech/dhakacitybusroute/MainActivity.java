package com.greentech.dhakacitybusroute;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SearchView fromPlace;
    SearchView toPlace;
    int from;
    int to;
    Button goBtn;
    SimpleCursorAdapter mFromSuggestionAdapter;
    SimpleCursorAdapter mToSuggestionAdapter;
    SimpleCursorAdapter mSuggestionAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goBtn = (Button) findViewById(R.id.goBtn);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                RouteData.data.open(MainActivity.this);
                Log.d("Databse", "Opened");
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();
                prepareAdapters();
                super.onPostExecute(aVoid);
            }
        }.execute();

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(to != 0 && from != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(RouteData.STARTPLACE, from);
                    bundle.putInt(RouteData.DESTINATION, to);

                    Intent intent = new Intent(MainActivity.this, ViewerActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(MainActivity.this, "No Value is set", Toast.LENGTH_LONG).show();
                }
            }
        });


//    private void startSearch(String s, int id) {
//
//        Bundle searchBundle = new Bundle();
//        searchBundle.putString(SEARCH, s);
//        searchBundle.putInt(ID, id);
//
//        Intent intent = new Intent(getActivity(), ViewerActivity.class);
//        intent.putExtras(searchBundle);
//        startActivity(intent);
//    }

//    private void searchlist(String s) {
//
//        Bundle searchBundle = new Bundle();
//        searchBundle.putString(SEARCH, s);
//        searchBundle.putInt(TYPE, 1);
//
//        Intent intent = new Intent(getActivity(), ListActivity.class);
//        intent.putExtras(searchBundle);
//        startActivity(intent);
//    }


    }

    private void prepareAdapters() {
        Log.d("adapter" , "initiating");
        final Cursor cursor = RouteData.data.getPlacesList("");
        String[] columns = new String[]{RouteData.NAME, RouteData.ID};
        int[] columnTextId = new int[]{android.R.id.text1};

        mFromSuggestionAdapter = new SimpleCursorAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1, cursor,
                columns, columnTextId, 0);

        mFromSuggestionAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return RouteData.data.getPlacesList((String) charSequence);
//                            return cursor;
            }
        });


        mToSuggestionAdapter = new SimpleCursorAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1, cursor,
                columns, columnTextId, 0);
        mToSuggestionAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return RouteData.data.getPlacesList((String) constraint);
            }
        });

        fromPlace = (SearchView) findViewById(R.id.from);
        toPlace = (SearchView) findViewById(R.id.to);

        fromPlace.setSuggestionsAdapter(mFromSuggestionAdapter);
        toPlace.setSuggestionsAdapter(mToSuggestionAdapter);

        SearchableInfo searchableInfo = ((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getSearchableInfo(getComponentName());
        fromPlace.setSearchableInfo(searchableInfo);
        fromPlace.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        fromPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchView) v).setIconified(false);
            }
        });

        toPlace.setSearchableInfo(searchableInfo);
        toPlace.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        toPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchView) v).setIconified(false);
            }
        });

        SearchView.OnQueryTextListener listener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchlist(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mFromSuggestionAdapter.getFilter().filter(newText);

                return true;
            }
        };

        SearchView.OnQueryTextListener listener2 = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                searchlist(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mToSuggestionAdapter.getFilter().filter(newText);

                return true;
            }
        };



        fromPlace.setOnQueryTextListener(listener);
        toPlace.setOnQueryTextListener(listener2);

        fromPlace.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) fromPlace.getSuggestionsAdapter().getItem(position);
                fromPlace.setQuery(cursor.getString(1),false);
                from = cursor.getInt(0);
                toPlace.setVisibility(View.VISIBLE);
//                int indexColumnSuggestion = cursor.getColumnIndex(MS.FAMOUSNAME);
//                fromPlace
//                startSearch(cursor.getString(indexColumnSuggestion), cursor.getInt(1));

                return true;
            }
        });

        toPlace.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) toPlace.getSuggestionsAdapter().getItem(position);
                toPlace.setQuery(cursor.getString(1),false);
                to = cursor.getInt(0);
//                int indexColumnSuggestion = cursor.getColumnIndex(MS.FAMOUSNAME);
//                fromPlace
//                startSearch(cursor.getString(indexColumnSuggestion), cursor.getInt(1));

                return true;
            }
        });
    }
}
