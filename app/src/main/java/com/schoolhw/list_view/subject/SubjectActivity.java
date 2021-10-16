package com.schoolhw.list_view.subject;

import android.os.Build;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.schoolhw.Files;
import com.schoolhw.MainActivity;
import com.schoolhw.R;
import com.schoolhw.json.JSONObject;
import com.schoolhw.json.JSONReader;
import com.schoolhw.json.JSONWriter;
import com.schoolhw.list_view.days.Days;

import java.io.File;
import java.io.IOException;

public class SubjectActivity extends AppCompatActivity {

    private ListView days;
    private EditText subjectName;
    private ProgressBar red, green, blue;
    private ImageButton addButton;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        this.loadComponents();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadComponents(){

        this.subjectName = findViewById(R.id.addSubjectName);

        this.days = findViewById(R.id.daysSelector);
        this.days.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        this.days.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, Days.values()));

        this.addButton = findViewById(R.id.saveNewSubjectButton);

        this.red = findViewById(R.id.red_bar);
        this.red.setOnTouchListener((view, motionEvent) -> {
            int value = (int) (motionEvent.getX() / view.getWidth() * 100);
            red.setProgress(value);
            return setSaveButtonBackgroundColor();
        });

        this.green = findViewById(R.id.green_bar);
        this.green.setOnTouchListener((view, motionEvent) -> {
            int value = (int) (motionEvent.getX() / view.getWidth() * 100);
            green.setProgress(value);
            return setSaveButtonBackgroundColor();
        });

        this.blue = findViewById(R.id.blue_bar);
        this.blue.setOnTouchListener((view, motionEvent) -> {
            int value = (int) (motionEvent.getX() / view.getWidth() * 100);
            blue.setProgress(value);
            return setSaveButtonBackgroundColor();
        });

        this.addButton.setOnClickListener(v -> this.registerNewSubject());
    }

    private boolean setSaveButtonBackgroundColor(){
        int argb =  this.getColor();
        this.addButton.setBackgroundColor(argb);
        return true;
    }

    private int getColor(){
        return ((0xff) << 24) |
                ( (this.red.getProgress() &0xff) << 16) |
                ( (this.green.getProgress() & 0xff) << 8) |
                (this.blue.getProgress() & 0xff);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void registerNewSubject(){

        String subjectName = this.subjectName.getText().toString();
        if(subjectName != null){
            if(this.days.getCheckedItemPositions().size() > 0){
                    Subject subject = new Subject(
                            subjectName,
                            this.getDaysSelected(),
                            this.getColor()
                    );

                    JSONObject subjectObj = subject.toJSONObject();

                    try {
                        JSONReader jsonReader = new JSONReader(Files.getFile(this.getApplicationContext(), "subjects.json"));
                        JSONObject mainObj = jsonReader.getMainObject();
                        mainObj.getObjects().add(subjectObj);

                        JSONWriter jsonWriter = new JSONWriter(Files.getFile(this.getApplicationContext(), "subjects.json"));
                        jsonWriter.write(mainObj);

                        jsonReader.close();
                        jsonWriter.close();

                        MainActivity.subjects.add(subject);

                        File folderSubject = new File(this.getApplicationContext().getFilesDir(), subjectName);

                        if(!folderSubject.exists()){
                            folderSubject.mkdir();
                        }else{
                            if(folderSubject.isFile()){

                                Toast.makeText(this.getApplicationContext(), subjectName
                                        + " should be a folder, but it is a file.\n I am gonna delete it and create a " +
                                        "folder", Toast.LENGTH_LONG).show();

                                folderSubject.delete();
                                folderSubject.mkdir();
                            }
                        }

                        Toast.makeText(getApplicationContext(), "Subject registered successfully", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    private Days[] getDaysSelected() {
        Days[] days = new Days[this.days.getCheckedItemCount()];

        int index = 0;

        for(int i = 0; i < this.days.getCount(); i++){

            if(this.days.getCheckedItemPositions().get(i)){
                days[index++] = Days.values()[i];
            }
        }

        return days;
    }
}