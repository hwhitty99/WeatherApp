package com.ebookfrenzy.weatherapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.weatherapp.Interfaces.CallbackInterface;
import com.ebookfrenzy.weatherapp.R;
import com.survivingwithandroid.weather.lib.exception.WeatherProviderInstantiationException;
import com.survivingwithandroid.weather.lib.model.City;

import java.util.List;

import static androidx.constraintlayout.motion.widget.MotionScene.TAG;


public class CitiesRecyclerViewAdapter extends RecyclerView.Adapter<CitiesRecyclerViewAdapter.ViewHolder> {

    private List<String> mValues;
    private LayoutInflater layoutInflater;
    private CallbackInterface mCallbackInterface;

    public CitiesRecyclerViewAdapter() {
    }

    public CitiesRecyclerViewAdapter(List<String> items, Context context, CallbackInterface callbackInterface) {
        mValues = items;
        layoutInflater = LayoutInflater.from(context);
        mCallbackInterface = callbackInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.city_list_item, parent, false);
        return new ViewHolder(view, mCallbackInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.d(TAG, "onBindViewHolder: position:" + position);

        String cityString = mValues.get(position);

        holder.mContentView.setText(cityString);

        holder.mContentView.setOnClickListener(v -> {
            try {
                mCallbackInterface.onItemClick(cityString);
            } catch (WeatherProviderInstantiationException e) {
                e.printStackTrace();
            }
        });
        holder.mContentView.setOnLongClickListener(v -> {
            if (mValues.size() >= 2) {
                mValues.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mValues.size());
                mCallbackInterface.onItemLongClick(mValues.size());
            } else {
                mCallbackInterface.onItemLongClick(0);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (mValues.size() == 0) {
            return 1;
        } else {
            return mValues.size();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final Button mContentView;

        ViewHolder(View view, CallbackInterface callbackInterface) {
            super(view);
            mContentView = view.findViewById(R.id.content);
            mCallbackInterface = callbackInterface;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

    }
}
