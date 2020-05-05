package com.yujongu.socialserviceagent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationListAdapter extends BaseAdapter {


    private SharedPreference sharedPreference;
    private FirebaseFirestore db;
    Context context;
    ArrayList<String> list;
    String TAG = "NotificationListAdapterT";

    public NotificationListAdapter(ArrayList<String> list) {
        this.list = list;
        this.sharedPreference = new SharedPreference();
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        context = viewGroup.getContext();
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dialog_notification_row, viewGroup, false);
        }

        final CircleImageView profImg = view.findViewById(R.id.notifDialogProfImg);
        final TextView profName = view.findViewById(R.id.notifDialogProfName);
        ImageButton confirm = view.findViewById(R.id.btnAcceptFriend);
        ImageButton decline = view.findViewById(R.id.btnDeclineFriend);

        final String userId = list.get(i);

        AppFriendContext appFriendContext = new AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc");
        KakaoTalkService.getInstance()
                .requestAppFriends(appFriendContext, new TalkResponseCallback<AppFriendsResponse>() {
                    @Override
                    public void onNotKakaoTalkUser() {
                        Log.e("KAKAO_API", "카카오톡 사용자가 아님");
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("KAKAO_API", "친구 조회 실패: " + errorResult);
                    }

                    @Override
                    public void onSuccess(AppFriendsResponse result) {
                        Log.i("KAKAO_API", "친구 조회 성공");

                        for (AppFriendInfo info : result.getFriends()){
                            if (String.valueOf(info.getId()).equals(userId)){

                                if (info.getProfileThumbnailImage().equals("")){
                                    profImg.setImageResource(R.drawable.kakao_default_profile_image);
                                } else {
                                    Picasso.get()
                                            .load(info.getProfileThumbnailImage())
                                            .into(profImg);
                                }
                                profName.setText(info.getProfileNickname());
                            }
                        }
                    }
                });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriendIdtoFriendList(userId);
                removeFriendIdfromRequestList(userId);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFriendIdfromRequestList(userId);
            }
        });



        return view;
    }

    private void addFriendIdtoFriendList(String newFriend){
        DocumentReference myInfoRef = db.collection("Users").document(sharedPreference.loadStringData(context, "UserId"));
        myInfoRef.update("Friends List", FieldValue.arrayUnion(newFriend)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Friend Added!!", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to add friend up on cloud", Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });

    }

    private void removeFriendIdfromRequestList(String reqFriendId){
        DocumentReference myInfoRef = db.collection("Users").document(sharedPreference.loadStringData(context, "UserId"));
        myInfoRef.update("Notifications", FieldValue.arrayRemove(reqFriendId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Successfully updated the leave to cloud", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to update the leave to cloud", Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });
    }
}
