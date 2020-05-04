package com.yujongu.socialserviceagent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.friends.response.model.AppFriendInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsListViewAdapter extends BaseAdapter {
    private List<AppFriendInfo> friendsList = new ArrayList<>();

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.friends_list_row, viewGroup, false);
        }

        CircleImageView thumbnailImg = view.findViewById(R.id.thumbnailIv);
        TextView friendNickName = view.findViewById(R.id.friendNameTv);
        Button friendListAddBtn = view.findViewById(R.id.friendsListAddBtn);

        AppFriendInfo item = friendsList.get(i);

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
                Toast.makeText(context, "Add Friend!!!!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


}
