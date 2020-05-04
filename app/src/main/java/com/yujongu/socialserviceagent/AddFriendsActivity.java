package com.yujongu.socialserviceagent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;

import java.util.ArrayList;
import java.util.List;

public class AddFriendsActivity extends AppCompatActivity {

    List<AppFriendInfo> list = new ArrayList<>();

    ListView friendsListview;
    FriendsListViewAdapter friendsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        initInstances();
        eventListeners();

        findFriends();
    }

    private void initInstances(){
        friendsListview = findViewById(R.id.friendsListView);
        friendsAdapter = new FriendsListViewAdapter(list);
    }

    private void eventListeners(){
        friendsListview.setAdapter(friendsAdapter);
    }
    private void findFriends(){
        AppFriendContext context = new AppFriendContext(AppFriendOrder.NICKNAME, 0, 100, "asc");

        KakaoTalkService.getInstance()
                .requestAppFriends(context, new TalkResponseCallback<AppFriendsResponse>() {
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
                        list.addAll(result.getFriends());

                        friendsAdapter.notifyDataSetChanged();

                    }
                });
    }
}
