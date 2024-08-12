package com.mindzone.dto.response;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.user.WeekDaySchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyResponse {

    private String patientId;
    private String professionalId;
    private TherapyStatus therapyStatus;
    private boolean active;
    private Date since;
    private List<WeekDaySchedule> schedule;
}
