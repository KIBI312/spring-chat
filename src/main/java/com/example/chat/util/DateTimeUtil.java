package com.example.chat.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final String TIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMATTER);
        return LocalDateTime.now().format(formatter);
    }
}
