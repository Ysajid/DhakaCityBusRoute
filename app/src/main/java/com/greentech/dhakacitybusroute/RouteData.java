package com.greentech.dhakacitybusroute;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Float2;
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
    public static final float fairPerKm = 1.6f;
    public static final float fairPerKmMax = 1.7f;
    public static final int minimumFair = 5;

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

            Log.d("Route", "from "+start+" to "+end);
            return mDB.rawQuery("select route_id as _id, name , place_ids " +
                                "from route , bus" +
                                " where id = bus_id and " +
                                "place_ids like '%," + start + ",%' and place_ids like '%,"+end+",%'" +
                                "and (isCircularRoute = 0 or place_ids like '%," + start + ",%,"+end+"')", null);
        }
        catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean isCircular(String routeId) {
        try {
            Cursor cursor = mDB.rawQuery("select isCircularRoute from route where route_id = " + routeId, null);
            cursor.moveToFirst();
            int isIt = cursor.getInt(0);
            cursor.close();
            return isIt == 1;
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public float getDistance(String routeId, int start , int end) {
        try {
            Cursor cursor = mDB.rawQuery("select distances from route where route_id = '" + routeId+"'", null);
            cursor.moveToFirst();
            String disStr = cursor.getString(0);
            cursor.close();
            String distances[] = disStr.split(",");
            float startDis = 0, endDis = 0;

            for(int i=0; i<distances.length; i++){
                Log.d("distances", distances[i]);
                if(i == start) startDis = Float.parseFloat(distances[i]);
                else if(i == end) endDis = Float.parseFloat(distances[i]);
            }

            Log.d("distances", startDis+" "+endDis);

            return (startDis > endDis)? startDis - endDis : endDis - startDis;

        }
        catch(SQLException e){
            e.printStackTrace();
            return 0.0f;
        }
    }

    public String getPlaceName(int id) {
        try {
            Log.d("place id = ", id+"");
            Cursor cursor = mDB.rawQuery("select place_id as _id, name from place where place_id ="+id, null);
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
