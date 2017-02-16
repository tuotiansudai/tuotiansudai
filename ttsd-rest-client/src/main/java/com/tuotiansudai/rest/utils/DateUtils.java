package com.tuotiansudai.rest.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");

    public static String serializeDate(Date value) throws IOException {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(value.toInstant(), ZONE_ID);
        return serializeDate(localDateTime);
    }

    public static String serializeDate(LocalDate value) throws IOException {
        return value.format(DATE_FORMATTER);
    }

    public static String serializeDate(LocalDateTime value) throws IOException {
        return value.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate deserializeLocalDate(String dateTimeString) throws IOException {
        return deserializeLocalDateTime(dateTimeString).toLocalDate();
    }

    public static Date deserializeDate(String dateTimeString) throws IOException {
        return Date.from(deserializeLocalDateTime(dateTimeString).toInstant(ZoneOffset.ofHours(8)));
    }

    public static LocalDateTime deserializeLocalDateTime(String dateTimeString) throws IOException {
        return DATE_TIME_FORMATTER.parse(dateTimeString, LocalDateTime::from);
    }
}
