package com.mindzone.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
@NoArgsConstructor
public class WeekDaySchedule {
    private DayOfWeek day;
    private List<TimeRange> daySchedule;

    public WeekDaySchedule(WeekDaySchedule s) {
        this.day = s.getDay();
        this.daySchedule = s.getDaySchedule().stream().map(TimeRange::new).toList();
    }
}
