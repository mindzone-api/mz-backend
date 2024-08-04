package com.mindzone.model.user;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;

@Data
public class WeekDayAvailability {
    private DayOfWeek day;
    private List<TimeRange> daySchedule;
}
