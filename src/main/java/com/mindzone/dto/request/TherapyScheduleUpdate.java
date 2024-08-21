package com.mindzone.dto.request;

import com.mindzone.model.user.WeekDaySchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TherapyScheduleUpdate {

    private List<WeekDaySchedule> schedule;
    private Date nextSessionDate;
}
