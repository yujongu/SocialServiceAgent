package com.yujongu.socialserviceagent;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NotificationDialog extends Dialog {

    ListView friendReqList;
    ArrayList<String> notifArray = new ArrayList<>();
    NotificationListAdapter friendReqAdapter;
    FirebaseFirestore db;
    SharedPreference sharedPreference;
    Context context;

    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;


    String TAG = "NotificationDialogT";
    public NotificationDialog(@NonNull Context context) {
        super(context);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.dialog_notifications);
        initInstances();
        retrieveData();





    }

    private void initInstances(){
        context = getContext();
        db = FirebaseFirestore.getInstance();
        sharedPreference = new SharedPreference();
        friendReqList = findViewById(R.id.dialogListView);
        friendReqAdapter = new NotificationListAdapter(notifArray);
        friendReqList.setAdapter(friendReqAdapter);

    }


    private void retrieveData(){
        DocumentReference myInfoRef = db.collection("Users").document(sharedPreference.loadStringData(context, "UserId"));
        myInfoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isComplete()){
                    DocumentSnapshot doc = task.getResult();
                    if (doc != null){
                        if (doc.get("Notifications") != null){
                            List<String> pairList = (List<String>) doc.get("Notifications");
                            notifArray.addAll(pairList);
                            friendReqAdapter.notifyDataSetChanged();

                        }
                    } else {
                        Log.d(TAG, "No Document Found");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
