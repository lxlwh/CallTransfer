package com.example.kevin.calltransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

//    MyMissedCall mymissedcall;
    private IntentFilter intentfilter;
    private MyPhoneStateReceiver myphonestaterec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_home1 = findViewById(R.id.text_test);
        Button button_home2 = findViewById(R.id.calllog_view);
        Button button_calltest = findViewById(R.id.button_calltest);

        button_home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CallLog_view.class);
                startActivity(intent);
            }
        });

        button_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Text_test.class);
                startActivity(intent);
            }
        });

        intentfilter = new IntentFilter();
        intentfilter.addAction("android.intent.action.PHONE_STATE");
        myphonestaterec = new MyPhoneStateReceiver();
        registerReceiver(myphonestaterec, intentfilter);

 /*       final IntentFilter Misscallfilter = new IntentFilter();
        Misscallfilter.addAction("com.android.phone.NotificationMgr.MissedCall_intent");
        mymissedcall = new MyMissedCall();
        registerReceiver(mymissedcall, Misscallfilter);

        button_calltest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.example.kevin.calltransfer.MYI");
                sendBroadcast(intent);
            }
        });*/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myphonestaterec);
    }

    class MyPhoneStateReceiver extends PhoneStateReceiver {
        private   int LastCallState = TelephonyManager.CALL_STATE_IDLE;
        @Override
        public void onReceive (Context arg0, Intent arg1) {
            String action = arg1.getAction();
            Log.d("PhoneStateReceiver", action);
            TelephonyManager telephonyManager = (TelephonyManager)arg0.getSystemService(Context.TELEPHONY_SERVICE);
            int CurrentCallState = telephonyManager.getCallState();
            Log.d("PhoneStateReceiver","currentCallState=" + CurrentCallState);
            if (CurrentCallState == TelephonyManager.CALL_STATE_IDLE){

            }else if (CurrentCallState == TelephonyManager.CALL_STATE_RINGING) {

            }else if (CurrentCallState == TelephonyManager.CALL_STATE_OFFHOOK) {

            }
            if (LastCallState == TelephonyManager.CALL_STATE_RINGING && CurrentCallState == TelephonyManager.CALL_STATE_IDLE) {
//                Toast.makeText(arg0, "You are awesome", Toast.LENGTH_LONG).show();
               Intent intent = new Intent(MainActivity.this,Text_test.class);
               startActivity(intent);

            }
            LastCallState = CurrentCallState;
        }
    }

}

/*class MyMissedCall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, "You got a missed call", Toast.LENGTH_LONG).show();
    }
}*/