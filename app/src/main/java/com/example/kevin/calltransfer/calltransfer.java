package com.example.kevin.calltransfer;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class calltransfer extends Service {

    private IntentFilter intentfilter;
    private MyPhoneStateReceiver myphonestaterec;

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

//                if(ContextCompat.checkSelfPermission(calltransfer.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
//                }else {
                    try {
/*                      Uri uri = Uri.parse("smsto:13752123953");
                        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                        intent.putExtra("sms_body","You got a missed call");
                        startActivity(intent);  */

                        String number = pref.getString("NUMBER","11");
                        String mycontent = "来电："+pref.getString("INCOMINGNUMBER","00")+pref.getString("MSM","22");
                        SmsManager manager = SmsManager.getDefault();
                        manager.sendTextMessage(number, null, mycontent, null, null);
//                        Toast.makeText(Text_test.this,R.string.hint_sendtexted,Toast.LENGTH_SHORT).show();

//                        TimerTask backtask = new TimerTask() {
//                            @Override
//                            public void run() {
//                                Intent intent = new Intent(Text_test.this,MainActivity.class);
//                                startActivity(intent);
//                            }
//                        }
//                        Timer backtimer = new Timer();
//                        backtimer.schedule(backtask, 3000);

                    } catch (Exception e){
//                        Toast.makeText(Text_test.this,"there is some problem here",Toast.LENGTH_SHORT).show();
                    }
//                }

            }
            LastCallState = CurrentCallState;
        }
    }

}
