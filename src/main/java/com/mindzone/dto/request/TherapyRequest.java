package com.mindzone.dto.request;

import com.mindzone.enums.HealthPlan;
import com.mindzone.enums.PaymentMethod;
import com.mindzone.enums.TherapyModality;
import com.mindzone.model.user.WeekDaySchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TherapyRequest {
    private String professionalId;
    private TherapyModality therapyModality;
    private PaymentMethod paymentMethod;
    private HealthPlan healthPlan;
    private List<WeekDaySchedule> schedule;
}
