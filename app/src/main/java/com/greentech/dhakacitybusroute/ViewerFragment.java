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

            view.setTag(holder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            RowHolder holder = (RowHolder) view.getTag();
            holder.bus_name.setText(cursor.getString(1));

            String route = cursor.getString(2);

            route = (String) route.subSequence(1,route.length() - 1);

            String arr[] = route.split(",,");
            String temp[];

            boolean startFound = false;

            for(String place_id : arr){
                if(Integer.parseInt(place_id) == mFrom) startFound = true;
                if(startFound){

                }
            }

            for(int i=holder.route.getChildCount()-1; i<arr.length; i++){
                TextView nameTV = new TextView(getActivity());
                holder.route.addView(nameTV);
            }
            if(holder.route.getChildCount() > arr.length) holder.route.removeViews(arr.length-1 , holder.route.getChildCount());

            int i = 0;

            for (String place_id: arr) {
                if(Integer.parseInt(place_id) == mFrom) startFound = true;
                else if (Integer.parseInt(place_id) == mTo) break;
                if(startFound) {
                    String name = RouteData.data.getPlaceName(place_id);
                    TextView nameTV = (TextView) holder.route.getChildAt(i);
                    nameTV.setText(name);
                }
                i++;
            }
            if(holder.route.getChildCount() == 0){

            }
        }
    }

    class RowHolder {
        TextView bus_name;
        LinearLayout route;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
