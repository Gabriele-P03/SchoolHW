package com.schoolhw.list_view.homework;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.schoolhw.MainActivity;
import com.schoolhw.R;
import com.schoolhw.list_view.subject.Subject;

class SubjectListAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private Subject[] subjects;

    public SubjectListAdapter(Context context, Subject[] subjects) {
        this.context = context;
        this.subjects = subjects;
    }

    @Override
    public int getCount() {
        return subjects.length;
    }

    @Override
    public Object getItem(int i) {
        return subjects[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;

        if(v == null){
            v = LayoutInflater.from(this.context).inflate(R.layout.list_text_view, null);
        }

        if(v != null){
            TextView textView = v.findViewById(R.id.item_text_view);
            textView.setText(this.subjects[i].getSubjectName());

            Drawable d = this.context.getDrawable(R.drawable.bordered_rect);
            d.setColorFilter(this.subjects[i].getColor(), PorterDuff.Mode.SRC_ATOP);
            textView.setBackground(d);

        }

        return v;
    }
}
