package com.yujongu.socialserviceagent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TableRow;
import android.widget.TextView;

public class SetVacayActivity extends Activity {

    private TextView startPaidDisplay, endPaidDisplay, startRewardDisplay, endRewardDisplay,
            startSpecialDisplay, endSpecialDisplay, startSickDisplay,  endSickDisplay;
    private Button startPaidDate_Btn, endPaidDate_Btn,  startRewardDate_Btn,  endRewardDate_Btn,
            startSpecialDate_Btn, endSpecialDate_Btn, startSickDate_Btn, endSickDate_Btn;
    private Calendar startPaidDate, endPaidDate, startRewardDate, endRewardDate,
            startSpecialDate, endSpecialDate, startSickDate, endSickDate;
    private CheckBox paidCheck, rewardCheck, specialCheck, sickCheck;
    private TableRow paidRow, rewardRow, specialRow, sickRow;
    private Button timePick_Btn;

    static final int DATE_DIALOG_ID = 0;

    private TextView activeDateDisplay;
    private Calendar activeDate;

    @SuppressLint("CutPasteId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_vacay);

        startPaidDisplay = findViewById(R.id.paidtextS_id);
        startPaidDate_Btn = findViewById(R.id.paidbuttonS_id);
        startRewardDisplay = findViewById(R.id.rewardtextS_id);
        startRewardDate_Btn = findViewById(R.id.rewardbuttonS_id);
        startSpecialDisplay = findViewById(R.id.specialtextS_id);
        startSpecialDate_Btn = findViewById(R.id.specialbuttonS_id);
        startSickDisplay = findViewById(R.id.sicktextS_id);
        startSickDate_Btn = findViewById(R.id.sickbuttonS_id);

        startPaidDate = Calendar.getInstance();
        startRewardDate = Calendar.getInstance();
        startSpecialDate = Calendar.getInstance();
        startSickDate = Calendar.getInstance();

        timePick_Btn = findViewById(R.id.pickTime_id);
        paidCheck = findViewById(R.id.checkbox_paid);
        rewardCheck = findViewById(R.id.checkbox_reward);
        specialCheck = findViewById(R.id.checkbox_special);
        sickCheck = findViewById(R.id.checkbox_sick);

        paidRow = findViewById(R.id.paidRow);
        rewardRow = findViewById(R.id.rewardRow);
        specialRow = findViewById(R.id.specialRow);
        sickRow = findViewById(R.id.sickRow);

        startPaidDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startPaidDisplay, startPaidDate);
            }
        });
        startRewardDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startRewardDisplay, startRewardDate);
            }
        });
        startSpecialDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startSpecialDisplay, startSpecialDate);
            }
        });
        startSickDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(startSickDisplay, startSickDate);
            }
        });

        endPaidDisplay = findViewById(R.id.paidtextE_id);
        endPaidDate_Btn = findViewById(R.id.paidbuttonE_id);
        endRewardDisplay = findViewById(R.id.rewardtextE_id);
        endRewardDate_Btn = findViewById(R.id.rewardbuttonE_id);
        endSpecialDisplay = findViewById(R.id.specialtextE_id);
        endSpecialDate_Btn = findViewById(R.id.specialbuttonE_id);
        endSickDisplay = findViewById(R.id.sicktextE_id);
        endSickDate_Btn = findViewById(R.id.sickbuttonE_id);

        endPaidDate = Calendar.getInstance();
        endRewardDate = Calendar.getInstance();
        endSpecialDate = Calendar.getInstance();
        endSickDate = Calendar.getInstance();

        endPaidDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(endPaidDisplay, endPaidDate);
            }
        });
        endRewardDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(endRewardDisplay, endRewardDate);
            }
        });
        endSpecialDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(endSpecialDisplay, endSpecialDate);
            }
        });
        endSickDate_Btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(endSickDisplay, endSickDate);
            }
        });

        updateDisplay(startPaidDisplay, startPaidDate);
        updateDisplay(endPaidDisplay, endPaidDate);
        updateDisplay(startRewardDisplay, startRewardDate);
        updateDisplay(endRewardDisplay, endRewardDate);
        updateDisplay(startSpecialDisplay, startSpecialDate);
        updateDisplay(endSpecialDisplay, endSpecialDate);
        updateDisplay(startSickDisplay, startSickDate);
        updateDisplay(endSickDisplay, endSickDate);

        timePick_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetVacayActivity.this, SetVacayTimeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        paidCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    paidRow.setVisibility(View.VISIBLE);
                }else{
                    paidRow.setVisibility(View.GONE);
                }

            }
        });

        rewardCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rewardRow.setVisibility(View.VISIBLE);
                }else{
                    rewardRow.setVisibility(View.GONE);
                }

            }
        });

        specialCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    specialRow.setVisibility(View.VISIBLE);
                }else{
                    specialRow.setVisibility(View.GONE);
                }

            }
        });

        sickCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sickRow.setVisibility(View.VISIBLE);
                }else{
                    sickRow.setVisibility(View.GONE);
                }

            }
        });


    }

    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        // Month is 0 based so add 1
                        .append(date.get(Calendar.MONTH) + 1).append("-")
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
                        .append(date.get(Calendar.YEAR)).append(" "));

    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private OnDateSetListener dateSetListener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
    }


}