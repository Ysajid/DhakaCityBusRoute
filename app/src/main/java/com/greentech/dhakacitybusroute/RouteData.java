package com.greentech.dhakacitybusroute;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.greentech.dhakacitybusroute.SqliteHelper.SQLiteAssetHelper;

/**
 * Created by sajid on 4/16/16.
 */
public class RouteData {

    public static final String NAME = "name";
    public static final String ID = "_id";
    public static final String STARTPLACE = "starting place";
    public static final String DESTINATION = "destination";

    public static final RouteData data = new RouteData();

    public RouteData(){

    }

    private SQLiteDatabase mDB;

    public void open(Context context) {
        try {
            DataBaseHelper dbhelper = new DataBaseHelper(context);
            mDB = dbhelper.getReadableDatabase();
            Log.d("frag db  open", " mDB " + mDB.getPath());
        } catch (SQLException sqlexception) {
            Log.d("RouteData", sqlexception.toString());
        }
    }

    private void close() {
        if (mDB != null) {
            mDB.close();
            mDB = null;
        }
    }

    public Cursor getPlacesList(String newText) {
        try {
            return mDB.rawQuery("select place_id as _id, name from place where name like '%" + newText + "%' ", null);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Cursor getAvailBuses(int start, int end) {
        try {
            return mDB.rawQuery("select route_id as _id, name , place_ids " +
                                "from route , bus" +
                                " where id = bus_id and " +
                                "place_ids like '%," + start + ",%' and place_ids like '%,"+end+",%'", null);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public String getPlaceName(String id) {
        try {
            Log.d("place id = ", id);
            Cursor cursor = mDB.rawQuery("select place_id as _id, name from place where place_id ='"+id+"'", null);
            cursor.moveToFirst();
            return cursor.getString(1);
        }catch (SQLException e){
            e.printStackTrace();
            return "";
        }
    }

    public class DataBaseHelper extends SQLiteAssetHelper {

        public static final String DATABASE_NAME = "Dhakacitybusroutes.db";
        public static final int DATABASE_VERSION = 1;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.setForcedUpgrade(); //because it is read only database so ForceUpgrade change or remove it is not read only
            Log.d("DatabaseHelper  ", "constructor");
        }
    }
}
