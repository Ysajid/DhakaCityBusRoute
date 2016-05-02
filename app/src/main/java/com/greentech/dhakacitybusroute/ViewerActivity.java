package com.greentech.dhakacitybusroute;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ViewerActivity extends AppCompatActivity{

    int from;
    int to;
    CursorAdapter adapter;
    Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Log.d("viewer Activity", "onCreate");

        Bundle bundle = getIntent().getExtras();
        from = bundle.getInt(RouteData.STARTPLACE);
        to = bundle.getInt(RouteData.DESTINATION);


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content,ViewerFragment.newInstance(from,to))
                .commit();



//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle(search);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
//        if (savedInstanceState == null) {
//            android.app.FragmentManager fragmentManager = getFragmentManager();
//            Log.d("type", ""+type);
//            switch (type) {
//                case 1:
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.content, ScholarsListFragmentLoader.newInstance(search))
//                            .commit();
//                    break;
//                case 2:
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.content, TagsFragment.newInstance())
//                            .commit();
//                    break;
//                case 3:
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.content, CustScholarsListFragment.newInstance(position))
//                            .commit();
//            }
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



//    public void setTitletext(String s) {
//        getSupportActionBar().setTitle(s);
//    }

}
