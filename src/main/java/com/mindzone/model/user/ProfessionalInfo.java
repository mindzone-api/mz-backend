package com.mindzone.model.user;

import com.mindzone.enums.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.Date;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessionalInfo  implements Serializable {

    private Profession profession;
    private Date activeUntil;
    private String description;
    private Gender gender;
    private SessionModality sessionModality;
    private List<HealthPlan> acceptedHealthPlans;
    private BigDecimal pricePerSession;
    private List<String> clinicPhotos;
    private List<PaymentMethod> paymentMethods;
    private List<Speciality> specialities;
    private Approach approach;
    private String address;
    private String city;
    private String professionalCode;
    Map<DayOfWeek, TimeRange> workingSchedule;
    private List<PatientFeedback> feedbacks;
}
