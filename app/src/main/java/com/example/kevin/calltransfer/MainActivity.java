package com.example.kevin.calltransfer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
//import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Map<String,String>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView callLogShow = (ListView) findViewById(R.id.callloglistview);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CALL_LOG},1);
        }else {
            datalist = getDataList();
            SimpleAdapter adapter = new SimpleAdapter(this, datalist, R.layout.calllog_mylistview,
                    new String[]{"name", "number", "date", "duration", "type"},
                    new int[]{R.id.C_name, R.id.C_number, R.id.C_date, R.id.C_duration, R.id.C_type});
            callLogShow.setAdapter(adapter);
        }

    }

    private List<Map<String,String>> getDataList() {
       List<Map<String,String>> MLIST= new ArrayList<Map<String, String>>();;
        try{
            ContentResolver resolver = getContentResolver();
            Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI,
                    new String[]{CallLog.Calls.CACHED_NAME,
                            CallLog.Calls.NUMBER,
                            CallLog.Calls.DATE,
                            CallLog.Calls.DURATION,
                            CallLog.Calls.TYPE}, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
                String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(dateLong));
                int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                String typeString = "";
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        typeString = "打入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        typeString = "打出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        typeString = "未接";
                        break;
                    default:
                        break;
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", (name == null) ? "未备注联系人" : name);
                map.put("number", number);
                map.put("date", date);
                map.put("duration", (duration / 60) + "分钟");
                map.put("type", typeString);
                MLIST.add(map);
//                MLIST = list;
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }

        return MLIST;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    datalist = getDataList();
                }else {
                    Toast.makeText(this,R.string.calllog_per_request_dey,Toast.LENGTH_SHORT).show();
                }
        }
    }
}
