package com.yujongu.socialserviceagent;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.friends.response.model.AppFriendInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListViewAdapter extends BaseAdapter {
    private List<AppFriendInfo> friendsList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private SharedPreference sharedPreference = new SharedPreference();

    private CircleImageView thumbnailImg;
    private TextView friendNickName;
    private Button friendListAddBtn;
    private TextView currStatusTv;

    String TAG = "FriendsListViewAdapterT";

    public FriendsListViewAdapter(List<AppFriendInfo> friendsList) {
        this.friendsList = friendsList;
    }

    @Override
    public int getCount() {
        return friendsList.size();
    }

    @Override
    public Object getItem(int i) {
        return friendsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friends_list_row, viewGroup, false);
        }

        thumbnailImg = view.findViewById(R.id.thumbnailIv);
        friendNickName = view.findViewById(R.id.friendNameTv);
        friendListAddBtn = view.findViewById(R.id.friendsListAddBtn);
        currStatusTv = view.findViewById(R.id.statusTv);

        final AppFriendInfo item = friendsList.get(i);

        if (item.getProfileThumbnailImage().equals("")){
            thumbnailImg.setImageResource(R.drawable.kakao_default_profile_image);
        } else {
            Picasso.get()
                    .load(item.getProfileThumbnailImage())
                    .into(thumbnailImg);
        }

        friendNickName.setText(item.getProfileNickname());

        friendListAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFriendRequestDataToCloud(String.valueOf(item.getId()), context, sharedPreference.loadStringData(context, "UserId"));

            }
        });

        return view;
    }

    private void addFriendRequestDataToCloud(String friendId, final Context context, String myId){
        DocumentReference myInfoRef = db.collection("Users").document(myId);
        myInfoRef.update("Request Lists", FieldValue.arrayUnion(friendId));
        DocumentReference friendInfoRef = db.collection("Users").document(friendId);
        friendInfoRef.update("Notifications", FieldValue.arrayUnion(myId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Request Sent!!", Toast.LENGTH_LONG).show();
                friendListAddBtn.setText("Request Sent!");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to send request...", Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });
    }

}
