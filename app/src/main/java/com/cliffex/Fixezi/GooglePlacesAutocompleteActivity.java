package com.cliffex.Fixezi;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.cliffex.Fixezi.Constant.PreferenceConnector;

public class GooglePlacesAutocompleteActivity extends Activity {

    private AutoCompleteTextView gettypedlocation;
    private Integer THRESHOLD = 2;
    private int count = 0;
    private double longitude;
    private double latitude;
    private String order_landmarkadd;
    private String order_landmarkadd1;
    private TextView cancle_text;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_address);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        cancle_text = (TextView) findViewById(R.id.cancle_text);

        cancle_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        autocompleteView();

    }

    private void autocompleteView() {

        gettypedlocation = (AutoCompleteTextView) findViewById(R.id.gettypedlocation);

        gettypedlocation.requestFocus();
        gettypedlocation.setThreshold(THRESHOLD);
        gettypedlocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    // clear_pick_ic.setVisibility(View.VISIBLE);
                    loadData(gettypedlocation.getText().toString());
                } else {
                    // clear_pick_ic.setVisibility(View.GONE);
                }
            }
        });
    }

    private void loadData(String s) {

        try {

            if (count == 0) {

                List<String> l1 = new ArrayList<>();

                if (s == null) {

                } else {

                    l1.add(s);

                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(GooglePlacesAutocompleteActivity.this, l1, "" + latitude, "" + longitude);
                    gettypedlocation.setAdapter(ga);
                    ga.notifyDataSetChanged();

                }

            }
            count++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GeoAutoCompleteAdapter extends BaseAdapter implements Filterable {

        private Activity context;
        private List<String> l2 = new ArrayList<>();
        private LayoutInflater layoutInflater;
        private WebOperations wo;
        private String lat, lon;

        public GeoAutoCompleteAdapter(Activity context, List<String> l2, String lat, String lon) {
            this.context = context;
            this.l2 = l2;
            this.lat = lat;
            this.lon = lon;
            layoutInflater = LayoutInflater.from(context);
            wo = new WebOperations(context);
        }

        @Override
        public int getCount() {
            return l2 == null ? 0 : l2.size();
        }

        @Override
        public Object getItem(int i) {
            return l2.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = layoutInflater.inflate(R.layout.geo_search_result, viewGroup, false);
            TextView geo_search_result_text = (TextView) view.findViewById(R.id.geo_search_result_text);
            try {
                geo_search_result_text.setText(l2.get(i));
                geo_search_result_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        if (l2 == null || l2.isEmpty()) {
                        } else {

                            Log.e("sdfasdfsfasf", "lat = " + lat + " lon = " + lon);

                            gettypedlocation.setText("" + l2.get(i));
                            gettypedlocation.dismissDropDown();

                            order_landmarkadd = gettypedlocation.getText().toString();

                            PreferenceConnector.writeString(GooglePlacesAutocompleteActivity.this, PreferenceConnector.Address_Save, order_landmarkadd);
                            Intent intent = new Intent();
                            intent.putExtra("add", order_landmarkadd);

//                          intent.putExtra("lat",gettypedlocation);
//                          intent.putExtra("lon",order_landmarkadd);

                            setResult(101, intent);
                            finish();

                        }

                    }
                });

            } catch (Exception e) {}

            return view;

        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        Log.e("aasdsdadsad", "Places Details = " + "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDQhXBxYiOPm-aGspwuKueT3CfBOIY3SJs&input=" + constraint.toString().trim().replaceAll(" ", "+") + "&location=" + lat + "," + lon + "+&radius=20000&types=geocode&sensor=true");
                        wo.setUrl("https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyDQhXBxYiOPm-aGspwuKueT3CfBOIY3SJs&input=" + constraint.toString().trim().replaceAll(" ", "+") + "&location=" + lat + "," + lon + "+&radius=20000&types=geocode&sensor=true");
                        String result = null;
                        try {
                            result = new MyTask(wo, 3).execute().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        parseJson(result);

                        // Assign the data to the FilterResults
                        filterResults.values = l2;
                        filterResults.count = l2.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if (results != null && results.count != 0) {
                        l2 = (List) results.values;
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }

        private void parseJson(String result) {

            Log.e("asdasdasdasdas", "Parse Json = " + result);
            try {
                l2 = new ArrayList<>();
                JSONObject jk = new JSONObject(result);

                JSONArray predictions = jk.getJSONArray("predictions");
                for (int i = 0; i < predictions.length(); i++) {
                    JSONObject js = predictions.getJSONObject(i);
                    l2.add(js.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}