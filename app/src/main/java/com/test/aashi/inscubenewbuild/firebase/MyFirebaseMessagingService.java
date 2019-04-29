package com.test.aashi.inscubenewbuild.firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.test.aashi.inscubenewbuild.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by ITSoftSupport on 17/05/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    JSONObject mData;


    public final void handleIntent(Intent intent) {
        try {
            if (intent.getExtras() != null) {
                RemoteMessage.Builder builder = new RemoteMessage.Builder("MyFirebaseMessagingService");

                for (String key : intent.getExtras().keySet()) {
                    builder.addData(key, intent.getExtras().get(key).toString());
                }

                onMessageReceived(builder.build());
            }

        } catch (Exception e) {
//            super.handleIntent(intent);
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
//        Log.d(TAG, "From: " + remoteMessage.getFrom());

/*
        Log.d("Test", "Notification:1 " + remoteMessage.getNotification().getBody());
        Log.d("Test", "Notification:2 " + remoteMessage.getData().toString());


*/
/*
        String TAG = "TEST";
        String payload = "";
        String notificationtitle = "";

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            payload = new Gson().toJson(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            notificationtitle = remoteMessage.getNotification().getBody();
        }


        sendNotification(payload, notificationtitle);
*/
        try {
            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            sendNotification(jsonObject.toString(), remoteMessage.getNotification().getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(String payload, String notificationtitle) {

        String mTitle = notificationtitle;
        String mID = "";
        String mType = "";
        int id = 1;
        Notification notification;
        try {
        mData = new JSONObject(payload);
            mType = mData.optString("type");
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent intent = new Intent(this, com.test.aashi.inscubenewbuild.Notification.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                    getString(R.string.default_notification_channel_id))
                    .setSmallIcon(R.drawable.logo)
                    .setBadgeIconType(R.drawable.logo)
                    .setContentTitle(mType)
                    .setContentText(mTitle)
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notification = notificationBuilder.build();
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(getRequestCode(), notification);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private static int getRequestCode()
    {
        Random rnd = new Random();
        return 100 + rnd.nextInt(900000);
    }
}
