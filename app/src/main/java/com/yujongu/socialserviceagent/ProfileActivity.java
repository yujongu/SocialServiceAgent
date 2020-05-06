package com.yujongu.socialserviceagent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
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
    final static String SP_REWARDDAYS = "UsedReward";
    final static String SP_SPECIALDAYS = "UsedSpecial";
    final static String SP_SICKDAYS = "UsedSick";

    //todo set multiple friend profiles to view
    Activity context = ProfileActivity.this;

    Map<String, Object> user;

    public ProfileInfo me;

    private TextView progressTv;
    private ProgressBar pbProgress;
    private CircleImageView profileIv;
    private TextView profileNameTv;
    private TextView militaryNameTv;
    private TextView startingDateTv, endingDateTv;
    private TextView totalDaysTv;
    private TextView discountDaysTv;
    private TextView paidLeaveTv;
    private TextView personalLeaveTv;
    private TextView TvPaidReward;
    private TextView TvPaidSpecial;
    private Button editProfileBtn;
    private Button addFriendBtn;
    private TableLayout tablelayout;
    private TableRow rowReward;
    private TableRow rowSpecial;
    private TextView TvTotalReward;
    private TextView TvSick;
    private TextView TvSickLeave;

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

        tablelayout = findViewById(R.id.tablelayout);

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
        paidLeaveTv = findViewById(R.id.TvPaidLeave);
        personalLeaveTv = findViewById(R.id.TvPersonalLeave);
        TvPaidReward = findViewById(R.id.TvPaidReward);
        TvPaidSpecial = findViewById(R.id.TvPaidSpecial);
        rowReward = findViewById(R.id.rowReward);
        rowSpecial = findViewById(R.id.rowSpecial);
        TvTotalReward = findViewById(R.id.TvTotalReward);
        TvSick = findViewById(R.id.TvSick);
        TvSickLeave = findViewById(R.id.TvSickLeave);



        setProfileInfo();
        if(militaryNameTv.getText().toString().equals("육군")){
            rowSoldier();
            militaryNameTv.setTextColor(Color.parseColor("#456646"));
        }else if(militaryNameTv.getText().toString().equals("해군")){
            militaryNameTv.setTextColor(Color.parseColor("#1885B7"));
            rowSoldier();
        }else if(militaryNameTv.getText().toString().equals("공군")) {
            militaryNameTv.setTextColor(Color.parseColor("#263790"));
            rowSoldier();
        }else if(militaryNameTv.getText().toString().equals("해병대")) {
            militaryNameTv.setTextColor(Color.parseColor("#A70D0D"));
            rowSoldier();
        }else if(militaryNameTv.getText().toString().equals("의경")) {
            militaryNameTv.setTextColor(Color.parseColor("#8D8D8D"));
            rowSoldier();
        }else if(militaryNameTv.getText().toString().equals("의무소방")) {
            militaryNameTv.setTextColor(Color.parseColor("#C85834"));
            rowSoldier();
        }else if(militaryNameTv.getText().toString().equals("사회복무요원")) {
            militaryNameTv.setTextColor(Color.parseColor("#833651"));
            rowReward.setVisibility(View.GONE);
            rowSpecial.setVisibility(View.GONE);
        }

    }

    private void eventListeners(){
        editProfileBtn.setOnClickListener(listener);
        addFriendBtn.setOnClickListener(listener);
    }

    private void rowSoldier(){
        rowReward.setVisibility(View.VISIBLE);
        rowSpecial.setVisibility(View.VISIBLE);
        TvSick.setText("청원휴가(병가): ");
    }

    private void vacayDurationAMP(){
        if(Integer.parseInt(discountDaysTv.getText().toString().substring(0,2))>45 && Integer.parseInt(discountDaysTv.getText().toString().substring(0,2))<76){
            paidLeaveTv.append((" / " + 26 + "일"));
        }else if(Integer.parseInt(discountDaysTv.getText().toString().substring(0,2))>75 && Integer.parseInt(discountDaysTv.getText().toString().substring(0,2))<90) {
            paidLeaveTv.append((" / " + 25 + "일"));
        }else{
            paidLeaveTv.append((" / " + 24 + "일"));
        }
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
        me.setEndService(getEndDate(me.getMilitaryType(), me.getStartService(), me.getPersonalLeaveDays()));
        sharedPreference.saveData(context,SP_ENDDATE, df.format(me.getEndService()));

        startingDateTv.setText(df.format(me.getStartService()));
        endingDateTv.setText(df.format(me.getEndService()));

        //paid leave days.
        String paidLeaveHours =sharedPreference.loadStringData(context, "PaidLeaveDays");



        if(militaryNameTv.getText().toString().equals("육군")){
            paidLeaveTv.setText(paidLeaveHours.substring(0,2));
            //정해진 휴가일수 측정
            TvTotalReward.setText("포상휴가(총 18): ");
            vacayDurationAMP();
        }else if(militaryNameTv.getText().toString().equals("해군")){
            paidLeaveTv.setText(paidLeaveHours.substring(0,2));
            TvTotalReward.setText("포상휴가(총 19): ");
            paidLeaveTv.append((" / " + 27 + "일"));
        }else if(militaryNameTv.getText().toString().equals("공군")) {
            paidLeaveTv.setText(paidLeaveHours.substring(0,2));
            TvTotalReward.setText("포상휴가(총 20): ");
            paidLeaveTv.append((" / " + 29 + "일"));
        }else if(militaryNameTv.getText().toString().equals("해병대")) {
            paidLeaveTv.setText(paidLeaveHours.substring(0,2));
            TvTotalReward.setText("포상휴가(총 19): ");
            vacayDurationAMP();
        }else if(militaryNameTv.getText().toString().equals("의경")) {
            paidLeaveTv.setText(paidLeaveHours.substring(0,2));
            TvTotalReward.setText("포상휴가: ");
            vacayDurationAMP();
        }else if(militaryNameTv.getText().toString().equals("의무소방")) {
            paidLeaveTv.setText(paidLeaveHours.substring(0,2));
            TvTotalReward.setText("포상휴가: ");
            paidLeaveTv.append((" / " + 27 + "일"));
        }else if(militaryNameTv.getText().toString().equals("사회복무요원")) {
            paidLeaveTv.setText(paidLeaveHours);
            int tNumPLeaveDays = 15;
            Calendar today = Calendar.getInstance();
            today.add(Calendar.DAY_OF_MONTH, -365);
            //1년 초과 확인
            if (today.getTime().after(me.getStartService())){
                //2020 3월 2일 이후 소집인지 확인
                today.set(Calendar.YEAR, 2020);
                today.set(Calendar.MONTH, 2);
                today.set(Calendar.DAY_OF_MONTH, 1);
                if (me.getStartService().after(today.getTime())){
                    tNumPLeaveDays = 13;
                } else {
                    tNumPLeaveDays = 16;
                }
            }
            paidLeaveTv.append(" / " + tNumPLeaveDays + "일");
            TvSick.setText("병가(총 30): ");
        }

        //포상휴가
        if (sharedPreference.loadStringData(context, SP_REWARDDAYS) == null){
            sharedPreference.saveData(context, SP_REWARDDAYS, String.valueOf(me.getRewardDays()));
        } else {
            me.setRewardDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_REWARDDAYS)));
        }
        TvPaidReward.setText(getString(R.string.dateText, me.getRewardDays()));

        //위로휴가
        if (sharedPreference.loadStringData(context, SP_SPECIALDAYS) == null){
            sharedPreference.saveData(context, SP_SPECIALDAYS, String.valueOf(me.getSpecialDays()));
        } else {
            me.setSpecialDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_SPECIALDAYS)));
        }
        TvPaidSpecial.setText(getString(R.string.dateText, me.getSpecialDays()));

        //병가 및 청원
        if (sharedPreference.loadStringData(context, SP_SICKDAYS) == null){
            sharedPreference.saveData(context, SP_SICKDAYS, String.valueOf(me.getSickDays()));
        } else {
            me.setSickDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_SICKDAYS)));
        }
        TvSickLeave.setText(getString(R.string.dateText, me.getSickDays()));

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
                return MilitaryTypeEnum.SSA;
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
                    redirectAddFriendsActivity();
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

    private void redirectAddFriendsActivity(){
        Intent intent = new Intent(ProfileActivity.this, AddFriendsActivity.class);
        startActivity(intent);
    }


}
