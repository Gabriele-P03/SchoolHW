package com.schoolhw;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.schoolhw.json.JSONArray;
import com.schoolhw.json.JSONObject;
import com.schoolhw.json.JSONReader;
import com.schoolhw.list_view.days.Days;
import com.schoolhw.list_view.subject.Subject;

import java.io.IOException;
import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    private TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);

        this.textView = this.findViewById(R.id.loadStuff);

        try {
            //Loading settings
            this.textView.setText("Loading settings...");
            MainActivity.settings = new Settings(this.getApplicationContext());
            MainActivity.subjects = this.loadSubjects();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private ArrayList<Subject> loadSubjects() {
        ArrayList<Subject> subjects = new ArrayList<>();

        try {
            JSONReader jsonReader = new JSONReader(Files.getFile(this.getApplicationContext(), "subjects.json"));
            JSONObject mainObj = jsonReader.getMainObject();

            for(JSONObject subject : mainObj.getObjects()){
                subjects.add(new Subject(
                        subject.getName(),
                        this.getDaysFromJsonArray(subject.getArrayByName("days")),
                        Integer.parseInt(subject.getMapByName("color").getValue())
                ));
            }

            jsonReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return subjects;
    }

    private Days[] getDaysFromJsonArray(JSONArray daysJsonArray) {

        Days[] days = new Days[daysJsonArray.getValues().size()];

        for(int i = 0; i < daysJsonArray.getValues().size(); i++){
            for(Days days1 : Days.values()){
                if(days1.getIndex() == i+1){
                    days[i] = days1;
                    break;
                }
            }
        }
        return days;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
