//package com.greentech.dhakacitybusroute;
//
//import android.app.Fragment;
//import android.app.SearchManager;
//import android.app.SearchableInfo;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.database.CursorIndexOutOfBoundsException;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.support.annotation.Nullable;
//import android.support.v4.widget.SimpleCursorAdapter;
//import android.support.v7.widget.SearchView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.EditorInfo;
//import android.widget.AdapterView;
//import android.widget.FilterQueryProvider;
//import android.widget.TextView;
//
//import com.greentech.muslimscholars.Activity.ListActivity;
//import com.greentech.muslimscholars.Activity.ViewerActivity;
//import com.greentech.muslimscholars.Data.MS;
//import com.greentech.muslimscholars.R;
//import com.greentech.muslimscholars.TagUtils.FlowLayout;
//
//import java.util.Random;
//
//public class MainFragment extends Fragment {
//
//    public static String SEARCH = "search";
//    public static String TYPE = "type";
//    public static String ID = "rowid";
//    public static String POS = "position";
//
//    private final AdapterView.OnItemClickListener gotodualist = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            String s = MS.tags[position];
//
//            Bundle searchBundle = new Bundle();
//            searchBundle.putString(MainFragment.SEARCH, s);
//
//            Intent intent = new Intent(getActivity(), ListActivity.class);
//            intent.putExtras(searchBundle);
//            startActivity(intent);
//        }
//    };
//    SearchView searchView;
//
//    private View.OnClickListener tagClicked(final int position) {
//
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String s = MS.tags[position];
//
//                Bundle searchBundle = new Bundle();
//                searchBundle.putString(MainFragment.SEARCH, s);
//                searchBundle.putInt(MainFragment.TYPE, 1);
//
//                Intent intent = new Intent(getActivity(), ListActivity.class);
//                intent.putExtras(searchBundle);
//                startActivity(intent);
//            }
//        };
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_main, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        long t1 = SystemClock.uptimeMillis();
//
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        searchView = (SearchView) getView().findViewById(R.id.searchview);
//        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getActivity().getComponentName());
//        searchView.setSearchableInfo(searchableInfo);
//        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((SearchView) v).setIconified(false);
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            SimpleCursorAdapter mSuggestionAdapter;
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                searchlist(query); // TODO: 12/22/15 query text view is not in the center
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (mSuggestionAdapter == null) {
//                    Cursor cursor = MS.ms.getScholarsList(newText);
//                    String[] columns = new String[]{MS.FAMOUSNAME, MS.ID_REAL};
//                    int[] columnTextId = new int[]{android.R.id.text1};
//
//                    mSuggestionAdapter = new SimpleCursorAdapter(getActivity(),
//                            android.R.layout.simple_list_item_1, cursor,
//                            columns, columnTextId, 0);
//
//                    mSuggestionAdapter.setFilterQueryProvider(new FilterQueryProvider() {
//                        @Override
//                        public Cursor runQuery(CharSequence charSequence) {
//                            return MS.ms.getScholarsList((String) charSequence);
//                        }
//                    });
//                    searchView.setSuggestionsAdapter(mSuggestionAdapter);
//                } else
//                    mSuggestionAdapter.getFilter().filter(newText);
//                return true;
//            }
//        });
//
//        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//            @Override
//            public boolean onSuggestionSelect(int position) {
//                return false;
//            }
//
//            @Override
//            public boolean onSuggestionClick(int position) {
//                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
//                int indexColumnSuggestion = cursor.getColumnIndex(MS.FAMOUSNAME);
//
//                startSearch(cursor.getString(indexColumnSuggestion), cursor.getInt(1));
//
//                return true;
//            }
//        });
//
//        final TextView textViewTitle = (TextView) getView().findViewById(R.id.randomTitle);
//        final TextView textView = (TextView) getView().findViewById(R.id.random);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            textViewTitle.setBackground(getResources().getDrawable(R.drawable.item_bg_ripple, getActivity().getTheme()));
//        }
//        Random random = new Random();
//        final int num = random.nextInt(25260);
//
//        new AsyncTask<Void, Void, Cursor>() {
//            @Override
//            protected Cursor doInBackground(Void... params) {
//                Cursor cursor = null;
//                while (cursor == null) {
//                    cursor = MS.ms.fetchRandomScholar(num);
//                }
//                return cursor;
//            }
//
//            @Override
//            protected void onPostExecute(Cursor cursor) {
//                cursor.moveToFirst();
//                try {
//                    final String name = cursor.getString(0);
//                    String rank = MS.ranks[cursor.getInt(1)];
//                    String birthdate = cursor.getString(2);
//                    String birthcity = cursor.getString(3);
//                    String deathdate = cursor.getString(4);
//                    String deathcity = cursor.getString(5);
////            String tags = cursor.getString(6);
////            String kunya = cursor.getString(7);
//
//                    String text = name + "\n" + rank + "\n" + birthdate + " in " + birthcity + " - "
//                            + deathdate + " in " + deathcity;
//
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append(rank).append("\n");
//
//                    if (birthdate != null)
//                        stringBuilder.append(birthdate);
//
//                    if (birthcity.length() > 1)
//                        stringBuilder.append(" (").append(birthcity).append(") ");
//
//                    stringBuilder.append("-");
//
//                    if (deathdate != null)
//                        stringBuilder.append(deathdate);
//
//                    if (deathcity.length() > 1)
//                        stringBuilder.append(" (").append(deathcity).append(") ");
//
//                    textViewTitle.setText(name);
//
//                    textView.setText(stringBuilder.toString());
//
//
//                    final int realid = cursor.getInt(8);
//
//                    textViewTitle.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Bundle searchBundle = new Bundle();
//                            Log.d("random scholar ", "clicked");
//                            searchBundle.putString(MainFragment.SEARCH, name);
//                            searchBundle.putInt(MainFragment.ID, realid);
//                            Intent intent = new Intent(getActivity(), ViewerActivity.class);
//                            intent.putExtras(searchBundle);
//
//                            startActivity(intent);
//                        }
//                    });
//                } catch (CursorIndexOutOfBoundsException e) {
//                    e.printStackTrace();
//                }
//                cursor.close();
//                super.onPostExecute(cursor);
//            }
//        }.execute();
//
//
////        GridView gridView = (GridView) getView().findViewById(R.id.taggrid);
////        gridView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_grid, MS.tags));
////        gridView.setOnItemClickListener(gotodualist);
//        initTagView();
//        Log.d("Time category frag ", (SystemClock.uptimeMillis() - t1) + " ms");
//    }
//
//    @Override
//    public void onPause() {
//        searchView.clearFocus();
//        super.onPause();
//    }
//
//    private void initTagView() {
//        FlowLayout tagLayout = (FlowLayout) getActivity().findViewById(R.id.tags);
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        int position = 0;
//
//        Log.d("tag child count", "" + tagLayout.getChildCount());
//        Random rand = new Random(SystemClock.currentThreadTimeMillis());
//        while (tagLayout.getChildCount() < 10) {
//            View view = inflater.inflate(R.layout.item_grid, null);
//
//            tagLayout.addView(view);
//            position = rand.nextInt(MS.tags.length - 1);
//            view.setOnClickListener(tagClicked(position));
//            TextView txt = (TextView) view.findViewById(R.id.text1);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                txt.setBackground(getResources().getDrawable(R.drawable.item_bg_ripple, getActivity().getTheme()));
//            }
//            txt.setText(MS.tags[position]);
//        }
//
//    }
//
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
//
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
//}
