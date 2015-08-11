package com.holypasta.trainer.receiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.JesusSaves;

import java.util.Calendar;

public class Reminder extends BroadcastReceiver implements Constants {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("!!! broadcast Reminder");
        if (!lastVisitToday(context)) {
            sendNotification(context);
        }
    }

    private boolean lastVisitToday(Context context) {
        long time = JesusSaves.getInstance(context).getVisit();
        Calendar calendarSaved = Calendar.getInstance();
        calendarSaved.setTimeInMillis(time);
        Calendar calendarToday = Calendar.getInstance();
        calendarToday.setTimeInMillis(System.currentTimeMillis());
        if (calendarSaved.get(Calendar.YEAR) == calendarToday.get(Calendar.YEAR)) {
            if (calendarSaved.get(Calendar.MONTH) == calendarToday.get(Calendar.MONTH)) {
                if (calendarSaved.get(Calendar.DAY_OF_MONTH) == calendarToday.get(Calendar.DAY_OF_MONTH)) {
                    System.out.println("!!! last visit today");
                    return true;
                }
            }
        }
        System.out.println("!!! last visit not today");
        return false;
    }

    private PendingIntent createPendingIntent(Context context) {
        Intent intent = new Intent(context, SingleActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return pendingIntent;
    }

    private void sendNotification(Context context) {
        int id = 0;// for what? todo
        PendingIntent pendingIntent = createPendingIntent(context);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("5 минут английского языка!")
                        .setContentText("Нажмите, чтобы начать занятие.")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, mBuilder.build());
    }
}
