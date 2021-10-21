package com.schoolhw.list_view.subject;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.RequiresApi;
import com.schoolhw.Files;
import com.schoolhw.MainActivity;
import com.schoolhw.R;
import com.schoolhw.json.JSONObject;
import com.schoolhw.json.JSONWriter;
import com.schoolhw.list_view.homework.HomeWork;
import org.w3c.dom.Text;

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
            this.deletingPopup(i, deleteButtonV);
        });

        return v;
    }

    private void deletingPopup(int index, View view) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.confirm_deleting_popup, null);

        final PopupWindow popupWindow[] = new PopupWindow[1];

        ((TextView)popupView.findViewById(R.id.confirm_deleting_tv)).setText("Deleting " + MainActivity.subjects.get(index).getSubjectName());
        popupView.findViewById(R.id.cancel_deleting_button).setOnClickListener( v -> popupWindow[0].dismiss());
        popupView.findViewById(R.id.confirm_deleting_button).setOnClickListener(v -> {
            this.tryRemoveSubject(index);
            popupWindow[0].dismiss();
        });

        popupWindow[0] = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow[0].showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void tryRemoveSubject(int index) {

        for(HomeWork hw : MainActivity.homeworks){
            if(hw.getSubject().equals(MainActivity.subjects.get(index))){

                Toast.makeText(this.context, "You cannot delete " + hw.getSubject().getSubjectName() + ": homework at " + hw.getDate(), Toast.LENGTH_LONG).show();

                return;
            }
        }

        String subjectName = MainActivity.subjects.get(index).getSubjectName();

        MainActivity.subjects.remove(index);
        this.saveSubjects();
        this.notifyDataSetChanged();

        Toast.makeText(this.context, subjectName + " has been deleted", Toast.LENGTH_SHORT).show();
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
