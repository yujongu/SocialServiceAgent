package com.yujongu.socialserviceagent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    final static String TAG = "EditProfileActivityT";
    Activity context = EditProfileActivity.this;

    private ProfileInfo me;
    private CircleImageView profileIv;
    private TextView profileNameTv;
    private SharedPreference sharedPreference;

    private Button cancelBtn;
    private Button saveBtn;

    private TextView ep_startingDateTv;

    private Spinner mTypeSpinner;
    private TextView militaryNameTv;

    private EditText ep_paidLeaveDTv;
    private EditText ep_paidLeaveHTv;
    private EditText ep_paidLeaveMTv;

    private EditText ep_personalLeaveTv;

    private TextView ep_TvDay;
    private TextView ep_TvHour;
    private TextView ep_TvMinute;
    private TextView ep_TvUsedVacayH;
    private TextView ep_TvUsedVacayD;
    private TextView ep_TvSickH;
    private TextView ep_TvSickD;
    private EditText ep_EtUsedSick;
    private EditText ep_EtPaidLeave;

    private TextView ep_TvReward;
    private TextView ep_TvSpecial;
    private EditText ep_EtUsedReward;
    private EditText ep_EtUsedSpecial;

    final DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.KOREA);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initInstances();
        eventListeners();

        setProfileInfo();
    }

    private void initInstances(){
        profileIv = findViewById(R.id.ep_profile_image);
        profileNameTv = findViewById(R.id.ep_profile_name);

        sharedPreference = new SharedPreference();


        cancelBtn = findViewById(R.id.btnCancel);
        saveBtn = findViewById(R.id.btnSave);

        mTypeSpinner = findViewById(R.id.ep_militarySpinner);

        militaryNameTv = findViewById(R.id.ep_TvMilitaryName);

        ep_startingDateTv = findViewById(R.id.epTvStartingDate);

        ep_paidLeaveDTv = findViewById(R.id.ep_EtPaidLeaveD);
        ep_paidLeaveHTv = findViewById(R.id.ep_EtPaidLeaveH);
        ep_paidLeaveMTv = findViewById(R.id.ep_EtPaidLeaveM);

        ep_personalLeaveTv = findViewById(R.id.ep_EtPersonalLeave);

        ep_TvDay = findViewById(R.id.ep_TvDay);
        ep_TvHour = findViewById(R.id.ep_TvHour);
        ep_TvMinute = findViewById(R.id.ep_TvMinute);
        ep_TvUsedVacayH = findViewById(R.id.ep_TvUsedVacayH);
        ep_TvUsedVacayD = findViewById(R.id.ep_TvUsedVacayD);
        ep_TvSickH = findViewById(R.id.ep_TvSickH);
        ep_TvSickD = findViewById(R.id.ep_TvSickD);
        ep_EtPaidLeave = findViewById(R.id.ep_EtPaidLeave);

        ep_TvReward = findViewById(R.id.ep_TvReward);
        ep_TvSpecial = findViewById(R.id.ep_TvSpecial);
        ep_EtUsedReward = findViewById(R.id.ep_EtUsedReward);
        ep_EtUsedSpecial = findViewById(R.id.ep_EtUsedSpecial);
        ep_EtUsedSick = findViewById(R.id.ep_EtUsedSick);

    }

    private void eventListeners(){
        cancelBtn.setOnClickListener(listener);
        saveBtn.setOnClickListener(listener);
        mTypeSpinner.setOnItemSelectedListener(avSelectedListener);
        ep_startingDateTv.setOnClickListener(listener);

    }

    public void setVisibilitySoldier(){
        ep_TvDay.setVisibility(View.GONE);
        ep_TvHour.setVisibility(View.GONE);
        ep_TvMinute.setVisibility(View.GONE);
        ep_EtPaidLeave.setVisibility(View.VISIBLE);
        ep_paidLeaveDTv.setVisibility(View.GONE);
        ep_paidLeaveHTv.setVisibility(View.GONE);
        ep_paidLeaveMTv.setVisibility(View.GONE);
        ep_TvUsedVacayH.setVisibility(View.GONE);
        ep_TvUsedVacayD.setVisibility(View.VISIBLE);
        ep_TvSickH.setVisibility(View.GONE);
        ep_TvSickD.setVisibility(View.VISIBLE);
        ep_TvReward.setVisibility(View.VISIBLE);
        ep_TvSpecial.setVisibility(View.VISIBLE);
        ep_EtUsedSpecial.setVisibility(View.VISIBLE);
        ep_EtUsedReward.setVisibility(View.VISIBLE);
    }
    AdapterView.OnItemSelectedListener avSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getItemAtPosition(i).toString()){
                case "Army":
                    militaryNameTv.setText(MilitaryTypeEnum.ARMY.getKName());
                    setVisibilitySoldier();
                    break;

                case "Marine":
                    militaryNameTv.setText(MilitaryTypeEnum.MARINE.getKName());
                    setVisibilitySoldier();
                    break;

                case "Navy":
                    militaryNameTv.setText(MilitaryTypeEnum.NAVY.getKName());
                    setVisibilitySoldier();
                    break;

                case "Airforce":
                    militaryNameTv.setText(MilitaryTypeEnum.AIRFORCE.getKName());
                    setVisibilitySoldier();
                    break;

                case "Police":
                    militaryNameTv.setText(MilitaryTypeEnum.POLICE.getKName());
                    setVisibilitySoldier();
                    break;

                case "Fire":
                    militaryNameTv.setText(MilitaryTypeEnum.FIRE.getKName());
                    break;

                case "SSA":
                default:
                    militaryNameTv.setText(MilitaryTypeEnum.SSA.getKName());
                    ep_TvDay.setVisibility(View.VISIBLE);
                    ep_TvHour.setVisibility(View.VISIBLE);
                    ep_TvMinute.setVisibility(View.VISIBLE);
                    ep_EtPaidLeave.setVisibility(View.GONE);
                    ep_paidLeaveDTv.setVisibility(View.VISIBLE);
                    ep_paidLeaveHTv.setVisibility(View.VISIBLE);
                    ep_paidLeaveMTv.setVisibility(View.VISIBLE);
                    ep_TvUsedVacayH.setVisibility(View.VISIBLE);
                    ep_TvUsedVacayD.setVisibility(View.GONE);
                    ep_TvSickH.setVisibility(View.VISIBLE);
                    ep_TvSickD.setVisibility(View.GONE);
                    ep_TvReward.setVisibility(View.GONE);
                    ep_TvSpecial.setVisibility(View.GONE);
                    ep_EtUsedSpecial.setVisibility(View.GONE);
                    ep_EtUsedReward.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    final static String MTYPE = "Military Type";
    final static String SDATE = "Start Date";
    final static String EDATE = "End Date";
    final static String PAIDLDAYS = "Paid Leave Hours";
    final static String PAIDLDAYSSOLDIERS = "Paid Leave Hours Soldiers";
    final static String REWARDDAYS = "Paid Reward Days";
    final static String SPECIALDAYS = "Paid Special Days";
    final static String SICKDAYS = "Paid Sick Days";

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnCancel:
                    redirectProfileActivity();
                    break;

                case R.id.btnSave:
                    sharedPreference.saveData(context, "MilitaryType", mTypeSpinner.getSelectedItem().toString());
                    sharedPreference.saveData(context, "StartingDate", ep_startingDateTv.getText().toString());

                    //paid leave time
                    int days = 0;
                    int hours = 0;
                    int minutes = 0;
                    if (ep_paidLeaveDTv.getText().toString().equals("") || ep_EtPaidLeave.getText().toString().equals("")){
                        days = 0;
                    } else {
                        days = Integer.parseInt(ep_paidLeaveDTv.getText().toString());
                        days = Integer.parseInt(ep_EtPaidLeave.getText().toString());
                    }
                    if (ep_paidLeaveHTv.getText().toString().equals("")){
                        hours = 0;
                    } else {
                        hours = Integer.parseInt(ep_paidLeaveHTv.getText().toString());

                    }
                    if (ep_paidLeaveMTv.getText().toString().equals("")){
                        minutes = 0;
                    } else {
                        minutes = Integer.parseInt(ep_paidLeaveMTv.getText().toString());
                    }
                    sharedPreference.saveData(context, "PaidLeaveDays", days + "일 " + hours + "시간 " + minutes + "분");
                    sharedPreference.saveData(context, "PaidLeaveDaysSoldiers", days + "일");

                    //personal leave days
                    if (ep_personalLeaveTv.getText().toString().equals("")){
                        ep_personalLeaveTv.setText("0");
                    }
                    sharedPreference.saveData(context, "PersonalLeaveDays", ep_personalLeaveTv.getText().toString());

                    //포상휴가
                    sharedPreference.saveData(context, "UsedReward", ep_EtUsedReward.getText().toString());
                    //위로휴가
                    sharedPreference.saveData(context, "UsedSpecial", ep_EtUsedSpecial.getText().toString());

                    //청원 및 병가
                    sharedPreference.saveData(context, "UsedSick", ep_EtUsedSick.getText().toString());

                    Map<String, Object> user = new HashMap<>();
                    user.put(MTYPE, mTypeSpinner.getSelectedItem().toString());
                    user.put(SDATE, ep_startingDateTv.getText().toString());
                    user.put(PAIDLDAYS, sharedPreference.loadStringData(context, "PaidLeaveDays"));
                    user.put(PAIDLDAYSSOLDIERS, sharedPreference.loadStringData(context, "PaidLeaveDaysSoldiers"));
                    user.put(REWARDDAYS, sharedPreference.loadStringData(context, "UsedReward"));
                    user.put(SPECIALDAYS, sharedPreference.loadStringData(context, "UsedSpecial"));
                    user.put(SICKDAYS, sharedPreference.loadStringData(context, "UsedSick"));

                    updateUserToCloud(sharedPreference.loadStringData(context, "UserId"), user);
                    redirectProfileActivity();
                    break;

                case R.id.epTvStartingDate:
                    final Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int date = cal.get(Calendar.DATE);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int nYear, int nMonth, int nDate) {

                            Calendar calendar = new GregorianCalendar(nYear, nMonth, nDate);
                            ep_startingDateTv.setText(df.format(calendar.getTime()));
                        }
                    }, year, month, date);
                    datePickerDialog.show();
                    break;
            }
        }
    };

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

    private void setProfileInfo(){
        //fetch info from sharedPreference and save to me.
        me = new ProfileInfo(
                sharedPreference.loadStringData(context, "ProfileName"),
                sharedPreference.loadStringData(context, "ProfilePicUrl")
        );

        //profileImage.
        if (me.getPictureUrl().equals("")){
            profileIv.setImageResource(R.drawable.kakao_default_profile_image);
        } else {
            Picasso.get()
                    .load(me.getPictureUrl())
                    .into(profileIv);
        }

        //profileName.
        profileNameTv.setText(me.getName());

        //set spinner value if available
        if (sharedPreference.loadStringData(context, "MilitaryType") != null){
            String mType = sharedPreference.loadStringData(context, "MilitaryType");
            me.setMilitaryType(getMilitaryEnum(mType));
            mTypeSpinner.setSelection(me.getMilitaryType().getPosition());
        }

        //start date.
        if (sharedPreference.loadStringData(context, "StartingDate") == null){
            sharedPreference.saveData(context, "StartingDate", df.format(me.getStartService()));
        } else {
            try {
                me.setStartService(df.parse(sharedPreference.loadStringData(context, "StartingDate")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ep_startingDateTv.setText(df.format(me.getStartService()));

        //paid leave days(연차)
        Calendar calendar = Calendar.getInstance();
        if (sharedPreference.loadStringData(context, "PaidLeaveDays") != null){
            String[] leaveDays = sharedPreference.loadStringData(context, "PaidLeaveDays").split(" ");
            me.setPaidLeaveDays(sharedPreference.loadStringData(context, "PaidLeaveDays"));
            ep_paidLeaveDTv.setText(leaveDays[0].replace("일", ""));
            ep_paidLeaveHTv.setText(leaveDays[1].replace("시간", ""));
            ep_paidLeaveMTv.setText(leaveDays[2].replace("분", ""));


//                Date paidLeaveTime = new SimpleDateFormat("d일 h시간 m분").parse(sharedPreference.loadStringData(context, "PaidLeaveDays"));
//                calendar.setTime(paidLeaveTime);
//                me.setPaidLeaveDays(paidLeaveTime);
//
//                ep_paidLeaveDTv.setText(String.valueOf(calendar.get(Calendar.DATE)));
//                ep_paidLeaveHTv.setText(String.valueOf(calendar.get(Calendar.HOUR)));
//                ep_paidLeaveMTv.setText(String.valueOf(calendar.get(Calendar.MINUTE)));
        }
        if (sharedPreference.loadStringData(context, "PaidLeaveDaysSoldiers") != null) {
            String leaveDays = sharedPreference.loadStringData(context, "PaidLeaveDaysSoldiers");
            me.setPaidLeaveDays(sharedPreference.loadStringData(context, "PaidLeaveDaysSoldiers"));
            ep_EtPaidLeave.setText(leaveDays.replace("일", ""));
        }

        //personal leave days(복무 연장)
        ep_personalLeaveTv.setText(sharedPreference.loadStringData(context, "PersonalLeaveDays"));

        //포상휴가
        ep_EtUsedReward.setText(sharedPreference.loadStringData(context, "UsedReward"));

        //위로휴가
        ep_EtUsedSpecial.setText(sharedPreference.loadStringData(context, "UsedSpecial"));

        //청원 및 병가
        ep_EtUsedSick.setText(sharedPreference.loadStringData(context, "UsedSick"));

    }

    @Override
    public void onBackPressed() {
        redirectProfileActivity();
    }

    private void redirectProfileActivity(){
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUserToCloud(String documentName, Map<String, Object> mapObj){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(documentName).set(mapObj, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Successfully saved to cloud", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to save to cloud", Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });
    }

}
