package com.google.sanjoy.notifyme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button_notify;

    //STEP 3 -
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";

    private NotificationManager mNotifyManager;

    //STEP 7 -
    private static final int NOTIFICATION_ID = 0;

    //STEP A -
    private Button button_cancel;
    private Button button_update;

    //STEP III -
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION";

    private NotificationReceiver mReceiver = new NotificationReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //STEP 1 -
        button_notify = findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

    //STEP 8 - calling the method- if we don't call then app crashes
    createNotificationChannel();

    //STEP B -
        button_update = findViewById(R.id.update);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update the notification
                updateNotification();
            }
        });

        button_cancel = findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cancel the notification
                cancelNotification();
            }
        });

        //step last - button enable / disable
        setNotificationButtonState(true, false, false);

        //STEP IV -
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));
    }

    //STEP 2 -
    public void sendNotification() {

        //step VI -
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        //STEP 10
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        //step  VII (in middle)
        notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent);

        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        setNotificationButtonState(false, true, true);



    }

    //STEP  4 -
    public void createNotificationChannel() {
        //using the NotifyManager class object
        mNotifyManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //check if - version sdk
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            //STEP 5 -
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager
                    .IMPORTANCE_HIGH);

            //STEP 6 -
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    //STEP 9 -
    private NotificationCompat.Builder getNotificationBuilder(){

        //STEP 11 -
        Intent notificationIntent = new Intent(this, MainActivity.class);

        //STEP 12 -
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)  //STEP 13 -
                .setAutoCancel(true);

        return notifyBuilder;
    }

    //STEP C - methods

    public void updateNotification() {
        //STEP F -
        Bitmap androidImage = BitmapFactory
                .decodeResource(getResources(),R.drawable.mascot_1);

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));

        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());


        //enable/disable button
        setNotificationButtonState(false, false, true);

    }

    public void cancelNotification() {

        //STEP D -
        mNotifyManager.cancel(NOTIFICATION_ID);

        // enable / disable button
        setNotificationButtonState(true, false, false);


    }

    // STEP LAST - extra - to enable / disable buttons
    void setNotificationButtonState(Boolean isNotifyEnabled,
                                    Boolean isUpdateEnabled,
                                    Boolean isCancelEnabled) {
        button_notify.setEnabled(isNotifyEnabled);
        button_update.setEnabled(isUpdateEnabled);
        button_cancel.setEnabled(isCancelEnabled);
    }

    //TASK 3 - roman no. steps
    //STEP I -
    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // Update the notification

            //step II -
            updateNotification();
        }
    }

    // STEP V -
    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

}