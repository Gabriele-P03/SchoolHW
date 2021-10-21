package com.schoolhw.list_view.homework;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import com.schoolhw.MainActivity;
import com.schoolhw.R;
import org.w3c.dom.Text;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class HomeWorkListAdapter extends BaseAdapter implements ListAdapter {

    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public HomeWorkListAdapter(Context context) {
        this.context = context;
        this.order();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void order() {
        if(MainActivity.homeworks != null) {
            if (MainActivity.subjects.size() > 0) {
                Collections.sort(MainActivity.homeworks, Comparator.comparing(HomeWork::getDate));
            }
        }
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = view;

        if(v == null){
            v = LayoutInflater.from(this.context).inflate(R.layout.list_homework, null);
        }

        if(v != null){

            HomeWork hw = MainActivity.homeworks.get(i);

            //Setting Date Text View
            if(i == 0 || this.isPreviousHomeworkDifferentDate(i)){
                LocalDate localDate = LocalDate.parse(hw.getDate().toString()).plusMonths(1);
                String dateAsString = hw.getDate().toString() + " - " + localDate.getDayOfWeek().toString();
                TextView date_hw = v.findViewById(R.id.date_homework);
                date_hw.setHeight((int) date_hw.getTextSize()+5);
                ((TextView)v.findViewById(R.id.date_homework)).setText(dateAsString);
            }else{
                ((TextView)v.findViewById(R.id.date_homework)).setHeight(0);
            }

            int dividerHeight = 0;

            if(this.isNextHomeworkDifferentDate(i)){
                dividerHeight = 5;
            }

            v.findViewById(R.id.divider_homework).getLayoutParams().height = dividerHeight;

            //Setting Subject Name and Homework Note
            ((TextView)v.findViewById(R.id.homework_note)).setText(hw.getNote());
            ((TextView)v.findViewById(R.id.homework_subject_name)).setText(hw.getSubject().getSubjectName());

            //Setting background drawable
            Drawable d = this.context.getDrawable(R.drawable.left_bordered_rect);
            d.setColorFilter(hw.getSubject().getColor(), PorterDuff.Mode.SRC_ATOP);
            v.findViewById(R.id.border_rect_hw).setBackground(d);

            //Setting delete button
            v.findViewById(R.id.delete_homework_button).setOnClickListener(v1 -> this.deletingPopup(i, v1));

            v.setOnClickListener(v1 -> copyOnClipboard(i));
        }

        return v;
    }

    private void copyOnClipboard(int i) {

        ClipboardManager cm = (ClipboardManager) this.context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("Homework copied", MainActivity.homeworks.get(i).toString()));
        Toast.makeText(this.context, "Homework copied", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deletingPopup(int index, View view) {

        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.confirm_deleting_popup, null);

        final PopupWindow popupWindow[] = new PopupWindow[1];

        HomeWork hw = MainActivity.homeworks.get(index);

        ((TextView)popupView.findViewById(R.id.confirm_deleting_tv)).setText("Deleting:\n " + hw.getDate() + " - " + hw.getSubject().getSubjectName() + "\n" + hw.getNote());
        popupView.findViewById(R.id.cancel_deleting_button).setOnClickListener( v -> popupWindow[0].dismiss());
        popupView.findViewById(R.id.confirm_deleting_button).setOnClickListener(v -> {
            this.deleteHW(index);
            popupWindow[0].dismiss();
        });

        popupWindow[0] = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow[0].showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    /**
     * @param i
     * @return if the previous (i - 1) homework in {@link MainActivity#homeworks} has a different date as the @i one.
     * If the list is over, return false
     */
    private boolean isPreviousHomeworkDifferentDate(int i) {

        if(i > 0 && i < MainActivity.homeworks.size()){
            return !MainActivity.homeworks.get(i-1).getDate().toString().equals(MainActivity.homeworks.get(i).getDate().toString());
        }

        return false;
    }

    /**
     * @param i
     * @return if the next (i + 1) homework in {@link MainActivity#homeworks} has a different date as the @i one.
     * If the list is over, return false
     */
    private boolean isNextHomeworkDifferentDate(int i) {

        if(i < MainActivity.homeworks.size()-1){
            return !MainActivity.homeworks.get(i+1).getDate().toString().equals(MainActivity.homeworks.get(i).getDate().toString());
        }

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void deleteHW(int i) {

        String fileName =
                MainActivity.homeworks.get(i).getSubject().getSubjectName() +
                "/" +
                 MainActivity.homeworks.get(i).getDate().toString() +
                 ".txt";

        File file = new File(this.context.getFilesDir(), fileName);
        if(file.exists())
            file.delete();
        else
            throw new RuntimeException("Could not find file: " + fileName);

        MainActivity.homeworks.remove(i);
        this.order();
        this.notifyDataSetChanged();
    }
}
