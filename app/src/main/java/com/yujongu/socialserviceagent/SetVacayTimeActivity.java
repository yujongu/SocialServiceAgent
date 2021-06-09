package com.yujongu.socialserviceagent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class SetVacayTimeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView paidDateTextS, paidDateTextE, paidTimeTextS, paidTimeTextE,
            rewardDateTextS, rewardDateTextE, rewardTimeTextS, rewardTimeTextE,
            specialDateTextS, specialDateTextE, specialTimeTextS, specialTimeTextE,
            sickDateTextS, sickDateTextE, sickTimeTextS, sickTimeTextE;;
    Button paidDateBtnS, paidDateBtnE, paidTimeBtnS,paidTimeBtnE,
            rewardDateBtnS, rewardDateBtnE, rewardTimeBtnS, rewardTimeBtnE,
            specialDateBtnS, specialDateBtnE, specialTimeBtnS, specialTimeBtnE,
            sickDateBtnS, sickDateBtnE, sickTimeBtnS, sickTimeBtnE;
    TableRow paidRowS, paidRowE, rewardRowS, rewardRowE, specialRowS, specialRowE, sickRowS, sickRowE;
    private CheckBox paidCheck, rewardCheck, specialCheck, sickCheck;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button confirm_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_vacay_time);

        paidDateTextS = findViewById(R.id.paidDateTextS);
        paidDateTextE = findViewById(R.id.paidDateTextE);
        paidTimeTextS = findViewById(R.id.paidTimeTextS);
        paidTimeTextE = findViewById(R.id.paidTimeTextE);
        paidDateBtnS = findViewById(R.id.paidDateBtnS);
        paidDateBtnE = findViewById(R.id.paidDateBtnE);
        paidTimeBtnS = findViewById(R.id.paidTimeBtnS);
        paidTimeBtnE = findViewById(R.id.paidTimeBtnE);
        rewardDateTextS = findViewById(R.id.rewardDateTextS);
        rewardDateTextE = findViewById(R.id.rewardDateTextE);
        rewardTimeTextS = findViewById(R.id.rewardTimeTextS);
        rewardTimeTextE = findViewById(R.id.rewardTimeTextE);
        rewardDateBtnS = findViewById(R.id.rewardDateBtnS);
        rewardDateBtnE = findViewById(R.id.rewardDateBtnE);
        rewardTimeBtnS = findViewById(R.id.rewardTimeBtnS);
        rewardTimeBtnE = findViewById(R.id.rewardTimeBtnE);
        specialDateTextS = findViewById(R.id.specialDateTextS);
        specialDateTextE = findViewById(R.id.specialDateTextE);
        specialTimeTextS = findViewById(R.id.specialTimeTextS);
        specialTimeTextE = findViewById(R.id.specialTimeTextE);
        specialDateBtnS = findViewById(R.id.specialDateBtnS);
        specialDateBtnE = findViewById(R.id.specialDateBtnE);
        specialTimeBtnS = findViewById(R.id.specialTimeBtnS);
        specialTimeBtnE = findViewById(R.id.specialTimeBtnE);
        sickDateTextS = findViewById(R.id.sickDateTextS);
        sickDateTextE = findViewById(R.id.sickDateTextE);
        sickTimeTextS = findViewById(R.id.sickTimeTextS);
        sickTimeTextE = findViewById(R.id.sickTimeTextE);
        sickDateBtnS = findViewById(R.id.sickDateBtnS);
        sickDateBtnE = findViewById(R.id.sickDateBtnE);
        sickTimeBtnS = findViewById(R.id.sickTimeBtnS);
        sickTimeBtnE = findViewById(R.id.sickTimeBtnE);

        paidDateBtnS.setOnClickListener(this);
        paidDateBtnE.setOnClickListener(this);
        paidTimeBtnS.setOnClickListener(this);
        paidTimeBtnE.setOnClickListener(this);
        rewardDateBtnS.setOnClickListener(this);
        rewardDateBtnE.setOnClickListener(this);
        rewardTimeBtnS.setOnClickListener(this);
        rewardTimeBtnE.setOnClickListener(this);
        specialDateBtnS.setOnClickListener(this);
        specialDateBtnE.setOnClickListener(this);
        specialTimeBtnS.setOnClickListener(this);
        specialTimeBtnE.setOnClickListener(this);
        sickDateBtnS.setOnClickListener(this);
        sickDateBtnE.setOnClickListener(this);
        sickTimeBtnS.setOnClickListener(this);
        sickTimeBtnE.setOnClickListener(this);

        paidCheck = findViewById(R.id.checkbox_paid);
        rewardCheck = findViewById(R.id.checkbox_reward);
        specialCheck = findViewById(R.id.checkbox_special);
        sickCheck = findViewById(R.id.checkbox_sick);

        paidRowS = findViewById(R.id.paidRowS);
        paidRowE = findViewById(R.id.paidRowE);
        rewardRowS = findViewById(R.id.rewardRowS);
        rewardRowE = findViewById(R.id.rewardRowE);
        specialRowS = findViewById(R.id.specialRowS);
        specialRowE = findViewById(R.id.specialRowE);
        sickRowS = findViewById(R.id.sickRowS);
        sickRowE = findViewById(R.id.sickRowE);

        confirm_Btn = findViewById(R.id.confirmButton);

        paidCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    paidRowS.setVisibility(View.VISIBLE);
                    paidRowE.setVisibility(View.VISIBLE);
                }else{
                    paidRowS.setVisibility(View.GONE);
                    paidRowE.setVisibility(View.GONE);
                }
            }
        });

        rewardCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rewardRowS.setVisibility(View.VISIBLE);
                    rewardRowE.setVisibility(View.VISIBLE);
                }else{
                    rewardRowS.setVisibility(View.GONE);
                    rewardRowE.setVisibility(View.GONE);
                }
            }
        });

        specialCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    specialRowS.setVisibility(View.VISIBLE);
                    specialRowE.setVisibility(View.VISIBLE);
                }else{
                    specialRowS.setVisibility(View.GONE);
                    specialRowE.setVisibility(View.GONE);
                }
            }
        });

        sickCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sickRowS.setVisibility(View.VISIBLE);
                    sickRowE.setVisibility(View.VISIBLE);
                }else{
                    sickRowS.setVisibility(View.GONE);
                    sickRowE.setVisibility(View.GONE);
                }
            }
        });

        confirm_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetVacayTimeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == paidDateBtnS) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            paidDateTextS.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == paidDateBtnE) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            paidDateTextE.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == paidTimeBtnS) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            paidTimeTextS.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        if (v == paidTimeBtnE) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            paidTimeTextE.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }

        if (v == rewardDateBtnS) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            rewardDateTextS.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == rewardDateBtnE) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            rewardDateTextE.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == rewardTimeBtnS) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            rewardTimeTextS.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        if (v == rewardTimeBtnE) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            rewardTimeTextE.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }

        if (v == specialDateBtnS) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            specialDateTextS.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == specialDateBtnE) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            specialDateTextE.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == specialTimeBtnS) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            specialTimeTextS.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        if (v == specialTimeBtnE) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            specialTimeTextE.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }

        if (v == sickDateBtnS) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            sickDateTextS.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == sickDateBtnE) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            sickDateTextE.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == sickTimeBtnS) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            sickTimeTextS.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }
        if (v == sickTimeBtnE) {
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            sickTimeTextE.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, true);
            timePickerDialog.show();
        }


    }
}