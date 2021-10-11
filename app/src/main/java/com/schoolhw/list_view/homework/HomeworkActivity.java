package com.schoolhw.list_view.homework;

import android.app.DatePickerDialog;
import android.os.Build;
import android.view.View;
import android.widget.*;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.schoolhw.MainActivity;
import com.schoolhw.R;
import com.schoolhw.list_view.days.Days;
import com.schoolhw.list_view.subject.Subject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;


public class HomeworkActivity extends AppCompatActivity {

    private EditText homework;
    private ListView subjects;
    private DatePicker picker;
    private ImageButton addHWButton;
    private int year, month, day;

    private Subject subjectSelected;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        this.loadComponents();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadComponents() {
        this.homework = findViewById(R.id.homework_note_adding);

        this.subjects = findViewById(R.id.list_subject_picker);
        this.picker = findViewById(R.id.day_homework_picker);
        this.picker.setMinDate(System.currentTimeMillis() - 1000);

        Subject[] subjects = MainActivity.getSubjectsArray();
        SubjectListAdapter subjectListAdapter = new SubjectListAdapter(this.getApplicationContext(), subjects);
        this.subjects.setAdapter(subjectListAdapter);
        this.subjects.setOnItemClickListener((adapterView, view, i, l) -> {
            this.subjectSelected = (Subject) this.subjects.getItemAtPosition(i);
            this.setRightNextDay(LocalDate.now());
        });

        this.picker.setOnDateChangedListener((datePicker, year, month, day) -> {

            if(this.year == year && this.month == month && this.day == day)
                return;

            LocalDate localDate = LocalDate.of(year, month+1, day);
            int dayOfWeek = localDate.getDayOfWeek().getValue();

            /*
                Check if in the day selected the student has the subject selected
                Else date will be set as the first right one next

                e.g.

                Today is Monday
                Math -> Mon, Thu, Fri
                Client select saturday -> Monday will be selected
                Client select Tuesday -> Thursday will be selected
             */
            if(this.subjectSelected != null) {
                if (this.validDay(dayOfWeek)) {
                    this.updateBufferDate(year, month, day);
                } else {
                    this.setRightNextDay(localDate);
                }
            }
        });

        this.addHWButton = findViewById(R.id.add_homework_button);
        this.addHWButton.setOnClickListener( v -> {
            if(this.subjectSelected != null)
                saveHomework(v, subjects);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveHomework(View v, Subject[] subjects) {

    }

    /**
     * Set the date picker as the right next date available for the given subject
     * @param selectedDay
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setRightNextDay(LocalDate selectedDay){

        selectedDay = selectedDay.plusDays(this.daysToJump(selectedDay));

        this.updateBufferDate(selectedDay.getYear(), selectedDay.getMonth().getValue()-1, selectedDay.getDayOfMonth());
    }

    private boolean validDay(int dayOfWeek){
        for(Days days : this.subjectSelected.getDays()){
            if(days.getIndex() == dayOfWeek){
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int daysToJump(LocalDate selectedDate){

        int tmp = selectedDate.getDayOfWeek().getValue();

        for(int i = 1; i <= 7; i++){

            if(++tmp > 7)
                tmp = 1;

            for(Days day : this.subjectSelected.getDays()){
                if(day.getIndex() == tmp)
                    return i;
            }
        }

        throw new RuntimeException("Could not get next day available...");
    }

    private void updateBufferDate(int year, int month, int day){
        this.year = year;
        this.month = month;
        this.day = day;

        this.picker.updateDate(year, month, day);
    }
}