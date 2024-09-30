package com.uni.doit.framework.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {

    // LocalDateTime 객체를 yyyy-MM-dd HH:mm:ss 형식의 문자열로 변환하는 메서드
    public static String formatDateTime(Object dateTime) {
        if (dateTime instanceof LocalDateTime) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return ((LocalDateTime) dateTime).format(dateTimeFormatter);
        } else if (dateTime instanceof Date) {
            // Date 객체도 처리해야 하는 경우 기존 방식 사용
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateTimeFormat.format((Date) dateTime);
        }
        return dateTime.toString();
    }

    // LocalDate 객체를 yyyy-MM-dd 형식의 문자열로 변환하는 메서드
    public static String formatDate(Object date) {
        if (date instanceof LocalDate) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return ((LocalDate) date).format(dateFormatter);
        } else if (date instanceof Date) {
            // Date 객체도 처리해야 하는 경우 기존 방식 사용
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format((Date) date);
        }
        return date.toString();
    }
}
