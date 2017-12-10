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
import android.os.Message;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* 1. 权限申请  done
   2. 服务绑定  更新服务状态 done
   3. 短信转发  done


   4. 设置界面，内容确认后应该给提醒
 */



public class MainActivity extends AppCompatActivity {

    private IntentFilter intentfilter, intentfiler2;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    private Handler mMainHandler;      //在主线程进行网络连接，主线程不运行直接进行网络连接
    private ExecutorService mThreadPool;  //为了方便展示,此处直接采用线程池进行线程管理,而没有一个个开线程


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_home2 = findViewById(R.id.calllog_view);
        Button button_setting = findViewById(R.id.button2);
        Button button_startCTSVC = findViewById(R.id.button_startcalltransfersevice);
        Button button_stopCTSVC =  findViewById(R.id.button_stopCTsvc);
        Button button_consever = findViewById(R.id.button_connectsever);
        Button button_myserver = findViewById(R.id.button_server);

        mThreadPool = Executors.newCachedThreadPool();      //初始化线程池

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentfiler2 = new IntentFilter();
        intentfiler2.addAction("com.example.kevin.svchintchange");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentfiler2);

        final Handler handler = new Handler() {             //处理 连接服务器 返回的消息，，显示欢迎消息
            public void handleMessage (Message msg) {
 //               String concontent = (String) msg.obj;
                Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
            }
        };

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

        button_myserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(MainActivity.this, Activity_server.class);
                startActivity(intent);
            }
        });

        button_consever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    String severcontent ;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Socket socket = null;
                            try {
                                socket = new Socket("118.126.111.157",9999);
                                InputStream inputstream = socket.getInputStream();
                                InputStreamReader isr = new InputStreamReader(inputstream);
                                BufferedReader br = new BufferedReader(isr);

                                Message message = new Message();
                                message.obj = br.readLine();
                                handler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
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
