package com.mindzone.model.user;

import lombok.Data;

import java.time.DayOfWeek;
import java.util.List;

@Data
public class WeekDaySchedule {
    private DayOfWeek day;
    private List<TimeRange> daySchedule;
}
