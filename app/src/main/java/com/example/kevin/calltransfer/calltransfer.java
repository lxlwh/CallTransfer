package com.example.kevin.calltransfer;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class calltransfer extends Service {

    private IntentFilter intentfilter, intentfilter2;
    private MyPhoneStateReceiver myphonestaterec;
    private LocalBroadcastManager localBroadcastManager;

    public calltransfer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("CallTransfer")
                .setContentText("CallTransfer is Running")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        startForeground(1, notification);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        Intent intent1 = new Intent("com.example.kevin.svchintchange");
        localBroadcastManager.sendBroadcast(intent1);
    }

    @Override
    public  int onStartCommand(Intent intent, int flags, int startId) {
        intentfilter = new IntentFilter();
        intentfilter.addAction("android.intent.action.PHONE_STATE");
        myphonestaterec = new MyPhoneStateReceiver();
        registerReceiver(myphonestaterec, intentfilter);
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        return svcflag;
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myphonestaterec);
    }



    private class MyPhoneStateReceiver extends PhoneStateReceiver {
        private   int LastCallState = TelephonyManager.CALL_STATE_IDLE;
        @Override
        public void onReceive (Context arg0, Intent arg1) {
            String action = arg1.getAction();
            TelephonyManager telephonyManager = (TelephonyManager)arg0.getSystemService(Context.TELEPHONY_SERVICE);
            int CurrentCallState = telephonyManager.getCallState();
            if (CurrentCallState == TelephonyManager.CALL_STATE_IDLE){

            }else if (CurrentCallState == TelephonyManager.CALL_STATE_RINGING) {
                String incomingphonenumber = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                SharedPreferences.Editor pref = getSharedPreferences("data",MODE_PRIVATE).edit();
                pref.putString("INCOMINGNUMBER",incomingphonenumber);
                pref.apply();


            }else if (CurrentCallState == TelephonyManager.CALL_STATE_OFFHOOK) {

            }
            if (LastCallState == TelephonyManager.CALL_STATE_RINGING && CurrentCallState == TelephonyManager.CALL_STATE_IDLE) {
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);

                    try {

                        String number = pref.getString("NUMBER","11");
                        String mycontent = "来电："+pref.getString("INCOMINGNUMBER","00")+pref.getString("MSM","22");
                        SmsManager manager = SmsManager.getDefault();
                        manager.sendTextMessage(number, null, mycontent, null, null);

                    } catch (Exception e){
//                        Toast.makeText(Text_test.this,"there is some problem here",Toast.LENGTH_SHORT).show();
                    }

            }
            LastCallState = CurrentCallState;
        }
    }

}
