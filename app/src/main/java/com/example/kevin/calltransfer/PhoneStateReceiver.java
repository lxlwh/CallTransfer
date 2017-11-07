package com.example.kevin.calltransfer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Kevin on 2017/11/5.
 */

    public class PhoneStateReceiver extends BroadcastReceiver {
        private  static int LastCallState = TelephonyManager.CALL_STATE_IDLE;
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
//                Toast.makeText(arg0, "You missed a phone call", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(PhoneStateReceiver.this,Text_test.class);

            }
            LastCallState = CurrentCallState;
        }
    }
