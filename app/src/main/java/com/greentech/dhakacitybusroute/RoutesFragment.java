package com.greentech.dhakacitybusroute;

import android.content.Context;
import android.database.Cursor;
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
 * to handle interaction events.
 * Use the {@link RoutesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RoutesFragment extends ListFragment implements LoaderManager.LoaderCallbacks {


    Cursor mCursor;
    CursorAdapter mAdapter;


    public static RoutesFragment newInstance() {
        RoutesFragment fragment = new RoutesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new CustAdapter(getActivity(), null, false);
//        getListView().setFastScrollEnabled(true);
//        getListView().setOnItemLongClickListener(onItemLongClick);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            getListView().getSelector().setColorFilter(fetchColor(getActivity()), PorterDuff.Mode.SRC_IN);
//        }

        setListAdapter(mAdapter);
//        getListView().setDividerHeight(6);
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routes, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CustCursorLoader(getActivity(), MainActivity.ROUTES);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        mCursor = (Cursor) data;
//        Log.d("cursor count", ""+mCursor.getCount());
        mAdapter.swapCursor(mCursor);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        mCursor = null;
        mAdapter.swapCursor(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */


    class CustAdapter extends CursorAdapter {

        public CustAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.list_item_2, null);

            RowHolder holder = new RowHolder();

            holder.route_id = (TextView) view.findViewById(R.id.route_id);
            holder.route = (LinearLayout) view.findViewById(R.id.routes);
//            holder.distance = (TextView) view.findViewById(R.id.distance);

            view.setTag(holder);

            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            RowHolder holder = (RowHolder) view.getTag();
            holder.route_id.setText(cursor.getString(0));

            String route = cursor.getString(1);

            route = (String) route.subSequence(2,route.length() - 2);

            String arr[] = route.split(",,");
            int len = arr.length;

            int temp[] = new int[len];


            boolean startFound = false;

            int places_between = 0;

//            for(int i=0; i<len; i++){
//                String place_id = arr[i];
//                if(Integer.parseInt(place_id) == mFrom) {startFound = true; start = i;}
//                if(startFound){
//                    temp[places_between++] = Integer.parseInt(place_id);
//                    if(Integer.parseInt(place_id) == mTo) { endFound = true; end = i; break;}
//                }
//            }

            for(int i=holder.route.getChildCount()-1; i < len; i++){
                TextView nameTV = new TextView(getActivity());
                holder.route.addView(nameTV);
            }


            for (int i=0; i < len; i++) {
                String name = RouteData.data.getPlaceName(Integer.parseInt(arr[i]));
                TextView nameTV = (TextView) holder.route.getChildAt(i);
                if(i + 1 < len) name += " -- ";
                nameTV.setText(name);
            }

            for(int i=len; i<holder.route.getChildCount(); i++) {
                holder.route.removeViewAt(i);
            }

//            holder.distance.setText(String.format("%.2f", distance) + " Km");
        }
    }

    class RowHolder {
        TextView route_id;
        LinearLayout route;
        TextView distance;
    }

}
