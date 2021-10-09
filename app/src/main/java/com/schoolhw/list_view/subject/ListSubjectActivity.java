package com.schoolhw.list_view.subject;

import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.schoolhw.R;

public class ListSubjectActivity extends AppCompatActivity {

    private ListView subjectsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_subject);
        this.loadComponents();
    }

    private void loadComponents(){
        this.subjectsListView = findViewById(R.id.list_subject);
        this.subjectsListView.setAdapter(new SubjectListAdapter(this.getApplicationContext()));
    }
}