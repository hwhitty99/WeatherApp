package com.ebookfrenzy.weatherapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ebookfrenzy.weatherapp.MainActivity;

public class ImageListAdapter extends BaseAdapter {

    int[] Image;
    Context context;

    private static LayoutInflater layoutInflater;
    public ImageListAdapter(MainActivity mainActivity, int[] mainImages) {
        context = mainActivity;
        Image = mainImages;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
