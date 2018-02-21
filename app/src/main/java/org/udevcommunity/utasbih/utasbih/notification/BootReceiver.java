package org.udevcommunity.utasbih.utasbih.notification;

        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;

        import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        {
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.HOUR_OF_DAY, 9);
            cal.set(Calendar.MINUTE, 00);
            cal.set(Calendar.SECOND, 0);
            //
            Intent intet = new Intent(context, AlarmReceiver.class);
            PendingIntent pendintet = PendingIntent.getBroadcast(context, 0, intet, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager mn = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            //TODO put the " 1000*60*((60*3)+45)" into a config Class
            mn.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),  1000*60*((60*3)+45), pendintet);
        }
    }
}
