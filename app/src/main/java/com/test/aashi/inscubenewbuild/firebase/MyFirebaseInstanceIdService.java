package com.test.aashi.inscubenewbuild.firebase;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ITSoftSupport on 17/05/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "FCM Service";
    SharedPreferences sharedPreferences;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        sharedPreferences = getSharedPreferences("firebase", MODE_PRIVATE);

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        if (refreshedToken != null) {
            SharedPreferences.Editor e = sharedPreferences.edit();
            e.putString("refreshToken", refreshedToken);
            e.commit();
        }
        // TODO: Implement this method to send any registration to your app's servers.
//        sendRegistrationToServer(refreshedToken);
    }

    /*private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }*/
}
