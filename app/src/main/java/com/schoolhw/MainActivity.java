package com.schoolhw;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.schoolhw.list_view.homework.HomeWork;
import com.schoolhw.list_view.homework.HomeWorkListAdapter;
import com.schoolhw.list_view.homework.HomeworkActivity;
import com.schoolhw.list_view.subject.ListSubjectActivity;
import com.schoolhw.list_view.subject.Subject;
import com.schoolhw.list_view.subject.SubjectActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Settings settings;
    public static ArrayList<Subject> subjects;
    public static ArrayList<HomeWork> homeworks;

    private ListView hwListView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setting menu's buttons
        this.loadComponents();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadComponents(){
        ImageButton menu = findViewById(R.id.menuButton);
        menu.setOnClickListener(view -> {

            //Inflate view after have set buttons, will cause crash
            LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = layoutInflater.inflate(R.layout.menu_layout, null, false);

          //Add Homework Button
            popupView.findViewById(R.id.addHWButton).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), HomeworkActivity.class)));
            //Add Subject Button
            popupView.findViewById(R.id.addSubjectButton).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SubjectActivity.class)));
            //List Subject Button
            popupView.findViewById(R.id.listSubjectButton).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ListSubjectActivity.class)));
            //Settings Button
            popupView.findViewById(R.id.settingsButton).setOnClickListener(v -> startActivity(null));
            //Permissions Button
            popupView.findViewById(R.id.permissionButton).setOnClickListener(v -> startActivity(null));

            /*
             *   @offsetY is the offset height between the top side of the display
             *   and the divider
             *
             * @see activity_main.xml - divider
             */
            View viewDivider = findViewById(R.id.dividerMain);
            final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.showAsDropDown(viewDivider, 0, viewDivider.getHeight());
        });
        //Setting homework list adapter
        hwListView = this.findViewById(R.id.list_list_homework);
        hwListView.setAdapter(new HomeWorkListAdapter(this.getApplicationContext()));

    }

    public static Subject[] getSubjectsArray(){
        Subject[] subjects = new Subject[MainActivity.subjects.size()];

        for(int i = 0; i < subjects.length; i++){
            subjects[i] = MainActivity.subjects.get(i);
        }

        return subjects;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((HomeWorkListAdapter)this.hwListView.getAdapter()).notifyDataSetChanged();
    }
}