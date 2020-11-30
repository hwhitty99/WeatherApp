package com.ebookfrenzy.weatherapp.Interfaces;


import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.City;

public interface CallbackInterface {
    void onItemClick(String city) throws WeatherProviderInstantiationException;
    void onItemLongClick(int position);
}
