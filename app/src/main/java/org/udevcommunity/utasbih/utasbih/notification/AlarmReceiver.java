package org.udevcommunity.utasbih.utasbih.notification;

        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.media.RingtoneManager;
        import android.net.Uri;
        import android.support.v4.app.NotificationCompat;

        import org.udevcommunity.utasbih.utasbih.MainActivity;
        import org.udevcommunity.utasbih.utasbih.R;


public class AlarmReceiver extends BroadcastReceiver {


    public static int not_id =2156538;
    NotificationCompat.Builder mNotifyBuilder=null;
    NotificationManager notificationManager =null;

    public final String PREF_NAME = "UTasbihPref";

    SharedPreferences settings;

    @Override
    public void onReceive(Context context, Intent intent) {
        settings = context.getSharedPreferences(PREF_NAME, 0);
        if (settings.getBoolean("notification",false))
        {
            Startnotification(context);
        }
    }

    public void Startnotification(Context context)
    {
        long when = System.currentTimeMillis();
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context,MainActivity.class);
        PendingIntent penditent =PendingIntent.getActivity(context,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent.getBroadcast(context,0,notificationIntent,0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mNotifyBuilder = new NotificationCompat.Builder(
                context);
        //TODO put following configurables into a config Class
        mNotifyBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mNotifyBuilder.setTicker("UTasbih");
        mNotifyBuilder.setContentTitle("UTasbih");
        mNotifyBuilder.setContentText("تذكير: هل سبحت اليوم");
        mNotifyBuilder.setSound(alarmSound);
        mNotifyBuilder.setAutoCancel(true);
        mNotifyBuilder.setWhen(when);
        mNotifyBuilder.setContentIntent(penditent);
        mNotifyBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        notificationManager.notify(not_id, mNotifyBuilder.build());
    }

    public void Stopnotification()
    {
        notificationManager.cancel(not_id);
    }

}

