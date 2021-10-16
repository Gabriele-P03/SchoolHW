package com.schoolhw;

import android.content.Intent;
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
import com.schoolhw.list_view.homework.HomeWork;
import com.schoolhw.list_view.subject.Subject;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Date;
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
            this.textView.setText("Loading Settings...");
            MainActivity.settings = new Settings(this.getApplicationContext());
            Thread.sleep(1000);
            this.textView.setText("Loading Subjects...");
            MainActivity.subjects = this.loadSubjects();
            Thread.sleep(1000);

            this.textView.setText("Loading Homework...");
            MainActivity.homeworks = this.loadHomework();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.finish();
    }

    private ArrayList<HomeWork> loadHomework() {
        ArrayList<HomeWork> homeWorks = new ArrayList<>();
        File folderFile = this.getApplicationContext().getFilesDir();

        if(folderFile.exists()){
            if(folderFile.isFile()){
                folderFile.delete();
                folderFile.mkdir();
            }
        }else{
            folderFile.mkdir();
        }

        for(Subject subject : MainActivity.subjects){
            File folderSubject = new File(folderFile, subject.getSubjectName());

            if(folderSubject.exists()){
                if(folderSubject.isFile()){
                    folderSubject.delete();
                    folderSubject.mkdir();
                }
            }else{
                folderSubject.mkdir();
            }

            for (File fileHomework : folderSubject.listFiles()){
                homeWorks.add(this.readHomeworkFile(subject, fileHomework));
            }
        }

        return homeWorks;
    }

    private HomeWork readHomeworkFile(Subject subject, File fileHomework){
        Date date = Date.valueOf(fileHomework.getName().replace(".txt", ""));
        String note = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileHomework));

            String tmp = "";
            while( (tmp = br.readLine()) != null ){
                note += tmp;
            }

            return new HomeWork(subject, date, note);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not read homework: " + fileHomework);
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

                this.textView.setText(this.textView.getText() + " " + subject.getName());
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
                if(days1.getIndex() == Integer.parseInt(daysJsonArray.getValues().get(i))){
                    days[i] = days1;
                    break;
                }
            }
        }
        return days;
    }

    @Override
    public void finish() {
        super.finish();
        this.startActivity(new Intent(this.getApplicationContext(), MainActivity.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
