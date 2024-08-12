package com.mindzone.util;

import com.mindzone.model.user.TimeRange;
import com.mindzone.model.user.WeekDaySchedule;

import java.time.DayOfWeek;
import java.util.List;

public class WeekDayScheduleUtil {

    /**
     * Analyses if the schedule informed in s2 fits in the s1 schedule.
     * The function considers that both schedules are sorted by periods of time and days of week,
     * starting from Monday to Sunday, 00AM to 11:59PM.
     * @param s1 schedule to analyse if the other one fits in
     * @param s2 schedule to analyse if fits in s1
     * @return a boolean that indicates if s2 fits in s1
     */
    public static boolean fitsIn(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2) {
        for (WeekDaySchedule daySchedule2 : s2) {
            DayOfWeek day2 = daySchedule2.getDay();
            WeekDaySchedule daySchedule1 = s1.stream()
                    .filter(ds -> ds.getDay().equals(day2))
                    .findFirst()
                    .orElse(null);
            if (daySchedule1 == null) {
                return false; // s2 has a day not present in s1
            }

            List<TimeRange> timeRanges1 = daySchedule1.getDaySchedule();
            List<TimeRange> timeRanges2 = daySchedule2.getDaySchedule();

            int i = 0, j = 0;
            while (i < timeRanges1.size() && j < timeRanges2.size()) {
                TimeRange range1 = timeRanges1.get(i);
                TimeRange range2 = timeRanges2.get(j);

                if (range2.getStartsAt() >= range1.getStartsAt() && range2.getEndsAt() <= range1.getEndsAt()) {
                    j++; // range2 fits in range1
                } else {
                    i++; // move to the next range in s1
                }
            }

            if (j < timeRanges2.size()) {
                return false; // there are ranges in s2 that do not fit in s1
            }
        }
        return true; // all days and times in s2 fit in s1
    }

    /**
     * Checks if there is any time overlap (conflict) between the schedules in s1 and s2.
     * The function considers that both schedules are sorted by periods of time and days of the week,
     * starting from Monday to Sunday, 00AM to 11:59PM.
     * @param s1 first schedule list to check for overlaps
     * @param s2 second schedule list to check for overlaps
     * @return a boolean indicating whether there is a time conflict between s1 and s2
     */
    public static boolean overlaps(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2) {
        for (WeekDaySchedule daySchedule1 : s1) {
            DayOfWeek day1 = daySchedule1.getDay();
            WeekDaySchedule daySchedule2 = s2.stream()
                    .filter(ds -> ds.getDay().equals(day1))
                    .findFirst()
                    .orElse(null);

            if (daySchedule2 != null) {
                List<TimeRange> timeRanges1 = daySchedule1.getDaySchedule();
                List<TimeRange> timeRanges2 = daySchedule2.getDaySchedule();

                int i = 0, j = 0;
                while (i < timeRanges1.size() && j < timeRanges2.size()) {
                    TimeRange range1 = timeRanges1.get(i);
                    TimeRange range2 = timeRanges2.get(j);

                    // Adjusted condition to allow adjacent time ranges
                    if (range1.getEndsAt() > range2.getStartsAt() && range1.getStartsAt() < range2.getEndsAt()) {
                        return true; // There is an overlap
                    }

                    if (range1.getEndsAt() <= range2.getStartsAt()) {
                        i++; // Move to the next range in s1
                    } else {
                        j++; // Move to the next range in s2
                    }
                }
            }
        }

        return false; // No overlap found
    }
}

