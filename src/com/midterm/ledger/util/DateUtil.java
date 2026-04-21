package com.midterm.ledger.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateUtil() {
    }

    public static LocalDate parseDate(String text) {
        try {
            return LocalDate.parse(text.trim(), DATE_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("日期格式错误，请输入 yyyy-MM-dd");
        }
    }

    public static YearMonth parseMonth(String text) {
        try {
            return YearMonth.parse(text.trim(), MONTH_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("月份格式错误，请输入 yyyy-MM");
        }
    }

    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String formatMonth(YearMonth month) {
        return month.format(MONTH_FORMATTER);
    }

    public static String nowText() {
        return LocalDateTime.now().format(DATETIME_FORMATTER);
    }
}
