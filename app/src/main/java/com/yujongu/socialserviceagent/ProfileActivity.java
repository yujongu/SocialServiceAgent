package com.yujongu.socialserviceagent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity{
    final static String TAG = "ProfileActivityT";
    final static String KEY_STARTDATE = "StartDate";
    final static String KEY_ENDDATE = "EndDate";
    final static String KEY_MTYPE = "MType";

    final static String SP_PNAME = "ProfileName";
    final static String SP_PIMAGE = "ProfilePicUrl";
    final static String SP_STARTDATE = "StartingDate";
    final static String SP_ENDDATE = "EndingDate";
    final static String SP_MTYPE = "MilitaryType";
    final static String SP_PERSONLEAVEDAYS = "PersonalLeaveDays";

    //todo set multiple friend profiles to view
    Activity context = ProfileActivity.this;

    Map<String, Object> user;

    private ProfileInfo me;

    private TextView progressTv;
    private ProgressBar pbProgress;
    private CircleImageView profileIv;
    private TextView profileNameTv;
    private TextView militaryNameTv;
    private TextView startingDateTv, endingDateTv;
    private TextView totalDaysTv;
    private TextView discountDaysTv;
    private TextView personalLeaveTv;
    private Button editProfileBtn;
    private Button addFriendBtn;

    final DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.KOREA);

    private SharedPreference sharedPreference;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initInstances();
        eventListeners();

    }

    private void initInstances(){
        sharedPreference = new SharedPreference();
        db = FirebaseFirestore.getInstance();

        pbProgress = findViewById(R.id.timeLeftPb);
        progressTv = findViewById(R.id.tvProgress);
        profileIv = findViewById(R.id.profile_image);
        profileNameTv = findViewById(R.id.profile_name);
        editProfileBtn = findViewById(R.id.btnEditProfile);
        addFriendBtn = findViewById(R.id.btnAddFriend);
        militaryNameTv = findViewById(R.id.TvMilitaryName);
        startingDateTv = findViewById(R.id.selectedStartingDate);
        endingDateTv = findViewById(R.id.TvEndingDate);
        totalDaysTv = findViewById(R.id.TvTotalDays);
        discountDaysTv = findViewById(R.id.TvDiscountDays);
        personalLeaveTv = findViewById(R.id.TvPersonalLeave);

        setProfileInfo();
    }

    private void eventListeners(){
        startingDateTv.setOnClickListener(listener);
        editProfileBtn.setOnClickListener(listener);
    }

    private void setProfileInfo(){
        //fetch info from sharedPreference and save to me.
        me = new ProfileInfo(
                sharedPreference.loadStringData(context, SP_PNAME),
                sharedPreference.loadStringData(context, SP_PIMAGE)
        );

        boolean freshUser = true;

        //profileName
        profileNameTv.setText(me.getName());

        //profileImage
        if (me.getPictureUrl().equals("")){
            profileIv.setImageResource(R.drawable.kakao_default_profile_image);
        } else {
            Picasso.get()
                    .load(me.getPictureUrl())
                    .into(profileIv);
        }

        //mType
        if (sharedPreference.loadStringData(context, SP_MTYPE) == null){
            sharedPreference.saveData(context, SP_MTYPE, me.getMilitaryType().getEName());
        } else {
            me.setMilitaryType(getMilitaryEnum(sharedPreference.loadStringData(context, SP_MTYPE)));
        }
        militaryNameTv.setText(me.getMilitaryType().getKName());

        //personalLeaveDate
        if (sharedPreference.loadStringData(context, SP_PERSONLEAVEDAYS) == null){
            sharedPreference.saveData(context, SP_PERSONLEAVEDAYS, String.valueOf(me.getPersonalLeaveDays()));
        } else {
            me.setPersonalLeaveDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_PERSONLEAVEDAYS)));
        }
        personalLeaveTv.setText(getString(R.string.dateText, me.getPersonalLeaveDays()));

        //start and end date
        if (sharedPreference.loadStringData(context, SP_STARTDATE) == null){
            sharedPreference.saveData(context, SP_STARTDATE, df.format(me.getStartService()));
        } else {
            try {
                me.setStartService(df.parse(sharedPreference.loadStringData(context, SP_STARTDATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        me.setEndService(getEndDate(
                me.getMilitaryType(), me.getStartService(), me.getPersonalLeaveDays()));
        sharedPreference.saveData(context,SP_ENDDATE, df.format(me.getEndService()));

        startingDateTv.setText(df.format(me.getStartService()));
        endingDateTv.setText(df.format(me.getEndService()));
    }


    private Date calculateServiceLength(MilitaryTypeEnum militaryTypeEnum, Date startDate){
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

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnEditProfile:
                    redirectEditProfileActivity();
                    break;

                case R.id.btnAddFriend:

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

    public Date getEndDate(MilitaryTypeEnum mType, Date startDate, int personalLeaveDays){
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(calculateServiceLength(mType, startDate));
        endDateCal.add(Calendar.DATE, personalLeaveDays);

        final Handler h = new Handler();
        h.post(new Runnable() {
            @Override
            public void run() {
                pbProgress.setProgress((int) (getProgressPercentage() * 10000));
                progressTv.setText(String.valueOf(getProgressPercentage()));

                h.postDelayed(this, 1000);
            }
        });
        return endDateCal.getTime();
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
