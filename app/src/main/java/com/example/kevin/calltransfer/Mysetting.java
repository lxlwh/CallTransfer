package com.example.kevin.calltransfer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Kevin on 2017/11/6.
 */

public class Mysetting extends AppCompatActivity {

    private String st1 = "11";
    private String st2 = "22";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        TextView checkinputnumber =  findViewById(R.id.textView2);
        TextView checkinputmsm =  findViewById(R.id.textView3);
        String phonenumber = checkinputnumber.getText().toString();
        String msmtosent = checkinputmsm.getText().toString();
        String number = pref.getString("NUMBER","11");
        String msmtoshow = pref.getString("MSM","22");
        if (number.equals(st1)){
            checkinputnumber.setText("没有设定的号码");
        }else{
            String oldnumber = "已经设定的号码是："+number;
            checkinputnumber.setText(oldnumber.toCharArray(),0,oldnumber.length());
        }
        if (msmtoshow.equals(st2)){
            checkinputmsm.setText("没有添加的内容");
        }else {
            String oldmsmtoshow = "已经添加的内容："+msmtoshow;
            checkinputmsm.setText(oldmsmtoshow.toCharArray(),0,oldmsmtoshow.length());
        }

        Button button_ok = (Button) findViewById(R.id.button_set_confirm);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText inputnumber = (EditText) findViewById(R.id.editText3);
                EditText inputmsm = (EditText) findViewById(R.id.editText4);
                String phonenumber = inputnumber.getText().toString();
                String msmtosent = inputmsm.getText().toString();
                SharedPreferences.Editor pref = getSharedPreferences("data", MODE_PRIVATE).edit();
                pref.putString("NUMBER",phonenumber);
                pref.putString("MSM",msmtosent);
                pref.apply();

                Intent intent = new Intent(Mysetting.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }
}
