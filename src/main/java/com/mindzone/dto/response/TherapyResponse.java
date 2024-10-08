package com.mindzone.dto.response;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.user.WeekDaySchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyResponse implements Serializable {

    private String id;
    private String patientId;
    private String professionalId;
    private Date since;
    private TherapyStatus therapyStatus;
    private boolean active;
    private Date createdAt;
    private List<WeekDaySchedule> schedule;
    private String nextSessionId;
}
