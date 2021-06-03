package com.yujongu.socialserviceagent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ProfileViewPagerActivity extends AppCompatActivity {

    Context context;
    private ViewPager viewPager;
    final static String TAG = "ProfileViewPagerActivityT";

    private SharedPreference sharedPreference;

    ArrayList<ProfileInfo> profiles;

    private ProfileInfo me;

    final DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.KOREA);

    final static String SP_PNAME = "ProfileName";
    final static String SP_PIMAGE = "ProfilePicUrl";
    final static String SP_STARTDATE = "StartingDate";
    final static String SP_ENDDATE = "EndingDate";
    final static String SP_MTYPE = "MilitaryType";
    final static String SP_PERSONLEAVEDAYS = "PersonalLeaveDays";
    final static String SP_REWARDDAYS = "UsedReward";
    final static String SP_SPECIALDAYS = "UsedSpecial";
    final static String SP_SICKDAYS = "UsedSick";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_pager);

        initInstances();
    }
    private void initInstances(){
        context = this;
        viewPager = findViewById(R.id.pViewPager);
        profiles = new ArrayList<ProfileInfo>();

        sharedPreference = new SharedPreference();
    }

    private void setMyProfileInfo(){
        //fetch info from sharedPreference and save to me.

        //profile name & profile image
        me = new ProfileInfo(
                sharedPreference.loadStringData(context, SP_PNAME),
                sharedPreference.loadStringData(context, SP_PIMAGE)
        );

        //mType
        if (sharedPreference.loadStringData(context, SP_MTYPE) == null){
            sharedPreference.saveData(context, SP_MTYPE, me.getMilitaryType().getEName());
        } else {
            me.setMilitaryType(getMilitaryEnum(sharedPreference.loadStringData(context, SP_MTYPE)));
        }

        //personalLeaveDate
        if (sharedPreference.loadStringData(context, SP_PERSONLEAVEDAYS) == null){
            sharedPreference.saveData(context, SP_PERSONLEAVEDAYS, String.valueOf(me.getPersonalLeaveDays()));
        } else {
            me.setPersonalLeaveDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_PERSONLEAVEDAYS)));
        }

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

        //paid leave days.
        String paidLeaveHours =sharedPreference.loadStringData(context, "PaidLeaveDays");
        if (sharedPreference.loadStringData(context, "PaidLeaveDays") == null){
            paidLeaveHours = "0일 0시간 0분";
        }
        String[] hold = paidLeaveHours.split(" ");
        paidLeaveHours = hold[0];
        if(me.getMilitaryType().getKName().equals("사회복무요원")) {
            paidLeaveHours =sharedPreference.loadStringData(context, "PaidLeaveDays");
        }
        me.setPaidLeaveDays(paidLeaveHours);

        //포상휴가
        if (sharedPreference.loadStringData(context, SP_REWARDDAYS) == null){
            sharedPreference.saveData(context, SP_REWARDDAYS, String.valueOf(me.getRewardDays()));
        } else {
            me.setRewardDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_REWARDDAYS)));
        }

        //위로휴가
        if (sharedPreference.loadStringData(context, SP_SPECIALDAYS) == null){
            sharedPreference.saveData(context, SP_SPECIALDAYS, String.valueOf(me.getSpecialDays()));
        } else {
            me.setSpecialDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_SPECIALDAYS)));
        }

        //병가 및 청원
        if (sharedPreference.loadStringData(context, SP_SICKDAYS) == null){
            sharedPreference.saveData(context, SP_SICKDAYS, String.valueOf(me.getSickDays()));
        } else {
            me.setSickDays(Integer.parseInt(sharedPreference.loadStringData(context, SP_SICKDAYS)));
        }
    }

    public Date getEndDate(MilitaryTypeEnum mType, Date startDate, int personalLeaveDays){
        Calendar endDateCal = Calendar.getInstance();
        endDateCal.setTime(calculateServiceLength(mType, startDate));
        endDateCal.add(Calendar.DATE, personalLeaveDays);

        return endDateCal.getTime();
    }

    //set total days & discount days
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



        me.setTotalDays(totalDiffDays);
        me.setDiscoutDays(discountDays);
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
}
