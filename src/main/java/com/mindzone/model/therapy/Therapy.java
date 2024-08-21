package com.mindzone.model.therapy;

import com.mindzone.enums.HealthPlan;
import com.mindzone.enums.PaymentMethod;
import com.mindzone.enums.TherapyModality;
import com.mindzone.enums.TherapyStatus;
import com.mindzone.model.AbstractModel;
import com.mindzone.model.user.WeekDaySchedule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "therapy")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Therapy extends AbstractModel implements Serializable {

    private String patientId;
    private String professionalId;
    private Date since;
    private TherapyModality therapyModality;
    private TherapyStatus therapyStatus;
    private String url;
    private Boolean active;
    private PaymentMethod paymentMethod;
    private HealthPlan healthPlan;
    private List<WeekDaySchedule> schedule;
    private String nextSessionId;
}
