package com.mindzone.dto.request;


import com.mindzone.enums.HealthPlan;
import com.mindzone.enums.PaymentMethod;
import com.mindzone.enums.TherapyModality;
import com.mindzone.model.user.WeekDaySchedule;
import lombok.*;

import java.util.Date;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyUpdate {
    private TherapyModality therapyModality;
    private PaymentMethod paymentMethod;
    private HealthPlan healthPlan;
    private List<WeekDaySchedule> schedule;
    private String url;
}
