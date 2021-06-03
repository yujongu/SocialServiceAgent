package com.yujongu.socialserviceagent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.yujongu.socialserviceagent.ProfileActivity.SP_PIMAGE;
import static com.yujongu.socialserviceagent.ProfileActivity.SP_PNAME;

public class DayActivity extends AppCompatActivity {

    TextView dateTv;
    TextView leavedTv;
    TextView nameTv;

    private SharedPreference sharedPreference;
    private Context context;
    public ProfileInfo me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        initInstances();
        eventListeners();
        getIntents();

    }

    private void initInstances() {
        dateTv = findViewById(R.id.tvDate);
        leavedTv = findViewById(R.id.tvLeaved);
        nameTv = findViewById(R.id.tvName);

        sharedPreference = new SharedPreference();
        context = this;
        me = new ProfileInfo(
                sharedPreference.loadStringData(context, SP_PNAME),
                sharedPreference.loadStringData(context, SP_PIMAGE)
        );
        nameTv.setText(me.getName());
    }

    private void eventListeners(){

    }

    private void getIntents(){
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");
        String vacay = intent.getStringExtra("Vacay");
        dateTv.setText(date);
        leavedTv.setText(vacay);
    }

}
