package com.mindzone.util;

import com.mindzone.model.user.TimeRange;
import com.mindzone.model.user.WeekDaySchedule;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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

    /**
     * Removes all occurrences from s2 in s1, splitting time ranges in s1 when necessary.
     * For example, if s1 has a range from 1200 to 1400 on Monday, and s2 has a range from 1250 to 1300 on Monday,
     * the resulting s1 will have two ranges: 1200 to 1250 and 1300 to 1400.
     * <p>
     * Note 1: s2 will never have a time range that is larger than the corresponding time range in s1.
     * Note 2: Both s1 and s2 are assumed to be ordered lists. The days of the week should be in ascending order
     * (Monday to Sunday), and within each day, the time ranges should be ordered from earliest to latest.
     * If the lists are not sorted, the behavior of this function is not guaranteed.
     *
     * @param s1 schedules to be updated
     * @param s2 schedules to remove from s1
     * @return the final s1 list without s2 occurrences
     */
     public static List<WeekDaySchedule> removeFrom(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2) {
        for (WeekDaySchedule daySchedule2 : s2) {
            DayOfWeek day2 = daySchedule2.getDay();
            WeekDaySchedule daySchedule1 = s1.stream()
                    .filter(ds -> ds.getDay().equals(day2))
                    .findFirst()
                    .orElse(null);

            if (daySchedule1 != null) {
                List<TimeRange> timeRanges1 = daySchedule1.getDaySchedule();
                List<TimeRange> timeRanges2 = daySchedule2.getDaySchedule();

                List<TimeRange> newTimeRanges = new ArrayList<>();

                for (TimeRange range1 : timeRanges1) {
                    boolean hasOverlap = false;
                    for (TimeRange range2 : timeRanges2) {
                        // Case 1: range2 completely inside range1
                        if (range2.getStartsAt() > range1.getStartsAt() && range2.getEndsAt() < range1.getEndsAt()) {
                            newTimeRanges.add(new TimeRange(range1.getStartsAt(), range2.getStartsAt()));
                            newTimeRanges.add(new TimeRange(range2.getEndsAt(), range1.getEndsAt()));
                            hasOverlap = true;
                            break;
                        }
                        // Case 2: range2 equals range1 or partially overlaps the start or end of range1
                        else if (range2.getStartsAt().equals(range1.getStartsAt()) && range2.getEndsAt().equals(range1.getEndsAt())) {
                            hasOverlap = true;
                            break;
                        } else if (range2.getStartsAt().equals(range1.getStartsAt()) && range2.getEndsAt() < range1.getEndsAt()) {
                            newTimeRanges.add(new TimeRange(range2.getEndsAt(), range1.getEndsAt()));
                            hasOverlap = true;
                            break;
                        } else if (range2.getEndsAt().equals(range1.getEndsAt()) && range2.getStartsAt() > range1.getStartsAt()) {
                            newTimeRanges.add(new TimeRange(range1.getStartsAt(), range2.getStartsAt()));
                            hasOverlap = true;
                            break;
                        }
                    }
                    // If no overlap was found, keep the original range
                    if (!hasOverlap) {
                        newTimeRanges.add(range1);
                    }
                }

                if (newTimeRanges.isEmpty()) {
                    s1.remove(daySchedule1);
                } else {
                    daySchedule1.setDaySchedule(newTimeRanges);
                }
            }
        }

        return s1;
    }

    /**
     * Returns the next session time as a Date object based on the provided date and time.
     * The WeekDaySchedule list is expected to be sorted from Monday to Sunday, and within each day, from earlier to later times.
     *
     * @param schedules list of WeekDaySchedule containing recurring weekly sessions.
     * @param fromDate the starting Date from which to search for the next session.
     * @return a Date object representing the next session time, or null if no upcoming session is found.
     */
    public static Date getNextOccurrence(List<WeekDaySchedule> schedules, Date fromDate) {
        // Convert fromDate to LocalDateTime in the local time zone
        LocalDateTime fromDateTime = LocalDateTime.ofInstant(fromDate.toInstant(), ZoneId.systemDefault());

        // Loop through the schedules to find the next occurrence
        for (WeekDaySchedule schedule : schedules) {
            // Loop through each TimeRange to find the next time
            for (TimeRange timeRange : schedule.getDaySchedule()) {
                // If the current day matches the schedule day
                if (schedule.getDay().equals(fromDateTime.getDayOfWeek())) {
                    // If the current time is before the end of the time range, return the next occurrence
                    if (timeRange.getEndsAt() > fromDateTime.getHour() * 60 + fromDateTime.getMinute()) {
                        return Date.from(fromDateTime.withHour(timeRange.getStartsAt() / 60)
                                .withMinute(timeRange.getStartsAt() % 60)
                                .withSecond(0)
                                .withNano(0)
                                .atZone(ZoneId.systemDefault()).toInstant());
                    }
                }
                // If the schedule day is after the current day, find the next day with a scheduled time
                if (schedule.getDay().getValue() > fromDateTime.getDayOfWeek().getValue()) {
                    return Date.from(fromDateTime.with(TemporalAdjusters.next(schedule.getDay()))
                            .withHour(timeRange.getStartsAt() / 60)
                            .withMinute(timeRange.getStartsAt() % 60)
                            .withSecond(0)
                            .withNano(0)
                            .atZone(ZoneId.systemDefault()).toInstant());
                }
            }
        }

        // If no time was found in the current week, return the first time in the next week
        WeekDaySchedule firstSchedule = schedules.get(0);
        TimeRange firstTimeRange = firstSchedule.getDaySchedule().get(0);

        return Date.from(fromDateTime.with(TemporalAdjusters.next(firstSchedule.getDay()))
                .withHour((firstTimeRange.getStartsAt() / 60)-3) // Formatted to brazilian time zone
                .withMinute(firstTimeRange.getStartsAt() % 60)
                .withSecond(0)
                .withNano(0)
                .atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Merges occurrences from s2 into s1.
     * If s2 contains intervals that fill gaps or connect adjacent intervals in s1,
     * they will be merged into a single interval.
     *
     * @param s1 schedules to be updated
     * @param s2 schedules to merge into s1
     */
    public static void mergeWith(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2) {
        for (WeekDaySchedule s2Schedule : s2) {
            for (WeekDaySchedule s1Schedule : s1) {
                if (s1Schedule.getDay().equals(s2Schedule.getDay())) {
                    List<TimeRange> mergedRanges = new ArrayList<>();
                    List<TimeRange> s1Ranges = s1Schedule.getDaySchedule();
                    List<TimeRange> s2Ranges = s2Schedule.getDaySchedule();

                    mergeIterator(mergedRanges, s1Ranges, s2Ranges);

                    // Replace the old schedule with the merged one
                    s1Schedule.setDaySchedule(mergedRanges);
                }
            }
        }
    }

    /**
     * Merges occurrences from s2 into s1, constrained by the limits specified in limit.
     * If s2 contains intervals that can be merged with s1, the merge will be restricted by the intervals in limit.
     *
     * @param s1    schedules to be updated
     * @param s2    schedules to merge into s1
     * @param limit schedules that define the maximum allowable range for merging
     */
    public static void mergeWith(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2, List<WeekDaySchedule> limit) {
        for (WeekDaySchedule s2Schedule : s2) {
            for (WeekDaySchedule s1Schedule : s1) {
                if (s1Schedule.getDay().equals(s2Schedule.getDay())) {
                    List<TimeRange> mergedRanges = new ArrayList<>();
                    List<TimeRange> s1Ranges = s1Schedule.getDaySchedule();
                    List<TimeRange> s2Ranges = s2Schedule.getDaySchedule();
                    List<TimeRange> limitRanges = limit.stream()
                            .filter(l -> l.getDay().equals(s1Schedule.getDay()))
                            .findFirst()
                            .map(WeekDaySchedule::getDaySchedule)
                            .orElse(Collections.emptyList());

                    mergeIterator(mergedRanges, s1Ranges, s2Ranges);

                    // Apply limit constraints
                    List<TimeRange> constrainedRanges = new ArrayList<>();
                    for (TimeRange merged : mergedRanges) {
                        for (TimeRange lim : limitRanges) {
                            if (merged.getEndsAt() > lim.getStartsAt() && merged.getStartsAt() < lim.getEndsAt()) {
                                constrainedRanges.add(new TimeRange(
                                        Math.max(merged.getStartsAt(), lim.getStartsAt()),
                                        Math.min(merged.getEndsAt(), lim.getEndsAt())
                                ));
                            }
                        }
                    }

                    // Replace the old schedule with the constrained one
                    s1Schedule.setDaySchedule(constrainedRanges);
                }
            }
        }

    }

    private static void mergeIterator(List<TimeRange> mergedRanges, List<TimeRange> s1Ranges, List<TimeRange> s2Ranges) {
        int i = 0, j = 0;
        while (i < s1Ranges.size() || j < s2Ranges.size()) {
            TimeRange current;
            if (i < s1Ranges.size() && (j >= s2Ranges.size() || s1Ranges.get(i).getStartsAt() <= s2Ranges.get(j).getStartsAt())) {
                current = s1Ranges.get(i++);
            } else {
                current = s2Ranges.get(j++);
            }

            if (!mergedRanges.isEmpty() && mergedRanges.get(mergedRanges.size() - 1).getEndsAt() >= current.getStartsAt()) {
                // Merge overlapping or adjacent ranges
                mergedRanges.get(mergedRanges.size() - 1)
                        .setEndsAt(Math.max(mergedRanges.get(mergedRanges.size() - 1).getEndsAt(), current.getEndsAt()));
            } else {
                mergedRanges.add(current);
            }
        }
    }
}

