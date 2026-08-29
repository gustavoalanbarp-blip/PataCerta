package com.patacerta.app.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Recebe o alarme agendado pelo ReminderScheduler e dispara a notificação
 * local correspondente (RF04). Usa AlarmManager em vez de um push remoto
 * porque os lembretes são criados e consumidos inteiramente no aparelho.
 */
public class ReminderAlarmReceiver extends BroadcastReceiver {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_MESSAGE = "extra_message";
    public static final String EXTRA_NOTIFICATION_ID = "extra_notification_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(EXTRA_TITLE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, (int) System.currentTimeMillis());

        NotificationHelper.showReminderNotification(
                context,
                notificationId,
                title != null ? title : "Lembrete PataCerta",
                message != null ? message : "Você tem um cuidado pendente."
        );
    }
}
