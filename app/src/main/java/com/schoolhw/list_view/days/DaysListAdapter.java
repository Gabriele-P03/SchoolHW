package com.schoolhw.list_view.days;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.schoolhw.R;

public class DaysListAdapter extends BaseAdapter implements ListAdapter {

    @Override
    public int getCount() {
        return Days.values().length;
    }

    @Override
    public Object getItem(int i) {
        return Days.values()[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if( v == null){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_subject, viewGroup, false);
        }

        ((TextView)v.findViewById(R.id.item_hours_days)).setText(Days.values()[i].name);

        return v;
    }
}
