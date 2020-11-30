package com.ebookfrenzy.weatherapp.Fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ebookfrenzy.weatherapp.R;
import com.survivingwithandroid.weather.lib.model.City;



public class AddCityFragment extends Fragment {

    onAddCityListener activityCallback;

    private String mCity;
    private String mCountry;
    private String cityString;
    private String countryString;
    private City mCityToAdd;

    public AddCityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            activityCallback = (onAddCityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onAddCityListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_city, container, false);

        EditText cityET = view.findViewById(R.id.addCityET);
        Spinner countrySpinner = view.findViewById(R.id.addCitySpinner);
        Button addCityButton = view.findViewById(R.id.addCityButton);

        addCityButton.setOnClickListener(v -> {
            cityString = cityET.getText().toString();
            countryString = countrySpinner.getSelectedItem().toString();

            activityCallback.onAddCityButtonClicked(cityString + ", " + countryString);
        });
        return view;
    }

    public interface onAddCityListener {
        void onAddCityButtonClicked(String addedCity);
    }
}
