package com.yujongu.socialserviceagent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    static final int PAID = 0;
    static final int SICK = 1;
    static final int PERSONAL = 2;

    private ImageButton profileBtn;

    private ImageButton prevMonthBtn;
    private TextView currMonthTv;
    private ImageButton nextMonthBtn;

    private ImageButton findTodayBtn;

    private RecyclerView mRecyclerView;


    private Calendar calendar;
    private CalendarAdapter mCalendarAdapter;
    private ArrayList<Day_Event> mCalendarData;

    private ImageButton addEventBtn;

    private ImageButton lBtn;
    private ImageButton cBtn;
    private ImageButton uBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();
        eventListeners();

    }

    private void initInstances(){
        prevMonthBtn = findViewById(R.id.btnPrevMonth);
        nextMonthBtn = findViewById(R.id.btnNextMonth);
        currMonthTv = findViewById(R.id.tvCurrMonth);

        findTodayBtn = findViewById(R.id.btnFindToday);

        profileBtn = findViewById(R.id.btnProfile);

        mRecyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        addEventBtn = findViewById(R.id.btnAddEvent);

        calendar = new GregorianCalendar();
        int today = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, 1);
        setMCalendarData(calendar);

        for (int i = 0; i < mCalendarData.size(); i++){
            if (mCalendarData.get(i).getDate() == today){
                mCalendarData.get(i).setClicked(true);
            }
        }
        mCalendarAdapter = new CalendarAdapter(this, mCalendarData);
        mRecyclerView.setAdapter(mCalendarAdapter);

        lBtn = findViewById(R.id.btnL);
        cBtn = findViewById(R.id.btnC);
        uBtn = findViewById(R.id.btnU);

    }

    private void eventListeners(){
        profileBtn.setOnClickListener(listener);
        prevMonthBtn.setOnClickListener(listener);
        nextMonthBtn.setOnClickListener(listener);
        findTodayBtn.setOnClickListener(listener);
        addEventBtn.setOnClickListener(listener);
        lBtn.setOnClickListener(listener);
        cBtn.setOnClickListener(listener);
        uBtn.setOnClickListener(listener);

        mRecyclerView.setHasFixedSize(true);

    }

    private void setMCalendarData(Calendar calendar){
        currMonthTv.setText(String.format(getString(R.string.yearMonthText), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1));
        mCalendarData = new ArrayList<>();
        //6 rows 7 cols
        for (int i = 0; i < 42; i++){
            Day_Event day;
            if (
                    (i < calendar.get(Calendar.DAY_OF_WEEK) - 1) ||
                    (i > calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + calendar.get(Calendar.DAY_OF_WEEK) - 2)
            ){
                day = new Day_Event(1);
                mCalendarData.add(day);
                continue;
            }
            day = new Day_Event(0);
            day.setMonth(calendar.get(Calendar.MONTH) + 1);
            day.setDate(i - (calendar.get(Calendar.DAY_OF_WEEK) - 1) + 1);
            mCalendarData.add(day);
        }

        Calendar todayCal = new GregorianCalendar();
        if (calendar.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH)){
            for (int i = 0; i < mCalendarData.size(); i++){
                if (mCalendarData.get(i).getDate() == todayCal.get(Calendar.DATE)){
                    mCalendarData.get(i).setClicked(true);
                }
            }
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnProfile:
                    profileBtn.setEnabled(false);
                    redirectProfileActivity();
                    break;

                case R.id.btnPrevMonth:
                    calendar.add(Calendar.MONTH, -1);
                    setMCalendarData(calendar);

                    boolean checkClicked = false;
                    for (int i = 0; i < mCalendarData.size(); i++){
                        if (mCalendarData.get(i).isClicked()){
                            checkClicked = true;
                        }
                    }
                    if (!checkClicked){
                        for (int i = 0; i < mCalendarData.size(); i++){
                            if (mCalendarData.get(i).getDate() == 1){
                                mCalendarData.get(i).setClicked(true);
                                break;
                            }
                        }
                    }


                    mCalendarAdapter.setCalendarList(mCalendarData);
                    break;

                case R.id.btnNextMonth:
                    calendar.add(Calendar.MONTH, 1);
                    setMCalendarData(calendar);

                    checkClicked = false;
                    for (int i = 0; i < mCalendarData.size(); i++){
                        if (mCalendarData.get(i).isClicked()){
                            checkClicked = true;
                        }
                    }
                    if (!checkClicked){
                        for (int i = 0; i < mCalendarData.size(); i++){
                            if (mCalendarData.get(i).getDate() == 1){
                                mCalendarData.get(i).setClicked(true);
                                break;
                            }
                        }
                    }

                    mCalendarAdapter.setCalendarList(mCalendarData);
                    break;

                case R.id.btnFindToday:
                    calendar = new GregorianCalendar();
                    int date = calendar.get(Calendar.DATE);
                    calendar.set(Calendar.DATE, 1);
                    setMCalendarData(calendar);
                    for (int i = 0; i < mCalendarData.size(); i++){
                        if (mCalendarData.get(i).getDate() == date){
                            mCalendarData.get(i).setClicked(true);
                        }
                    }
                    mCalendarAdapter.setCalendarList(mCalendarData);
                    break;

                case R.id.btnAddEvent:
                    triggerBtnVisible();
                    break;

                case R.id.btnL:
                    showPopupWindow(view, PERSONAL);
                    triggerBtnVisible();
                    break;

                case R.id.btnC:
                    showPopupWindow(view, SICK);
                    triggerBtnVisible();
                    break;

                case R.id.btnU:
                    showPopupWindow(view, PAID);
                    triggerBtnVisible();
                    break;
            }
        }
    };

    private Date convertToDate(String text){

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("k:mm");
        try {
            date = format.parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private void showPopupWindow(final View view, final int type){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window_leave, null);

        TextView popupDateTv = popupView.findViewById(R.id.popupTvDate);
        TextView popupLeaveTypeTv = popupView.findViewById(R.id.popupTvLeaveType);
        final Spinner popupStartTimeSpinner = popupView.findViewById(R.id.popupSpinStartTime);
        final Spinner popupEndTimeSpinner = popupView.findViewById(R.id.popupSpinEndTime);
        Button popupSaveEventBtn = popupView.findViewById(R.id.popupBtnSaveEvent);
        Button popupCancelEventBtn = popupView.findViewById(R.id.popupBtnCancelEvent);

        AdapterView.OnItemSelectedListener avSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (adapterView.getId()){
                    case R.id.popupSpinStartTime:
                        if (convertToDate(popupStartTimeSpinner.getItemAtPosition(i).toString()).after
                                (convertToDate(popupEndTimeSpinner.getSelectedItem().toString()))){
                            popupEndTimeSpinner.setSelection(i);
                        }
                        break;

                    case R.id.popupSpinEndTime:
                        if (convertToDate(popupStartTimeSpinner.getSelectedItem().toString()).after
                                (convertToDate(popupEndTimeSpinner.getItemAtPosition(i).toString()))){
                            popupStartTimeSpinner.setSelection(i);
                        }
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        popupStartTimeSpinner.setOnItemSelectedListener(avSelectedListener);
        popupEndTimeSpinner.setOnItemSelectedListener(avSelectedListener);


        for (int i = 0; i < mCalendarData.size(); i++){
            if (mCalendarData.get(i).isClicked()){
                popupDateTv.setText(
                        String.format(getString(R.string.monthDateText), mCalendarData.get(i).getMonth(), mCalendarData.get(i).getDate()));
            }
        }

        switch (type){
            case PAID:
                popupLeaveTypeTv.setText("연가 일정");
                break;

            case SICK:
                popupLeaveTypeTv.setText("병가 일정");
                break;
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupSaveEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo(type, popupStartTimeSpinner.getSelectedItem().toString(), popupEndTimeSpinner.getSelectedItem().toString());
                popupWindow.dismiss();
            }
        });

        popupCancelEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
    }

    private void saveInfo(int eventType, String startTime, String endTime){
        if (startTime.equals(endTime)){
            Toast.makeText(this, "시작 시간과 끝나는 시간이 같습니다.\n저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
            Log.i("Canceled", "Did not save");
        } else {
            Log.i("Type", String.valueOf(eventType));
            Log.i("Start time", startTime);
            Log.i("End time", endTime);
        }
    }

    private void triggerBtnVisible(){
        if (uBtn.getVisibility() != View.VISIBLE){
            uBtn.setVisibility(View.VISIBLE);
            cBtn.setVisibility(View.VISIBLE);
            lBtn.setVisibility(View.VISIBLE);
        } else {
            uBtn.setVisibility(View.GONE);
            cBtn.setVisibility(View.GONE);
            lBtn.setVisibility(View.GONE);
        }
    }

    private void redirectProfileActivity(){
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
