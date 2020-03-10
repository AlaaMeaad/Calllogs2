package alaaMeaad.calllogs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            Intent serviceIntent = new Intent(context, MyService.class);
            context.startService(serviceIntent);
            Toast.makeText(context.getApplicationContext(), "Alarm Manager just rannn", Toast.LENGTH_LONG).show();

        } else {
            MainActivity.mainActivity.allFilds();

            Toast.makeText(context.getApplicationContext(), "Alarm Manager just ran", Toast.LENGTH_LONG).show();
            Log.e("sssdddasd", "onReceive: " );





        }

    }
}

