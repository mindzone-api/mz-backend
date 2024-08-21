package com.mindzone.util;

import com.mindzone.model.user.TimeRange;
import com.mindzone.model.user.WeekDaySchedule;

import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

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
     */
    public static void removeFrom(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2) {
        Iterator<WeekDaySchedule> s1Iterator = s1.iterator();

        while (s1Iterator.hasNext()) {
            WeekDaySchedule schedule1 = s1Iterator.next();
            List<TimeRange> updatedRanges = new ArrayList<>(schedule1.getDaySchedule());

            for (WeekDaySchedule schedule2 : s2) {
                if (schedule1.getDay().equals(schedule2.getDay())) {
                    for (TimeRange range2 : schedule2.getDaySchedule()) {
                        List<TimeRange> tempRanges = new ArrayList<>();

                        for (TimeRange range1 : updatedRanges) {
                            if (range2.getEndsAt() <= range1.getStartsAt() || range2.getStartsAt() >= range1.getEndsAt()) {
                                tempRanges.add(range1);
                            } else {
                                if (range2.getStartsAt() > range1.getStartsAt()) {
                                    tempRanges.add(new TimeRange(range1.getStartsAt(), range2.getStartsAt()));
                                }
                                if (range2.getEndsAt() < range1.getEndsAt()) {
                                    tempRanges.add(new TimeRange(range2.getEndsAt(), range1.getEndsAt()));
                                }
                            }
                        }
                        updatedRanges = tempRanges;
                    }
                }
            }

            if (updatedRanges.isEmpty()) {
                s1Iterator.remove();
            } else {
                schedule1.setDaySchedule(updatedRanges);
            }
        }
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
        // Convert fromDate to LocalDateTime in UTC
        LocalDateTime fromDateTime = LocalDateTime.ofInstant(fromDate.toInstant(), ZoneOffset.UTC);

        // Loop through the schedules to find the next occurrence
        for (WeekDaySchedule schedule : schedules) {
            // Loop through each TimeRange to find the next time
            for (TimeRange timeRange : schedule.getDaySchedule()) {
                // If the current day matches the schedule day
                if (schedule.getDay().equals(fromDateTime.getDayOfWeek())) {
                    // If the current time is before the end of the time range, return the next occurrence
                    if (timeRange.getStartsAt() > fromDateTime.getHour() * 60 + fromDateTime.getMinute()) {
                        return Date.from(fromDateTime.withHour(timeRange.getStartsAt() / 60)
                                .withMinute(timeRange.getStartsAt() % 60)
                                .withSecond(0)
                                .withNano(0)
                                .atZone(ZoneOffset.UTC).toInstant());
                    }
                }
                // If the schedule day is after the current day, find the next day with a scheduled time
                if (schedule.getDay().getValue() > fromDateTime.getDayOfWeek().getValue()) {
                    return Date.from(fromDateTime.with(TemporalAdjusters.next(schedule.getDay()))
                            .withHour(timeRange.getStartsAt() / 60)
                            .withMinute(timeRange.getStartsAt() % 60)
                            .withSecond(0)
                            .withNano(0)
                            .atZone(ZoneOffset.UTC).toInstant());
                }
            }
        }

        // If no time was found in the current week, return the first time in the next week
        WeekDaySchedule firstSchedule = schedules.get(0);
        TimeRange firstTimeRange = firstSchedule.getDaySchedule().get(0);

        return Date.from(fromDateTime.with(TemporalAdjusters.next(firstSchedule.getDay()))
                .withHour(firstTimeRange.getStartsAt() / 60)
                .withMinute(firstTimeRange.getStartsAt() % 60)
                .withSecond(0)
                .withNano(0)
                .atZone(ZoneOffset.UTC).toInstant());
    }


    /**
     * Merges schedules from s2 into s1.
     * @param s1 The schedules to be updated
     * @param s2 The schedules to merge into s1
     */
    public static void mergeWith(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2) {
        Map<DayOfWeek, List<TimeRange>> mergedSchedules = new LinkedHashMap<>();

        // Add all schedules from s1 to the map
        addSchedulesToMap(s1, mergedSchedules);

        // Merge schedules from s2 into the schedules from s1
        mergeSchedules(s2, mergedSchedules);

        // Update the list s1 with the merged schedules, preserving the order
        updateScheduleList(s1, mergedSchedules);
    }

    /**
     * Merges schedules from s2 into s1 with respect to the limit.
     * @param s1 The schedules to be updated
     * @param s2 The schedules to merge into s1
     * @param limit The list of schedules that limit the addition from s2
     */
    public static void mergeWith(List<WeekDaySchedule> s1, List<WeekDaySchedule> s2, List<WeekDaySchedule> limit) {
        Map<DayOfWeek, List<TimeRange>> mergedSchedules = new LinkedHashMap<>();

        // Add all schedules from s1 to the map
        addSchedulesToMap(s1, mergedSchedules);

        // Merge schedules from s2 into the schedules from s1, respecting the limit
        mergeSchedulesWithLimit(s2, mergedSchedules, limit);

        // Update the list s1 with the merged schedules, preserving the order
        updateScheduleList(s1, mergedSchedules);
    }

    private static void addSchedulesToMap(List<WeekDaySchedule> schedules, Map<DayOfWeek, List<TimeRange>> map) {
        for (WeekDaySchedule schedule : schedules) {
            map.putIfAbsent(schedule.getDay(), new ArrayList<>());
            map.get(schedule.getDay()).addAll(schedule.getDaySchedule());
        }
    }

    private static void updateScheduleList(List<WeekDaySchedule> list, Map<DayOfWeek, List<TimeRange>> map) {
        list.clear();
        // Iterate in the order of DayOfWeek enum (Monday to Sunday)
        for (DayOfWeek day : DayOfWeek.values()) {
            List<TimeRange> timeRanges = map.get(day);
            if (timeRanges != null && !timeRanges.isEmpty()) {
                WeekDaySchedule ws = new WeekDaySchedule();
                ws.setDay(day);
                ws.setDaySchedule(timeRanges);
                list.add(ws);
            }
        }
    }

    private static void mergeSchedules(List<WeekDaySchedule> s2, Map<DayOfWeek, List<TimeRange>> mergedSchedules) {
        for (WeekDaySchedule schedule : s2) {
            List<TimeRange> currentSchedules = mergedSchedules.getOrDefault(schedule.getDay(), new ArrayList<>());

            for (TimeRange newRange : schedule.getDaySchedule()) {
                currentSchedules = mergeTimeRanges(currentSchedules, newRange);
            }

            mergedSchedules.put(schedule.getDay(), currentSchedules);
        }
    }

    private static void mergeSchedulesWithLimit(List<WeekDaySchedule> s2, Map<DayOfWeek, List<TimeRange>> mergedSchedules, List<WeekDaySchedule> limit) {
        for (WeekDaySchedule schedule : s2) {
            List<TimeRange> currentSchedules = mergedSchedules.getOrDefault(schedule.getDay(), new ArrayList<>());

            for (TimeRange newRange : schedule.getDaySchedule()) {
                List<TimeRange> updatedSchedules = mergeTimeRanges(currentSchedules, newRange);
                if (!updatedSchedules.isEmpty()) {
                    TimeRange limitedRange = getLimitedRange(updatedSchedules.get(updatedSchedules.size() - 1), limit, schedule.getDay());
                    if (limitedRange != null) {
                        updatedSchedules.set(updatedSchedules.size() - 1, limitedRange);
                    }
                }
                currentSchedules = updatedSchedules;
            }

            mergedSchedules.put(schedule.getDay(), currentSchedules);
        }
    }

    private static List<TimeRange> mergeTimeRanges(List<TimeRange> existingRanges, TimeRange newRange) {
        List<TimeRange> updatedSchedules = new ArrayList<>();
        boolean merged = false;

        for (TimeRange existingRange : existingRanges) {
            if (newRange.getEndsAt() < existingRange.getStartsAt() || newRange.getStartsAt() > existingRange.getEndsAt()) {
                // No overlap: keep the original range
                updatedSchedules.add(existingRange);
            } else {
                // Overlap detected: merge intervals
                newRange = new TimeRange(
                        Math.min(newRange.getStartsAt(), existingRange.getStartsAt()),
                        Math.max(newRange.getEndsAt(), existingRange.getEndsAt())
                );
                merged = true;
            }
        }

        if (merged) {
            // Add the merged range
            updatedSchedules.add(newRange);
        } else {
            // Add the new range
            updatedSchedules.add(newRange);
        }

        return updatedSchedules;
    }

    private static TimeRange getLimitedRange(TimeRange range, List<WeekDaySchedule> limit, DayOfWeek day) {
        for (WeekDaySchedule limitSchedule : limit) {
            if (limitSchedule.getDay().equals(day)) {
                for (TimeRange limitRange : limitSchedule.getDaySchedule()) {
                    if (range.getEndsAt() <= limitRange.getStartsAt() || range.getStartsAt() >= limitRange.getEndsAt()) {
                        // No overlap with the limit range
                        continue;
                    }
                    // Calculate the limited range
                    int newStart = Math.max(range.getStartsAt(), limitRange.getStartsAt());
                    int newEnd = Math.min(range.getEndsAt(), limitRange.getEndsAt());
                    if (newStart < newEnd) {
                        return new TimeRange(newStart, newEnd);
                    }
                }
            }
        }
        return null; // Return null if the range is completely outside the limits
    }
}

