package com.patacerta.app.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.patacerta.app.data.local.entity.Reminder;

/**
 * Agenda o disparo local de um lembrete usando AlarmManager.setExactAndAllowWhileIdle,
 * garantindo que a notificação apareça mesmo com o app em segundo plano ou
 * o aparelho em modo de economia de energia (Doze).
 */
public final class ReminderScheduler {

    private ReminderScheduler() {}

    public static void schedule(Context context, Reminder reminder, String petName) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) return;

        Intent intent = new Intent(context, ReminderAlarmReceiver.class);
        intent.putExtra(ReminderAlarmReceiver.EXTRA_TITLE, labelForType(reminder.getType()) + ": " + reminder.getTitle());
        intent.putExtra(ReminderAlarmReceiver.EXTRA_MESSAGE, petName + " • " + reminder.getNotes());
        intent.putExtra(ReminderAlarmReceiver.EXTRA_NOTIFICATION_ID, (int) reminder.getId());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                (int) reminder.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        try {
            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    reminder.getDueAtMillis(),
                    pendingIntent
            );
        } catch (SecurityException e) {
            // Em Android 12+ o usuário pode ter revogado a permissão de alarmes
            // exatos nas configurações do sistema. Nesse caso, o lembrete
            // continua salvo no Room e visível na Central de notificações,
            // apenas sem o disparo automático em segundo plano.
        }
    }

    private static String labelForType(String type) {
        switch (type) {
            case Reminder.TYPE_VACCINE: return "Vacina";
            case Reminder.TYPE_MEDICINE: return "Remédio";
            case Reminder.TYPE_WALK: return "Passeio";
            default: return "Lembrete";
        }
    }
}
