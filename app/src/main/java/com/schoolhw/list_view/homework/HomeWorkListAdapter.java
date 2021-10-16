package com.schoolhw.list_view.homework;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.RequiresApi;
import com.schoolhw.MainActivity;
import com.schoolhw.R;

import java.io.File;

public class HomeWorkListAdapter extends BaseAdapter implements ListAdapter {

    private Context context;

    public HomeWorkListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return MainActivity.homeworks.size();
    }

    @Override
    public Object getItem(int i) {
        return MainActivity.homeworks.get(i);
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
            v = LayoutInflater.from(this.context).inflate(R.layout.list_homeworks, null);
        }

        if(v != null){

            HomeWork hw = MainActivity.homeworks.get(i);

            ((TextView)v.findViewById(R.id.homework_subject_name)).setText(hw.getSubject().getSubjectName());

            ((TextView)v.findViewById(R.id.homework_note)).setText(hw.getNote());

            v.findViewById(R.id.delete_homework_button).setOnClickListener(view1 -> this.deleteHW(hw));

            Drawable d = this.context.getDrawable(R.drawable.left_bordered_rect);
            d.setColorFilter(hw.getSubject().getColor(), PorterDuff.Mode.SRC_ATOP);
            v.findViewById(R.id.border_rect_hw).setBackground(d);
        }

        return v;
    }

    private void deleteHW(HomeWork hw) {

        MainActivity.homeworks.remove(hw);
        String fileName = hw.getSubject().getSubjectName() + "/" + hw.getDate().toString() + ".txt";
        File file = new File(this.context.getFilesDir(), fileName);
        if(file.exists())
            file.delete();
        else
            throw new RuntimeException("Could not find file: " + fileName);
        this.notifyDataSetChanged();
    }

}
