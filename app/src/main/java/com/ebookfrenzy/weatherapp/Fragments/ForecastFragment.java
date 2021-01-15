package com.ebookfrenzy.weatherapp.Fragments;


import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ForecastFragment extends Fragment {

    private static final String DAYS = "daysOfWeekArray";
    private static final String TEMPS = "temperaturesArray";
    private static final String IMGS = "imagesArray";
    private static final String CITY = "cityName";

    private String[] mdaysOfWeekArray;
    private int[] mtemperatureArray;
    private Image[] mimagesArray;
    private String mcityName;
    private String cityId;

    private TextView day1, day2, day3, day4, day5, HL1, HL2, HL3, HL4, HL5;
    private TextView forecastTitle;
    private ImageView day1Image, day2Image, day3Image, day4Image, day5Image;




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

        forecastTitle = view.findViewById(R.id.forecastTitle);

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

        try {
            displayForecast();
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void displayForecast() throws WeatherProviderInstantiationException {
        config = new WeatherConfig();
        config.unitSystem = WeatherConfig.UNIT_SYSTEM.I;
        config.maxResult = 5;
        config.numDays = 5;
        config.ApiKey = "a6a3f21e908d05f2d5452e23f7d12a83";


        WeatherClient client = (new WeatherClient.ClientBuilder()).attach(getActivity())
                .provider(new OpenweathermapProviderType())
                .httpClient(com.survivingwithandroid.weather.lib.client.okhttp.WeatherDefaultClient.class)
                .config(config)
                .build();

        client.searchCity(mcityName, new WeatherClient.CityEventListener() {
            @Override
            public void onWeatherError(WeatherLibException wle) {

            }

            @Override
            public void onConnectionError(Throwable t) {

            }

            @Override
            public void onCityListRetrieved(List<City> cityList) {

                if(cityList.size() != 0) {
                    cityId = cityList.get(0).getId();
                } else {
                    cityId = "6295630";
                    Toast.makeText(getActivity(), "Not a city", Toast.LENGTH_SHORT).show();
                }

                client.getCurrentCondition(new WeatherRequest(cityId), new WeatherClient.WeatherEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onWeatherRetrieved(CurrentWeather mWeather) {
                        Weather weather = mWeather.weather;
                        forecastTitle.setText("5 Day Forecast of " + weather.location.getCity());
                        // day1.setText(DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().getTime()));
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                        Date d = new Date();
                        String dayOfTheWeek = sdf.format(d);
                        day1.setText(dayOfTheWeek);
                        HL1.setText("H: " + weather.temperature.getMaxTemp() + mWeather.getUnit().tempUnit + "\nL: " + weather.temperature.getMinTemp() + mWeather.getUnit().tempUnit);

                        client.getDefaultProviderImage(weather.currentCondition.getIcon(), new WeatherClient.WeatherImageListener() {
                            @Override
                            public void onWeatherError(WeatherLibException wle) {

                            }

                            @Override
                            public void onConnectionError(Throwable t) {

                            }

                            @Override
                            public void onImageReady(Bitmap mImage) {
                                day1Image.setImageBitmap(mImage);
                            }
                        });

                    }

                    @Override
                    public void onWeatherError(WeatherLibException wle) {

                    }

                    @Override
                    public void onConnectionError(Throwable t) {

                    }
                });
            }
        });
    }

}
