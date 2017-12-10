package com.example.kevin.calltransfer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Activity_server extends AppCompatActivity {

    private List<Servercontent> mysevercontent = new ArrayList<Servercontent>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.myserver);

        Button contentsend = (Button) findViewById(R.id.server_send);
        final EditText contentedit = findViewById(R.id.sever_tosent);

        final Handler handler = new Handler() {             //处理 连接服务器 返回的消息，，显示欢迎消息
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 1:
                        Toast.makeText(Activity_server.this,"Message sent",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        // 将服务器发送回来的消息进行保存，刷新listview
                        // 目前 并没有对时间进行更新
                        Servercontent testcontent1 = new Servercontent(msg.obj.toString(),"22:22 2017.12.10");
                        mysevercontent.add(testcontent1);
                        ContentAdapter adapter = new ContentAdapter(Activity_server.this,R.layout.severcontenttoshow, mysevercontent);
                        ListView listView = (ListView) findViewById(R.id.server_content);
                        listView.setAdapter(adapter);
                        break;
                }
            }
        };

        ClientThread clientthread = new ClientThread(handler);
        new Thread(clientthread).start();

        // 消息的发送处理，将获得的消息发送 到服务器：  118.126.111.157   端口：9999
        contentsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(Activity_server.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Activity_server.this, new String[]{Manifest.permission.INTERNET}, 1);
                } else {
                    String severcontent ;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Socket socket = new Socket("118.126.111.157",9999);
                                OutputStream os = socket.getOutputStream();
                                String st = contentedit.getText().toString();
                                os.write(st.getBytes("utf-8"));

                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);

                                os.close();           //关闭输出流
                                socket.close();            //关闭socket
                                contentedit.setText("");  //清空发送对话框
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });

        Servercontent testcontent1 = new Servercontent("this is a test","22:22 2017.12.10");
        mysevercontent.add(testcontent1);
        ContentAdapter adapter = new ContentAdapter(Activity_server.this,R.layout.severcontenttoshow, mysevercontent);
        ListView listView = (ListView) findViewById(R.id.server_content);
        listView.setAdapter(adapter);
    }

    public class Servercontent {

        private String smscontent;
        private String smstime;

        public Servercontent (String smscontent, String smstime) {
            this.smscontent = smscontent;
            this.smstime = smstime;
        }
        public String getSmscontent () {
            return smscontent;
        }

        public String getSmstime () {
            return smstime;
        }
    }

    public class ContentAdapter extends ArrayAdapter<Servercontent> {

        private int resourceId;

        public ContentAdapter (Context context, int textViewResourceId, List<Servercontent> objects){
            super (context, textViewResourceId, objects);
            resourceId = textViewResourceId;
        }

        public View getView (int poistion, View convertView, ViewGroup parent) {
            Servercontent SContent = getItem(poistion);
            View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            TextView SmsTime = (TextView) view.findViewById(R.id.smstime);
            TextView SmsContent = (TextView) view.findViewById(R.id.smscontent);
            SmsTime.setText(SContent.getSmstime());
            SmsContent.setText(SContent.getSmscontent());
            return view;
        }

    }
}
