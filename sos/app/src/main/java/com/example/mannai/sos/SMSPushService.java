package com.example.mannai.sos;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;

import java.util.Random;

/**
 * Created by Mannai on 8/2/2018.
 */

public class SMSPushService extends IntentService {

    //from MyIntentService to MainActivity
    final static String KEY_INT_FROM_SERVICE = "KEY_INT_FROM_SERVICE";
    final static String KEY_STRING_FROM_SERVICE = "KEY_STRING_FROM_SERVICE";
    final static String ACTION_UPDATE_CNT = "UPDATE_CNT";
    final static String ACTION_UPDATE_MSG = "UPDATE_MSG";

    //from MainActivity to MyIntentService
    final static String KEY_MSG_TO_SERVICE = "KEY_MSG_TO_SERVICE";
    final static String ACTION_MSG_TO_SERVICE = "MSG_TO_SERVICE";

    SmsListener myServiceReceiver;
    int cnt;

    public SMSPushService() {
        super("SMSPushService");
    }

    @Override
    public void onCreate() {
        myServiceReceiver = new SmsListener();
        super.onCreate();
    }

    /*

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(getApplicationContext(),  "onStartCommand", Toast.LENGTH_LONG).show();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MSG_TO_SERVICE);
        registerReceiver(myServiceReceiver, intentFilter);

        return super.onStartCommand(intent, flags, startId);
    }

    */
    @Override
    public void onDestroy() {
        //Toast.makeText(getApplicationContext(), "onDestroy", Toast.LENGTH_LONG).show();
        //unregisterReceiver(myServiceReceiver);
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        Log.i("SMSX","onHandleIntent");
        Bundle extras = intent.getExtras();

        sendNotificationx(this.getApplicationContext(),extras.getString("msg"));
        //sendNotification(extras.getString("msg"));


    }

    private void sendNotificationx(Context context, String messageBody){
        messageBody = "New patient";
        Log.i("SMSX","sendNotificationx "+messageBody);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context.getApplicationContext(), ItemListActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(),"notify_001")
                        .setSmallIcon(R.drawable.cast_ic_mini_controller_pause_large)
                        .setContentTitle("SOS")
                        .setContentText(messageBody);
        mBuilder.setContentIntent(contentIntent);
        //mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        mBuilder.setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        Log.i("SMSX","notify "+messageBody);
        mNotificationManager.notify(1, mBuilder.build());
    }


    void sendNotification( String msg){

        Random random = new Random();
        int notifyID = random.nextInt(9999 - 1000) + 1000;
        Intent resultIntent = new Intent(this, ItemListActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
         resultIntent.putExtra("msg", msg);
        resultIntent.putExtra("cur", "notification");

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);




        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_ONE_SHOT);
        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("illusion")
                .setSmallIcon(R.drawable.cast_ic_mini_controller_pause)
                .setContentIntent(resultPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE |Notification.DEFAULT_SOUND )
                .setContentText(msg)
                .setAutoCancel(true);
                //   .setGroupSummary(true)
              //  .setGroup("x")
                // .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.i))
                //.setDeleteIntent(onCancelNotificationReceiverPendingIntent);
        Notification notification = mNotifyBuilder.build();

        mNotificationManager.notify(notifyID, notification);
    }

    /*

    public class MyServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if(action.equals(ACTION_MSG_TO_SERVICE)){
                String msg = intent.getStringExtra(KEY_MSG_TO_SERVICE);

                msg = new StringBuilder(msg).reverse().toString();

                //send back to MainActivity
                Intent i = new Intent();
                i.setAction(ACTION_UPDATE_MSG);
                i.putExtra(KEY_STRING_FROM_SERVICE, msg);
                sendBroadcast(i);
            }
        }
    }

*/

}
