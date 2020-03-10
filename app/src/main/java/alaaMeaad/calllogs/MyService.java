package alaaMeaad.calllogs;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class MyService extends IntentService {


    public MyService() {
        super("MyService");
    }
    Context context;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 26);
        ComponentName receiver = new ComponentName(context, MyBroadCastReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        Intent alarmIntent = new Intent(this, MyBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, 0, 1000, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),  1000*60 , pendingIntent);
    }
}

