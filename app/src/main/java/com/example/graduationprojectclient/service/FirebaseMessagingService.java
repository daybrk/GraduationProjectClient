package com.example.graduationprojectclient.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.graduationprojectclient.MainActivity;
import com.example.graduationprojectclient.R;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //TODO: Выкидывает если уведомление приходит, когда юзер не залогинелся
        try {
            if (!CommunicationWithServerService.getROLE().equals("USER")) {
                String[] inspectorEmail = remoteMessage.getNotification().getTitle().split("-");
                if (inspectorEmail[1].equals(CommunicationWithServerService.getEMAIL())) {
                    sendNotification(remoteMessage.getNotification().getBody(), inspectorEmail);
                }
            } else {
                System.out.println("");
            }
        } catch (Exception e) {
            System.out.println("");
        }
    }



    private void sendNotification(String messageBody, String[] inspectorEmail) {

        Log.i("TAGTAGTAGTAGTAG", "TAGTAGTAGTAGTAG");
        Intent intent = new Intent(this, MainActivity.class);
        @SuppressLint("UnspecifiedImmutableFlag")
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        String channelId = "server-client-notify";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_baseline_delete_sweep_24)
                        .setContentText(messageBody)
                        .setContentTitle(inspectorEmail[0])
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

}
