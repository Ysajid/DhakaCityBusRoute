package com.greentech.dhakacitybusroute;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewerFragment extends ListFragment implements LoaderManager.LoaderCallbacks{

    private int mFrom;
    private int mTo;
    private Cursor mCursor;
    private CustAdapter adapter;

    public ViewerFragment() {
        // Required empty public constructor
    }

    public static ViewerFragment newInstance(int from, int to) {
        ViewerFragment fragment = new ViewerFragment();
        Bundle args = new Bundle();
        args.putInt(RouteData.STARTPLACE, from);
        args.putInt(RouteData.DESTINATION, to);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getInt(RouteData.STARTPLACE);
            mTo = getArguments().getInt(RouteData.DESTINATION);
        }


        adapter = new CustAdapter(getActivity(), null, false);
//        getListView().setFastScrollEnabled(true);
//        getListView().setOnItemLongClickListener(onItemLongClick);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            getListView().getSelector().setColorFilter(fetchColor(getActivity()), PorterDuff.Mode.SRC_IN);
//        }

        setListAdapter(adapter);

        getLoaderManager().initLoader(1,null, this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_viewer, container, false);
    }

    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {
        Log.d("loader", "created");
        return new CustCursorLoader(getActivity(), mFrom, mTo);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        mCursor = (Cursor) data;
        Log.d("cursor count", ""+mCursor.getCount());
        adapter.swapCursor(mCursor);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        mCursor = null;
        adapter.swapCursor(null);
    }

    class CustAdapter extends CursorAdapter {

        public CustAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.list_item, null);

            RowHolder holder = new RowHolder();

            holder.bus_name = (TextView) view.findViewById(R.id.bus_name);
            holder.route = (LinearLayout) view.findViewById(R.id.route);
            holder.avail = (TextView) view.findViewById(R.id.availablity);
            holder.distance = (TextView) view.findViewById(R.id.distance);
            holder.minFair = (TextView) view.findViewById(R.id.minibusFair);
            holder.maxFair = (TextView) view.findViewById(R.id.busFair);
            holder.routeId = (TextView) view.findViewById(R.id.routeId);

            view.setTag(holder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            RowHolder holder = (RowHolder) view.getTag();
            holder.bus_name.setText(cursor.getString(1));

            String route = cursor.getString(2);

            route = (String) route.subSequence(2,route.length() - 2);

            String arr[] = route.split(",,");
            int len = arr.length;

            int temp[] = new int[len];

            float distance;
            int minFair;
            int maxFair;

            boolean startFound = false;
            boolean endFound = false;

            int start=0, end=0;

            int places_between = 0;

            for(int i=0; i<len; i++){
                String place_id = arr[i];
                if(Integer.parseInt(place_id) == mFrom) {startFound = true; start = i;}
                if(startFound){
                    temp[places_between++] = Integer.parseInt(place_id);
                    if(Integer.parseInt(place_id) == mTo) { endFound = true; end = i; break;}
                }
            }

            if(!startFound || !endFound) {
                startFound = false;
                endFound = false;
                places_between = 0;

                for(int i=0; i<len/2; i++){
                    String temp_str = arr[i];
                    arr[i] = arr[len - i - 1];
                    arr[len - i - 1] = temp_str;
                }

                for(int i=0; i<len; i++){
                    String place_id = arr[i];
                    if(Integer.parseInt(place_id) == mFrom) {startFound = true; start = i;}
                    if(startFound){
                        temp[places_between++] = Integer.parseInt(place_id);
                        if(Integer.parseInt(place_id) == mTo) { endFound = true; end = i; break;}
                    }
                }
            }

            for(int i=holder.route.getChildCount()-1; i < places_between; i++){
                TextView nameTV = new TextView(getActivity());
                holder.route.addView(nameTV);
            }


            for (int i=0; i < places_between; i++) {
                String name = RouteData.data.getPlaceName(temp[i]);
                TextView nameTV = (TextView) holder.route.getChildAt(i);
                nameTV.setText(name + " -- ");
            }

            for(int i=places_between; i<holder.route.getChildCount(); i++) {
                holder.route.removeViewAt(i);
            }

            distance = RouteData.data.getDistance(cursor.getString(0), start, end);
            minFair = Math.round(distance * RouteData.fairPerKm);
            maxFair = Math.round(distance * RouteData.fairPerKmMax);

            if(minFair < RouteData.minimumFair) minFair = RouteData.minimumFair;
            if(maxFair < RouteData.minimumFair) maxFair = RouteData.minimumFair;

            holder.minFair.setText("Minibus Fair : "+minFair);
            holder.maxFair.setText("Bus Fair : "+maxFair);
            holder.distance.setText(distance + " Km");
            holder.routeId.setText("Route No. "+cursor.getString(0));
        }
    }

    class RowHolder {
        TextView bus_name;
        LinearLayout route;
        TextView avail;
        TextView distance;
        TextView minFair;
        TextView maxFair;
        TextView routeId;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
