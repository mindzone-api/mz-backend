package com.mindzone.model.user;

import com.mindzone.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessionalInfo  implements Serializable {

    private Profession profession;
    private List<TherapyModality> therapyModalities;
    private List<PaymentMethod> paymentMethods;
    private Approach approach;
    private List<Speciality> specialities;
    private List<HealthPlan> acceptedHealthPlans;

    private Date activeUntil;
    private String description;
    private BigDecimal pricePerSession;
    private Duration sessionDuration;
    private List<String> clinicPhotos;

    private String professionalCode;
    private List<WeekDaySchedule> availability;
    private List<PatientFeedback> feedbacks;

    private String stripeId;
    private String subscriptionId;
}
