package com.example.mannai.sos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.mannai.sos.dummy.DummyContent;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static com.example.mannai.sos.SMSPushService.ACTION_UPDATE_MSG;
import static com.example.mannai.sos.SMSPushService.KEY_STRING_FROM_SERVICE;

public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.i("SMSX","onReceive");
        String action = intent.getAction();
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){

            Log.i("SMSX","it is SMS");
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();

                        //sendNotificationx(context,msgBody);
                        Log.i("SMSX",msgBody);
                        Log.i("SMSX",msg_from);
                        String[] x = msgBody.split(",");

                        double lat = Double.parseDouble(x[0]);// 21.42251;
                        double lng = Double.parseDouble(x[1]);

                        String city = getAddress(context,lat,lng);
                        addRequest(context,lat,lng,msg_from,city);

                    }
                }catch(Exception e){
//                            Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }


    String getAddress(Context context,double lat, double lng){
        Log.i("SMSX","getAddress");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            Log.i("SMSX","try");
        } catch (IOException e){
            Log.i("SMSX","exception");
            addresses = null;

        }
        String city = "";
        if ( addresses != null) {

            Log.i("SMSX","not null");
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
             city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.i("SMSX","ADD "+city+" "+address+" "+state+" "+country+" "+postalCode+" "+knownName);
        }else

            Log.i("SMSX","null");

        return city;
    }

    void addRequest(final Context context,double lat,double lng, String id,String city){
        //loading = true;
        WSHttpClient client = new WSHttpClient(context.getApplicationContext());
        RequestParams params = new RequestParams();
        params.put("lat", lat);
        params.put("lng", lng);
        params.put("id", id);
        params.put("city", city);


        client.post("addHelp.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                Log.e("XX", "onFailure: " + throwable.getMessage() + " - " + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray search = null;
                DummyContent.DummyItem u[] = null;
                JSONArray l;
                Log.i("GFX","response "+response.toString());

                try {

                    String status = response.getString("status");
                    if ( status.equals("INVALID")){
                        //Toast.makeText(UserList.this, R.string.onfailure, Toast.LENGTH_LONG ).show();
                    }else{
                        Intent it = new Intent(context, SMSPushService.class);

                        it.putExtra("msg","X");

                        context.startService(it);

                    }



                }catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("CX",e.getMessage());
                }


            }

        });


    }


    private void sendNotificationx(Context context, String messageBody){
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, ItemListActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(0)
                        .setContentTitle("SOS")
                        .setContentText(messageBody);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
