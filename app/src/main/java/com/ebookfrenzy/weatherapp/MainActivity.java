package com.ebookfrenzy.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ebookfrenzy.weatherapp.Adapters.CitiesRecyclerViewAdapter;
import com.ebookfrenzy.weatherapp.Fragments.AddCityFragment;
import com.ebookfrenzy.weatherapp.Fragments.CitiesFragment;
import com.ebookfrenzy.weatherapp.Fragments.CurrentFragment;
import com.ebookfrenzy.weatherapp.Fragments.ForecastFragment;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CitiesFragment.OnSelectCityListener, CurrentFragment.CurrentFragmentListener, AddCityFragment.onAddCityListener {

    private static final String ARGS_LAST_CITY = "cityToDisplay";
    private static final String ARGS_SAVED_CITY_LIST = "savedCityList";
    private static final String TAG = "";
    private Menu optionsMenu;
    private String cityToDisplayString;

    private CurrentFragment mCurrentFragment = new CurrentFragment();
    private CitiesFragment citiesFragment = new CitiesFragment();
    private AddCityFragment addCityFragment = new AddCityFragment();
    private ForecastFragment forecastFragment = new ForecastFragment();
    private CitiesRecyclerViewAdapter recyclerViewAdapter = new CitiesRecyclerViewAdapter();

    private WeatherConfig config = new WeatherConfig();
    private WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
    private WeatherClient client;

    Bundle savedArgs = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showOverflowMenu(true);

        config.unitSystem = WeatherConfig.UNIT_SYSTEM.I;
        config.maxResult = 5;
        config.numDays = 5;
        config.ApiKey = "a6a3f21e908d05f2d5452e23f7d12a83";

        try {
            client = (builder).provider(new OpenweathermapProviderType())
                    .httpClient(com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient.class)
                    .config(config)
                    .build();
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }

        if(savedInstanceState != null) {
            if (savedInstanceState.getString(ARGS_LAST_CITY) != null) {
                savedArgs.putString(CurrentFragment.ARG_CITY_NAME, savedInstanceState.getString(ARGS_LAST_CITY));
            }
        }
        if(savedInstanceState != null) {
                if (savedInstanceState.getStringArrayList(ARGS_SAVED_CITY_LIST) != null) {
                savedArgs.putStringArrayList(CitiesFragment.ARGS_CITY_LIST, savedInstanceState.getStringArrayList(ARGS_SAVED_CITY_LIST));
            }
        }

        updateCurrentFragment();

        Intent intent = new Intent(this, RefreshService.class);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.optionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        FragmentManager fragmentManager = getSupportFragmentManager();

        int id = item.getItemId();

        try {
            if (id == R.id.menu_add_city) {
                if (savedArgs != null){
                    addCityFragment.setArguments(savedArgs);
                }
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, addCityFragment)
                        .addToBackStack(null)
                        .commit();

                showOverflowMenu(false);

                return true;
            } else if ((id == R.id.menu_remove_city) || (id == R.id.menu_select_city)) {
                if (savedArgs != null){
                    citiesFragment.setArguments(savedArgs);
                }
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, citiesFragment)
                        .addToBackStack(null)
                        .commit();

                showOverflowMenu(false);

                return true;
            } else {
                return super.onOptionsItemSelected(item);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateCurrentFragment() {

        mCurrentFragment = CurrentFragment.newInstance(cityToDisplayString);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, mCurrentFragment)
                .commit();

        showOverflowMenu(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showOverflowMenu(true);
    }

    @Override
    public void onForecastClick(View view, String city) {

        forecastFragment = ForecastFragment.newInstance(city);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, forecastFragment)
                .addToBackStack(null)
                .commit();

        showOverflowMenu(false);

        Toast.makeText(view.getContext(), "5 Day Forecast", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCitySelected(String cityString) throws WeatherProviderInstantiationException {

        cityToDisplayString = cityString;
        updateCurrentFragment();

        Toast.makeText(this, cityString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddCityButtonClicked(String addedCity) {

        if (savedArgs != null){
            citiesFragment.setArguments(savedArgs);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, citiesFragment).addToBackStack(null);
        transaction.commit();

        citiesFragment.addCityToList(addedCity);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        outState.putString(ARGS_LAST_CITY, cityToDisplayString);

        outState.putStringArrayList(ARGS_SAVED_CITY_LIST, (ArrayList<String>) citiesFragment.getCityArrayList());
    }

    public void showOverflowMenu(boolean showMenu) {
        if (optionsMenu == null) {
            return;
        }
        optionsMenu.setGroupVisible(R.id.main_menu_group, showMenu);
    }
}
