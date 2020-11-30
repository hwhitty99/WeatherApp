package com.ebookfrenzy.weatherapp.Fragments;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ebookfrenzy.weatherapp.R;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.City;
import com.survivingwithandroid.weather.lib.model.CurrentWeather;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    private static final String DAYS = "daysOfWeekArray";
    private static final String TEMPS = "temperaturesArray";
    private static final String IMGS = "imagesArray";
    private static final String CITY = "cityName";

    private String[] mdaysOfWeekArray;
    private int[] mtemperatureArray;
    private Image[] mimagesArray;
    private String mcityName;

    private WeatherConfig config = new WeatherConfig();
    private WeatherClient.ClientBuilder builder = new WeatherClient.ClientBuilder();
    private WeatherClient client;

    public ForecastFragment() {
        // Required empty public constructor
    }

    public static ForecastFragment newInstance(String cityName){

        ForecastFragment forecastFragment = new ForecastFragment();
        Bundle args = new Bundle();
        /*args.putStringArray(DAYS, daysOfWeekArray);
        args.putIntArray(TEMPS, temperaturesArray);
        args.putParcelableArray(IMGS, (Parcelable[]) imagesArray);*/
        args.putString(CITY, cityName);
        forecastFragment.setArguments(args);
        return forecastFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mdaysOfWeekArray = getArguments().getStringArray(DAYS);
            mtemperatureArray = getArguments().getIntArray(TEMPS);
            mimagesArray = (Image[]) getArguments().getParcelableArray(IMGS);
            mcityName = getArguments().getString(CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        TextView forecastTitle = view.findViewById(R.id.forecastTitle);

        TextView day1, day2, day3, day4, day5, HL1, HL2, HL3, HL4, HL5;

        day1 = view.findViewById(R.id.dayOne);
        day2 = view.findViewById(R.id.dayTwo);
        day3 = view.findViewById(R.id.dayThree);
        day4 = view.findViewById(R.id.dayFour);
        day5 = view.findViewById(R.id.dayFive);

        HL1 = view.findViewById(R.id.oneHL);
        HL2 = view.findViewById(R.id.twoHL);
        HL3 = view.findViewById(R.id.threeHL);
        HL4 = view.findViewById(R.id.fourHL);
        HL5 = view.findViewById(R.id.fiveHL);

        ImageView day1Image, day2Image, day3Image, day4Image, day5Image;

        day1Image = view.findViewById(R.id.dayOneImage);
        day2Image = view.findViewById(R.id.dayTwoImage);
        day3Image = view.findViewById(R.id.dayThreeImage);
        day4Image = view.findViewById(R.id.dayFourImage);
        day5Image = view.findViewById(R.id.dayFiveImage);

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

        return view;
    }

}
