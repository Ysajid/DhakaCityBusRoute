package com.greentech.dhakacitybusroute;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;


class CustCursorLoader extends AsyncTaskLoader {

    private final int from;
    private final int to;
    private final int cursorType;
//    private final int cursor_viewType;
//    private final String query;
    private Cursor mCursor;


    public CustCursorLoader(Context context,int cursorType, int from, int to) {
        super(context);
        this.from = from;
        this.to = to;
        this.cursorType = cursorType;
    }

    public CustCursorLoader(Context context,int cursorType){
        super(context);
        this.cursorType = cursorType;
        this.from = 0;
        this.to = 0;
    }


    private void deliverResult(Cursor cursor) {

        if (isReset()) {
            if (cursor != null) {
                cursor.close();
            }
        } else {
            Cursor cursor1 = mCursor;
            mCursor = cursor;
            if (isStarted()) {
                super.deliverResult(cursor);
            }
            if (cursor1 != null && cursor1 != cursor && !cursor1.isClosed()) {
                cursor1.close();
                return;
            }
        }
    }

    public Cursor loadInBackground() {
//        Log.d("loadingx ", catidORquery + " cursor view type  " + cursor_viewType);
        switch (cursorType) {
            case MainActivity.SEARCH:
                return RouteData.data.getAvailBuses(from, to);
            case MainActivity.ROUTES:
                return RouteData.data.getAllRoutes();
            default:
                return RouteData.data.getAvailBuses(from, to);
        }
    }


    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        mCursor = null;
    }

    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    protected void onStopLoading() {
        cancelLoad();
    }

}
