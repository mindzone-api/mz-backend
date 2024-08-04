package com.mindzone.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {
    public static Date calculateBirthFromMinimumAge(int age) {
        LocalDate now = LocalDate.now();
        LocalDate birthDateLimit = now.minusYears(age);

        birthDateLimit = LocalDate.from(LocalDate.of(birthDateLimit.getYear()+1, 12, 31).atStartOfDay());
        return Date.from(birthDateLimit.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date calculateBirthFromMaximumAge(int age) {
        LocalDate now = LocalDate.now();
        LocalDate birthDateLimit = now.minusYears(age);

        birthDateLimit = LocalDate.from(LocalDate.of(birthDateLimit.getYear()-1, 12, 31).atStartOfDay());
        return Date.from(birthDateLimit.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

