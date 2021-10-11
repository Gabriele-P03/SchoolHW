package com.schoolhw.list_view.days;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.annotation.LayoutRes;
import com.schoolhw.R;

public class DaysListAdapter extends BaseAdapter implements ListAdapter {

    private Days[] days;

    public DaysListAdapter(Days[] days) {
        this.days = days;
    }

    @Override
    public int getCount() {
        return this.days.length;
    }

    @Override
    public Object getItem(int i) {
        return this.days[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if( v == null){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_text_view, viewGroup, false);
        }

        ((TextView) v.findViewById(R.id.item_text_view)).setText(this.days[i].name);

        return v;
    }
}
