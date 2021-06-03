package com.yujongu.socialserviceagent;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NotificationDialog extends Dialog {

    ListView friendReqList;
    ArrayList<String> notifArray = new ArrayList<>();
    NotificationListAdapter friendReqAdapter;
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
        sharedPreference = new SharedPreference();
        friendReqList = findViewById(R.id.dialogListView);
        friendReqAdapter = new NotificationListAdapter(notifArray);
        friendReqList.setAdapter(friendReqAdapter);

    }


    private void retrieveData(){
//        DocumentReference myInfoRef = db.collection("Users").document(sharedPreference.loadStringData(context, "UserId"));
//        myInfoRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isComplete()){
//                    DocumentSnapshot doc = task.getResult();
//                    if (doc != null){
//                        if (doc.get("Notifications") != null){
//                            List<String> pairList = (List<String>) doc.get("Notifications");
//                            notifArray.addAll(pairList);
//                            friendReqAdapter.notifyDataSetChanged();
//
//                        }
//                    } else {
//                        Log.d(TAG, "No Document Found");
//                    }
//                } else {
//                    Log.d(TAG, "get failed with ", task.getException());
//                }
//            }
//        });
    }
}
