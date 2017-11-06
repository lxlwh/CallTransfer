package com.example.kevin.calltransfer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Kevin on 2017/11/6.
 */

public class Mysetting extends AppCompatActivity {


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

            }
        });
    }
}
