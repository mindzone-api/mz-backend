package com.mindzone.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public final static long MILLIS_IN_A_DAY = 24 * 60 * 60 * 1000L;

    /**
     * Calculates the minimum birth date (from that date until today)
     * possible from the age informed
     * @param age informed to calculate the minimum birth date
     * @return the minimum birth date
     */
    public static Date calculateBirthFromMinimumAge(int age) {
        LocalDate now = LocalDate.now();
        LocalDate birthDateLimit = now.minusYears(age);

        birthDateLimit = LocalDate.from(LocalDate.of(birthDateLimit.getYear()+1, 12, 31).atStartOfDay());
        return Date.from(birthDateLimit.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Calculates the maximum birth date (from that date backwards)
     * possible from the age informed
     * @param age informed to calculate the maximum birth date
     * @return the maximum birth date
     */
    public static Date calculateBirthFromMaximumAge(int age) {
        LocalDate now = LocalDate.now();
        LocalDate birthDateLimit = now.minusYears(age);

        birthDateLimit = LocalDate.from(LocalDate.of(birthDateLimit.getYear()-1, 12, 31).atStartOfDay());
        return Date.from(birthDateLimit.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

