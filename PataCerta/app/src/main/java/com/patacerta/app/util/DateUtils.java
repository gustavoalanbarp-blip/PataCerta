package com.patacerta.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateUtils {
    private DateUtils() {}

    private static final SimpleDateFormat DISPLAY_FORMAT =
            new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", new Locale("pt", "BR"));

    private static final SimpleDateFormat SHORT_DATE_FORMAT =
            new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

    public static String formatFull(long millis) {
        return DISPLAY_FORMAT.format(new Date(millis));
    }

    public static String formatShort(long millis) {
        return SHORT_DATE_FORMAT.format(new Date(millis));
    }

    /** Retorna algo como "vence em 3 dias" / "venceu há 2 dias" / "vence hoje". */
    public static String relativeDueLabel(long dueMillis) {
        long diffMs = dueMillis - System.currentTimeMillis();
        long days = diffMs / (24 * 60 * 60 * 1000);
        if (days == 0) return "vence hoje";
        if (days > 0) return "vence em " + days + (days == 1 ? " dia" : " dias");
        long overdue = -days;
        return "venceu há " + overdue + (overdue == 1 ? " dia" : " dias");
    }
}
