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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


    private EditText ep_personalLeaveTv;

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

        ep_personalLeaveTv = findViewById(R.id.ep_TvPersonalLeave);
    }

    private void eventListeners(){
        cancelBtn.setOnClickListener(listener);
        saveBtn.setOnClickListener(listener);
        mTypeSpinner.setOnItemSelectedListener(avSelectedListener);
        ep_startingDateTv.setOnClickListener(listener);

    }
    AdapterView.OnItemSelectedListener avSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (adapterView.getItemAtPosition(i).toString()){
                case "Army":
                    militaryNameTv.setText(MilitaryTypeEnum.ARMY.getKName());
                    break;

                case "Marine":
                    militaryNameTv.setText(MilitaryTypeEnum.MARINE.getKName());
                    break;

                case "Navy":
                    militaryNameTv.setText(MilitaryTypeEnum.NAVY.getKName());
                    break;

                case "Airforce":
                    militaryNameTv.setText(MilitaryTypeEnum.AIRFORCE.getKName());
                    break;

                case "Police":
                    militaryNameTv.setText(MilitaryTypeEnum.POLICE.getKName());
                    break;

                case "Fire":
                    militaryNameTv.setText(MilitaryTypeEnum.FIRE.getKName());
                    break;

                case "SSA":
                default:
                    militaryNameTv.setText(MilitaryTypeEnum.SSA.getKName());
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
                    if (ep_personalLeaveTv.getText().toString().equals("")){
                        ep_personalLeaveTv.setText("0");
                    }
                    sharedPreference.saveData(context, "PersonalLeaveDays", ep_personalLeaveTv.getText().toString());

                    Map<String, Object> user = new HashMap<>();
                    user.put(MTYPE, mTypeSpinner.getSelectedItem().toString());
                    user.put(SDATE, ep_startingDateTv.getText().toString());
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
        try {
            if (sharedPreference.loadStringData(context, "StartingDate") != null){
                me.setStartService(df.parse(sharedPreference.loadStringData(context, "StartingDate")));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ep_startingDateTv.setText(df.format(me.getStartService()));

        ep_personalLeaveTv.setText(sharedPreference.loadStringData(context, "PersonalLeaveDays"));
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
