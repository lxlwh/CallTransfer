package com.example.kevin.calltransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Kevin on 2017/11/4.
 */

public class Text_test extends AppCompatActivity {

    @Override

    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_test);

//        Button button = (Button) findViewById(R.id.button_sendtext);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);

                if(ContextCompat.checkSelfPermission(Text_test.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(Text_test.this, new String[]{Manifest.permission.SEND_SMS}, 1);
                }else {
                    try {
/*                      Uri uri = Uri.parse("smsto:13752123953");
                        Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                        intent.putExtra("sms_body","You got a missed call");
                        startActivity(intent);  */

                        String number = pref.getString("NUMBER","11");
                        String mycontent = pref.getString("MSM","22");
                        SmsManager manager = SmsManager.getDefault();
                        manager.sendTextMessage(number, null, mycontent, null, null);
                    } catch (Exception e){
                        Toast.makeText(Text_test.this,"there is some problem here",Toast.LENGTH_SHORT).show();
                    }
                }

//            }
//        });
    }
}
