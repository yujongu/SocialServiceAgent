package com.yujongu.socialserviceagent.KakaoLogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.Utility;
import com.kakao.util.helper.log.Logger;
import com.yujongu.socialserviceagent.MainActivity;
import com.yujongu.socialserviceagent.R;
import com.yujongu.socialserviceagent.SharedPreference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback callback;

    private SharedPreference sharedPreference;



    final static String TAG = "LoginActivityT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        callback = new SessionCallback();
        sharedPreference = new SharedPreference();
//        Log.i("key hash", Utility.getKeyHash(this));

        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();
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
                final String nickname = response.getKakaoAccount().getProfile().getNickname() == null ?
                        "" : response.getKakaoAccount().getProfile().getNickname();
                final String url = response.getKakaoAccount().getProfile().getProfileImageUrl() == null ?
                        "" : response.getKakaoAccount().getProfile().getProfileImageUrl();

                sharedPreference.saveData(LoginActivity.this, "ProfileName", nickname);
                sharedPreference.saveData(LoginActivity.this, "ProfilePicUrl", url);
                redirectMainActivity();
            }
        });

    }

    private void redirectMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }



}
