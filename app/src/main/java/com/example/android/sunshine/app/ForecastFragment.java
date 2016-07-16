package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ForecastFragment extends Fragment{

        protected ArrayAdapter<String> mForecastAdapter;

        public ForecastFragment() {
        }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.action_refresh){
            updateWeather();
            return true;

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            //List<String> weekForecast = new ArrayList<String>(Arrays.asList(forecastArray));
            mForecastAdapter = new ArrayAdapter<String> (getActivity(),R.layout.list_item_forecast,R.id.list_item_forecast_textview,new ArrayList<String>());
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(mForecastAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                    String forecast = mForecastAdapter.getItem(i);
                    //Toast.makeText(getActivity(), forecast, Toast.LENGTH_SHORT).show();
                    Intent activityDetailIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);
                    // Verify that the intent will resolve to an activity
                    //if (activityDetailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(activityDetailIntent);
                    //}
                }
            }) ;

            return rootView;
        }
        private void updateWeather(){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String location = prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
            FetchWeatherTask fetchWeatherTask = new FetchWeatherTask(getActivity(),mForecastAdapter);
            fetchWeatherTask.execute(location);
        }

        @Override
        public void onStart(){
            super.onStart();
            updateWeather();
        }
}
