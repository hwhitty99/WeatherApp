package com.ebookfrenzy.weatherapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.weatherapp.Adapters.CitiesRecyclerViewAdapter;
import com.ebookfrenzy.weatherapp.Interfaces.CallbackInterface;
import com.ebookfrenzy.weatherapp.R;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link}
 * interface.
 */
public class CitiesFragment extends Fragment implements CallbackInterface {

    private static final String DEFAULT_CITY = "Bourbonnais,United States";
    public static final String ARGS_CITY_LIST = "cityList";

    private List<String> cityArrayList = new ArrayList<>();
    private CitiesRecyclerViewAdapter citiesRecyclerViewAdapter;

    private OnSelectCityListener activityCallback;

    public interface OnSelectCityListener {
        void onCitySelected(String city) throws WeatherProviderInstantiationException;
    }

    @Override
    public void onItemClick(String city) throws WeatherProviderInstantiationException {
        citySelected(city);
        Log.d(TAG, "CitiesFragment onItemClick: " + city);
    }

    @Override
    public void onItemLongClick(int position) {
        if (position == 0) {
            Toast.makeText(getActivity(), "You gotta keep at least 1 right?", Toast.LENGTH_SHORT).show();
        }
    }

    public CitiesFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert getArguments() != null;
        if (getArguments().getStringArrayList(ARGS_CITY_LIST) != null) {
            cityArrayList = getArguments().getStringArrayList(ARGS_CITY_LIST);
        } else {
            cityArrayList.add(0, DEFAULT_CITY);
            getArguments().putStringArrayList(ARGS_CITY_LIST, (ArrayList<String>) cityArrayList);
        }

        citiesRecyclerViewAdapter = new CitiesRecyclerViewAdapter(cityArrayList, getActivity(), this);

        View view = inflater.inflate(R.layout.city_recycler, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

        recyclerView.setAdapter(citiesRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return view;
    }

    @Override
    public void onAttach(Context context) throws ClassCastException {
        super.onAttach(context);

        try {
            activityCallback = (OnSelectCityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(new StringBuilder().append(Objects.requireNonNull(getActivity()).toString())
                    .append(" must implement OnSelectCityListener").toString());
        }
    }

    public void addCityToList(String city) {
        cityArrayList.add(0, city);
        assert getArguments() != null;
        getArguments().putStringArrayList(ARGS_CITY_LIST, (ArrayList<String>) cityArrayList);
    }

    public List<String> getCityArrayList() {
        return cityArrayList;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void citySelected(String city) throws WeatherProviderInstantiationException {
        activityCallback.onCitySelected(city);
    }
}
