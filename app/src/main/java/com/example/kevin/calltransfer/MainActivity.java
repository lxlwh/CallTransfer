package com.example.kevin.calltransfer;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/* 1. 权限申请  done
   2. 服务绑定  更新服务状态 done
   3. 短信转发  done


   4. 设置界面，内容确认后应该给提醒
 */



public class MainActivity extends AppCompatActivity {

    private IntentFilter intentfilter, intentfiler2;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_home2 = findViewById(R.id.calllog_view);
        Button button_setting = findViewById(R.id.button2);
        Button button_startCTSVC = findViewById(R.id.button_startcalltransfersevice);
        Button button_stopCTSVC =  findViewById(R.id.button_stopCTsvc);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentfiler2 = new IntentFilter();
        intentfiler2.addAction("com.example.kevin.svchintchange");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentfiler2);

        button_home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CallLog_view.class);
                startActivity(intent);
            }
        });


        button_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Mysetting.class);
                startActivity(intent);
            }
        });

        button_startCTSVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this,calltransfer.class);
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                } else {
                    startService(startIntent);
                }
            }
        });

        button_stopCTSVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, calltransfer.class);
                stopService(stopIntent);
                TextView textview_CT_SVC_hit = findViewById(R.id.textView_CT_svc_hint);
                textview_CT_SVC_hit.setText(R.string.textview_SVC_hint_stop);
            }
        });


    }


    protected void texthintchange(){
        TextView textview_CT_SVC_hit = findViewById(R.id.textView_CT_svc_hint);
        textview_CT_SVC_hit.setText(R.string.textview_SVC_hint_default);
        textview_CT_SVC_hit.setText(R.string.textview_SVC_hint_start);
    }
    class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent){
            texthintchange();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

}
