package com.example.kevin.calltransfer;

import android.os.Looper;
import android.os.Message;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * Created by Kevin on 2017/12/10.
 */

public class ClientThread implements Runnable{
    private Socket s;
    private Handler handler;
    public Handler revHandler;
    BufferedReader br = null;
    OutputStream os = null;

    public ClientThread (Handler handler) {
        this.handler = handler;
    }

    public void run () {
        try {
            s = new Socket("118.126.111.157",9999);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = s.getOutputStream();

            new Thread () {
                @Override
                public void run() {
                    String content = null;
                    try {
                        while ((content = br.readLine())!= null) {
                            Message msg = new Message ();
                            msg.what = 2;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            Looper.prepare();
            Looper.loop();
        }  catch (SocketTimeoutException e1) {
            System.out.println("哎呀我去，网络连接超时！！");
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }
}
