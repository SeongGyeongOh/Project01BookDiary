package com.osg.project01bookdiary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title;
    String message;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i("RECEIVE", "push received");

        Map<String, String> datas = remoteMessage.getData();
        if(datas!=null){
            title=datas.get("title");
            message=datas.get("message");

            Log.i("MSG", title+message);
        }

        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel("ch01", "Channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder=new NotificationCompat.Builder(this, "ch01");
        }else{
            builder=new NotificationCompat.Builder(this, null);
        }
        builder.setSmallIcon(R.drawable.kakaoaccount_icon).setContentTitle(title).setContentText(message);

        Notification notification=builder.build();
        manager.notify(100, notification);


    }
}
