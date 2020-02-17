package com.yujongu.socialserviceagent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DayActivity extends AppCompatActivity {

    TextView dateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        initInstances();
        eventListeners();
        getIntents();

    }

    private void initInstances(){
        dateTv = findViewById(R.id.tvDate);
    }

    private void eventListeners(){

    }

    private void getIntents(){
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        dateTv.setText(date);
    }

}
