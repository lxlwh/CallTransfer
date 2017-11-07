package com.example.kevin.calltransfer;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

//    MyMissedCall mymissedcall;
    private IntentFilter intentfilter;
    private MyPhoneStateReceiver myphonestaterec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Button button_home1 = findViewById(R.id.text_test);
        Button button_home2 = findViewById(R.id.calllog_view);
        Button button_setting = findViewById(R.id.button2);

        button_home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CallLog_view.class);
                startActivity(intent);
            }
        });

//        button_home1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, Text_test.class);
//                startActivity(intent);
//            }
//        });

        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Mysetting.class);
                startActivity(intent);
            }
        });

        intentfilter = new IntentFilter();
        intentfilter.addAction("android.intent.action.PHONE_STATE");
        myphonestaterec = new MyPhoneStateReceiver();
        registerReceiver(myphonestaterec, intentfilter);

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
               Intent intent = new Intent(MainActivity.this,Text_test.class);
               startActivity(intent);

            }
            LastCallState = CurrentCallState;
        }
    }

}
