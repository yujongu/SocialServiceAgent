package com.yujongu.socialserviceagent;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity{

    //todo set multiple friend profiles to view
    Activity context = ProfileActivity.this;

    private ProfileInfo me;

    private TextView progressTv;
    private ProgressBar pbProgress;
    private CircleImageView profileIv;
    private TextView profileNameTv;
    private Spinner mTypeSpinner;
    private TextView militaryNameTv;
    private TextView startingDateTv, endingDateTv;
    private TextView totalDaysTv;
    private TextView discountDaysTv;
    private TextView personalLeaveTv;
    private Button editProfileBtn;

    final DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.KOREA);

    private SharedPreference sharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initInstances();
        eventListeners();

    }



    private void initInstances(){
        sharedPreference = new SharedPreference();

        pbProgress = findViewById(R.id.timeLeftPb);
        progressTv = findViewById(R.id.tvProgress);

        profileIv = findViewById(R.id.profile_image);
        profileNameTv = findViewById(R.id.profile_name);

        setProfileInfo();

        editProfileBtn = findViewById(R.id.btnEditProfile);

        mTypeSpinner = findViewById(R.id.militarySpinner);

        if (sharedPreference.loadStringData(context, "MilitaryType") != null){
            mTypeSpinner.setSelection(getMilitaryEnum(sharedPreference.loadStringData(context, "MilitaryType")).getPosition());
        }

        militaryNameTv = findViewById(R.id.TvMilitaryName);

        startingDateTv = findViewById(R.id.selectedStartingDate);
        startingDateTv.setText(df.format(me.getStartService()));

        endingDateTv = findViewById(R.id.TvEndingDate);

        totalDaysTv = findViewById(R.id.TvTotalDays);
        discountDaysTv = findViewById(R.id.TvDiscountDays);
        personalLeaveTv = findViewById(R.id.TvPersonalLeave);
        if (sharedPreference.loadStringData(context, "PersonalLeaveDays") != null){
            personalLeaveTv.setText(sharedPreference.loadStringData(context, "PersonalLeaveDays"));
        } else {
            sharedPreference.saveData(context, "PersonalLeaveDays", String.valueOf(0));
        }
    }

    private void eventListeners(){
        mTypeSpinner.setOnItemSelectedListener(avSelectedListener);

        startingDateTv.setOnClickListener(listener);
        editProfileBtn.setOnClickListener(listener);
    }

    private void setProfileInfo(){
        //fetch info from sharedPreference and save to me.
        me = new ProfileInfo(
                sharedPreference.loadStringData(context, "ProfileName"),
                sharedPreference.loadStringData(context, "ProfilePicUrl")
        );

        try {
            if (sharedPreference.loadStringData(context, "StartingDate") != null){
                me.setStartService(df.parse(sharedPreference.loadStringData(context, "StartingDate")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //profileImage
        if (me.getPictureUrl().equals("")){
            profileIv.setImageResource(R.drawable.kakao_default_profile_image);
        } else {
            Picasso.get()
                    .load(me.getPictureUrl())
                    .into(profileIv);
        }

        //profileName
        profileNameTv.setText(me.getName());

    }

    private Date calculateFreedom(MilitaryTypeEnum militaryTypeEnum, Date startDate){
        Date firstDate = new GregorianCalendar(2018, 9, 1).getTime(); //2018/10/1

        Calendar calendar = Calendar.getInstance();
        //---2018/10/1---
        calendar.setTime(firstDate);

        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.MONTH, -militaryTypeEnum.getValue()); //군 기간

        calendar.add(Calendar.DATE,1);

        firstDate = calendar.getTime();

        long diffMillis = Math.abs(startDate.getTime() - firstDate.getTime());

        long diff = TimeUnit.DAYS.convert(diffMillis, TimeUnit.MILLISECONDS);

        int discountDays = 0;
        if (militaryTypeEnum == MilitaryTypeEnum.AIRFORCE){
            if ((diff / 14) + 1 <= 60){
                discountDays = (int) (diff / 14 + 1);
            } else {
                discountDays = 60;
            }
        } else {
            if ((diff / 14) + 1 <= 90){
                discountDays = (int) (diff / 14 + 1);
            } else {
                discountDays = 90;
            }
        }

        calendar.setTime(startDate);

        calendar.add(Calendar.MONTH, militaryTypeEnum.getValue());
        calendar.add(Calendar.DATE, (-discountDays));
        calendar.add(Calendar.DATE, -1);

        long totalDiffMillis = Math.abs(calendar.getTime().getTime() - startDate.getTime());

        long totalDiffDays = TimeUnit.DAYS.convert(totalDiffMillis, TimeUnit.MILLISECONDS);

        Log.i("TotalDay", String.valueOf(totalDiffDays));
        totalDaysTv.setText(totalDiffDays + "일");
        discountDaysTv.setText(discountDays + "일");
        return calendar.getTime();
    }

    private MilitaryTypeEnum getMilitaryEnum(String eName){
        switch (eName){
            case "Army":
                return MilitaryTypeEnum.ARMY;

            case "Marine":
                return MilitaryTypeEnum.MARINE;

            case "Navy":
                return MilitaryTypeEnum.NAVY;

            case "Airforce":
                return MilitaryTypeEnum.AIRFORCE;

            case "Police":
                return MilitaryTypeEnum.POLICE;

            case "Fire":
                return MilitaryTypeEnum.FIRE;

            case "SSA":
            default:
                return MilitaryTypeEnum.SSA;
        }
    }


    AdapterView.OnItemSelectedListener avSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getItemAtPosition(i).toString()){
                case "Army":
                    getEndDate(MilitaryTypeEnum.ARMY);
                    break;

                case "Marine":
                    getEndDate(MilitaryTypeEnum.MARINE);
                    break;

                case "Navy":
                    getEndDate(MilitaryTypeEnum.NAVY);
                    break;

                case "Airforce":
                    getEndDate(MilitaryTypeEnum.AIRFORCE);
                    break;

                case "Police":
                    getEndDate(MilitaryTypeEnum.POLICE);
                    break;

                case "Fire":
                    getEndDate(MilitaryTypeEnum.FIRE);
                    break;

                case "SSA":
                default:
                    getEndDate(MilitaryTypeEnum.SSA);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
//                case R.id.selectedStartingDate:
//                    final Calendar cal = Calendar.getInstance();
//                    int year = cal.get(Calendar.YEAR);
//                    int month = cal.get(Calendar.MONTH);
//                    int date = cal.get(Calendar.DATE);
//
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(DatePicker datePicker, int nYear, int nMonth, int nDate) {
//
//                            Calendar calendar = new GregorianCalendar(nYear, nMonth, nDate);
//                            sharedPreference.saveData(context, "StartingDate",
//                                    df.format(calendar.getTime()));
//
//                            me.setStartService(calendar.getTime());
//
//                            startingDateTv.setText(sharedPreference.loadStringData(context, "StartingDate"));
//
//                            sharedPreference.saveData(context, "EndingDate",
//                                    df.format(calculateFreedom(me.getMilitaryType(), me.getStartService())));
//
//                            me.setEndService(calculateFreedom(me.getMilitaryType(), me.getStartService()));
//
//                            endingDateTv.setText(sharedPreference.loadStringData(context, "EndingDate"));
//
//                        }
//                    }, year, month, date);
//                    datePickerDialog.show();
//                    break;

                case R.id.btnEditProfile:
                    redirectEditProfileActivity();
                    break;
            }
        }
    };

    private double getProgressPercentage(){
        double diffMillisTotal = Math.abs(me.getEndService().getTime() - me.getStartService().getTime());
        double diffMillisCurrent = Math.abs(Calendar.getInstance().getTime().getTime() - me.getStartService().getTime());

        if (me.getStartService().compareTo(Calendar.getInstance().getTime()) > 0){
            return 0;
        }

        double percent = diffMillisCurrent * 100 / diffMillisTotal;
        BigDecimal bd = new BigDecimal(percent).setScale(7, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    @Override
    public void onBackPressed() {
        redirectMainActivity();
    }

    public void getEndDate(MilitaryTypeEnum type){
        sharedPreference.saveData(context, "MilitaryType", type.getEName());
        me.setMilitaryType(type);
        militaryNameTv.setText(me.getMilitaryType().getKName());
        if (sharedPreference.loadStringData(context, "PersonalLeaveDays").equals("")){
            sharedPreference.saveData(context, "PersonalLeaveDays", "0");
        }
        int personalLeaveDays = Integer.parseInt(sharedPreference.loadStringData(context, "PersonalLeaveDays"));
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(calculateFreedom(me.getMilitaryType(), me.getStartService()));
        endDateCal.add(Calendar.DATE, personalLeaveDays);
        sharedPreference.saveData(context, "EndingDate", df.format(endDateCal.getTime()));
        me.setEndService(endDateCal.getTime());

        endingDateTv.setText(sharedPreference.loadStringData(context, "EndingDate"));

        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                pbProgress.setProgress((int) (getProgressPercentage() * 10000));
                progressTv.setText(String.valueOf(getProgressPercentage()));

                h.postDelayed(this, 1000);
            }
        });
    }

    private void redirectMainActivity(){
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectEditProfileActivity(){
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }






}
