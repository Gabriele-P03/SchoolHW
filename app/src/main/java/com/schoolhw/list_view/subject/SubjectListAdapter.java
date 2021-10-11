package com.schoolhw.list_view.subject;

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
import com.schoolhw.Files;
import com.schoolhw.MainActivity;
import com.schoolhw.R;
import com.schoolhw.json.JSONObject;
import com.schoolhw.json.JSONWriter;

import java.io.IOException;

class SubjectListAdapter extends BaseAdapter implements ListAdapter {

    private Context context;

    public SubjectListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return MainActivity.subjects.size();
    }

    @Override
    public Object getItem(int i) {
        return MainActivity.subjects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if( v == null){
            v = LayoutInflater.from(context).inflate(R.layout.list_subjects, viewGroup, false);
        }

        Drawable d = this.context.getDrawable(R.drawable.bordered_rect);
        d.setColorFilter(MainActivity.subjects.get(i).getColor(), PorterDuff.Mode.SRC_ATOP);
        v.findViewById(R.id.subjectRect).setBackground(d);

        ((TextView)v.findViewById(R.id.subjectName)).setText(MainActivity.subjects.get(i).getSubjectName());
        ((TextView)v.findViewById(R.id.daysSubjectList)).setText(MainActivity.subjects.get(i).daysAsString());
        v.findViewById(R.id.deleteSubjectButton).setOnClickListener(deleteButtonV -> {
            MainActivity.subjects.remove(i);
            this.saveSubjects();
            this.notifyDataSetChanged();
        });

        return v;
    }

    private void saveSubjects(){

        JSONObject object = new JSONObject();

        for(Subject subject : MainActivity.subjects){
            object.getObjects().add(subject.toJSONObject());
        }

        try {
            JSONWriter writer = new JSONWriter(Files.getFile(this.context, "subjects.json"));
            writer.write(object);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
