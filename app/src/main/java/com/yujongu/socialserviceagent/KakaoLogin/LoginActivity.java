package com.yujongu.socialserviceagent.KakaoLogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.yujongu.socialserviceagent.MainActivity;
import com.yujongu.socialserviceagent.R;
import com.yujongu.socialserviceagent.SharedPreference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Context context = LoginActivity.this;

    private SessionCallback callback;

    private SharedPreference sharedPreference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    final static String TAG = "LoginActivityT";
    final static String KEY_ID = "UserId";
    final static String KEY_NAME = "Name";
    final static String KEY_IMAGE = "Image URL";
    final static String FRIENDSLIST = "Friends List";
    final static String NOTIFICATION = "Notifications";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        sharedPreference = new SharedPreference();
//        Log.i("key hash", Utility.getKeyHash(this));

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
        //check internet & login
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("Get Activity Result", "Activity result");
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            Log.e("Session Opened", "Session Successfully Opened");
//            redirectSignupActivity();  // 세션 연결성공 시 redirectSignupActivity() 호출
            requestMe();

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("Session Failed", "Session has failed to open");
            if (exception != null) {
                Logger.e(exception);
            }
            setContentView(R.layout.activity_login); // 세션 연결이 실패했을때
        }                                            // 로그인화면을 다시 불러옴
    }

    private void requestMe() {
        UserManagement.getInstance().me(new MeV2ResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onSuccess(MeV2Response response) {
                final String userId = response.getId() + "";
                final String nickname = response.getKakaoAccount().getProfile().getNickname() == null ?
                        "" : response.getKakaoAccount().getProfile().getNickname();
                final String url = response.getKakaoAccount().getProfile().getProfileImageUrl() == null ?
                        "" : response.getKakaoAccount().getProfile().getProfileImageUrl();
                //user info

                Map<String, Object> user = new HashMap<>();
                user.put(KEY_ID, userId);
                user.put(KEY_NAME, nickname);
                user.put(KEY_IMAGE, url);

                ArrayList<String> friendList = new ArrayList<>();
                user.put(FRIENDSLIST, friendList);

                ArrayList<String> notifs = new ArrayList<>();
                user.put(NOTIFICATION, notifs);

                if (sharedPreference.loadStringData(context, "UserId") == null){
                    saveUserToCloud(String.valueOf(response.getId()), user);
                } else if (!sharedPreference.loadStringData(context, "UserId").equals(userId)
                        || !sharedPreference.loadStringData(context, "ProfileName").equals(nickname)
                        || !sharedPreference.loadStringData(context, "ProfilePicUrl").equals(url)){
                    saveUserToCloud(String.valueOf(response.getId()), user);
                }



                sharedPreference.saveData(context, "UserId", userId);
                sharedPreference.saveData(context, "ProfileName", nickname);
                sharedPreference.saveData(context, "ProfilePicUrl", url);
                redirectMainActivity();
            }
        });
    }

    private void saveUserToCloud(String documentName, Map<String, Object> mapObj){
        db.collection("Users").document(documentName).set(mapObj, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Fail", Toast.LENGTH_LONG).show();
                Log.d(TAG, e.toString());
            }
        });
    }

    private void redirectMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
