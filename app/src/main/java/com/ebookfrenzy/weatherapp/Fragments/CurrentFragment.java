package com.ebookfrenzy.weatherapp.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import java.util.Calendar;
import java.util.List;


public class CurrentFragment extends Fragment implements View.OnClickListener {

    public static final String ARG_CITY_NAME = "cityName";

    private TextView cityTV;
    private TextView dateTV;
    private TextView conditionsTV;
    private TextView currentTV;
    private TextView highTV;
    private TextView lowTV;
    private ImageView image;

    private WeatherConfig config;

    private String mCity;
    private String cityId;

    private CurrentFragmentListener activityCallback;

    public interface CurrentFragmentListener {
        void onForecastClick(View view, String city);
    }

    public static CurrentFragment newInstance(String city) {
        CurrentFragment currentFragment = new CurrentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CITY_NAME, city);
        currentFragment.setArguments(args);
        return currentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current, container, false);

        cityTV = view.findViewById(R.id.current_city_tv);
        dateTV = view.findViewById(R.id.current_date_tv);
        conditionsTV = view.findViewById(R.id.current_conditions_tv);
        currentTV = view.findViewById(R.id.current_temp_tv);
        highTV = view.findViewById(R.id.current_high_tv);
        lowTV = view.findViewById(R.id.current_low_tv);
        image = view.findViewById(R.id.imageView);

        final Button forecastButton = view.findViewById(R.id.view_forecast_button);

        forecastButton.setOnClickListener(
                this::forecastClicked
        );

        assert getArguments() != null;
        if (getArguments().getString(ARG_CITY_NAME) != null) {
            mCity = getArguments().getString(ARG_CITY_NAME);
        } else {
            mCity = "Bourbonnais, United States";
        }

        try {
            refresh();
        } catch (WeatherProviderInstantiationException e) {
            e.printStackTrace();
        }

        return view;
    }

    private void forecastClicked(View view) {
        activityCallback.onForecastClick(view, mCity);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            activityCallback = (CurrentFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CurrentFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void refresh() throws WeatherProviderInstantiationException {
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

        client.searchCity(mCity, new WeatherClient.CityEventListener() {
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
                                cityTV.setText(weather.location.getCity());
                                dateTV.setText(DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().getTime()));
                                conditionsTV.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
                                currentTV.setText(String.valueOf(weather.temperature.getTemp()) + mWeather.getUnit().tempUnit);
                                highTV.setText(weather.temperature.getMaxTemp() + mWeather.getUnit().tempUnit);
                                lowTV.setText(weather.temperature.getMinTemp() + mWeather.getUnit().tempUnit);

                                client.getDefaultProviderImage(weather.currentCondition.getIcon(), new WeatherClient.WeatherImageListener() {
                                    @Override
                                    public void onWeatherError(WeatherLibException wle) {

                                    }

                                    @Override
                                    public void onConnectionError(Throwable t) {

                                    }

                                    @Override
                                    public void onImageReady(Bitmap mImage) {
                                        image.setImageBitmap(mImage);
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

        client.getCurrentCondition(new WeatherRequest(cityId), new WeatherClient.WeatherEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onWeatherRetrieved(CurrentWeather mWeather) {
                Weather weather = mWeather.weather;
                cityTV.setText(weather.location.getCity());
                dateTV.setText(DateFormat.getDateInstance(DateFormat.FULL).format(Calendar.getInstance().getTime()));
                conditionsTV.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
                currentTV.setText(String.valueOf(weather.temperature.getTemp()) + mWeather.getUnit().tempUnit);
                highTV.setText(weather.temperature.getMaxTemp() + mWeather.getUnit().tempUnit);
                lowTV.setText(weather.temperature.getMinTemp() + mWeather.getUnit().tempUnit);

                client.getDefaultProviderImage(weather.currentCondition.getIcon(), new WeatherClient.WeatherImageListener() {
                    @Override
                    public void onWeatherError(WeatherLibException wle) {

                    }

                    @Override
                    public void onConnectionError(Throwable t) {

                    }

                    @Override
                    public void onImageReady(Bitmap mImage) {
                        image.setImageBitmap(mImage);
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

    @Override
    public void onClick(View v) {

    }
}
