package alaaMeaad.calllogs.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import alaaMeaad.calllogs.MainActivity;
import alaaMeaad.calllogs.MyBroadCastReceiver;
import alaaMeaad.calllogs.R;
import alaaMeaad.calllogs.api.ApiServers;

import static alaaMeaad.calllogs.App.CHINNEL_ID;
import static alaaMeaad.calllogs.api.ApiClient.getClient;


public class Foregroungservice extends Service {


    Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 60*60*1000 - SystemClock.elapsedRealtime()%1000);
//             whatever you want to do below


            Notification notification = new NotificationCompat
                    .Builder(MainActivity.mainActivity,CHINNEL_ID)
                    .setContentTitle("Example Service")
                    .setContentText(MainActivity.mainActivity.sb)
                    .build();
            startForeground(1,notification);
            if(ContextCompat.checkSelfPermission(MainActivity.mainActivity,
                    Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.mainActivity,
                        Manifest.permission.READ_CALL_LOG)){
                    ActivityCompat.requestPermissions(MainActivity.mainActivity,
                            new String[]{Manifest.permission.READ_CALL_LOG} , 1);
                }else {
                    ActivityCompat.requestPermissions(MainActivity.mainActivity,
                            new String[]{Manifest.permission.READ_CALL_LOG} , 1);
                }
            }else  {
                MainActivity.mainActivity.textView = MainActivity.mainActivity.findViewById(R.id.textview);
                MainActivity.mainActivity.textView.setText(MainActivity.mainActivity.getCallDetails1());
            }
            MainActivity.mainActivity.apiServers = getClient().create(ApiServers.class);


            MainActivity.mainActivity.alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            MainActivity.mainActivity.startAlarm();
            MainActivity.mainActivity.button = MainActivity.mainActivity.findViewById(R.id.btn_send_data);
            MainActivity.mainActivity.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.mainActivity.allFilds();
                }
            });

        }
    };
    PowerManager pm;
    PowerManager.WakeLock wl;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate() {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GpsTrackerWakelock");
        wl.acquire(60*60*1000L /*60 minutes*/);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(periodicUpdate);
        return START_STICKY;
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent
//                .getActivity(this,0,notificationIntent,flags);
//
//
//        Notification notification = new NotificationCompat
//                .Builder(this,CHINNEL_ID)
//                .setContentTitle("Example Service")
//                .setContentText(MainActivity.mainActivity.sb)
//                .setContentIntent(pendingIntent)
//                .build();
//        startForeground(1,notification);
//        if(ContextCompat.checkSelfPermission(MainActivity.mainActivity,
//                Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.mainActivity,
//                    Manifest.permission.READ_CALL_LOG)){
//                ActivityCompat.requestPermissions(MainActivity.mainActivity,
//                        new String[]{Manifest.permission.READ_CALL_LOG} , 1);
//            }else {
//                ActivityCompat.requestPermissions(MainActivity.mainActivity,
//                        new String[]{Manifest.permission.READ_CALL_LOG} , 1);
//            }
//        }else  {
//            MainActivity.mainActivity.textView = MainActivity.mainActivity.findViewById(R.id.textview);
//            MainActivity.mainActivity.textView.setText(MainActivity.mainActivity.getCallDetails1());
//        }
//        MainActivity.mainActivity.apiServers = getClient().create(ApiServers.class);
//
//
//        MainActivity.mainActivity.alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        Intent alarmIntent = new Intent(this, MyBroadCastReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//        MainActivity.mainActivity.startAlarm();
//        MainActivity.mainActivity.button = MainActivity.mainActivity.findViewById(R.id.btn_send_data);
//        MainActivity.mainActivity.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.mainActivity.allFilds();
//            }
//        });

//        return  START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wl.release();
    }
}
