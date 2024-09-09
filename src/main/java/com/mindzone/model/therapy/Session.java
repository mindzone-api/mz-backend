package com.mindzone.model.therapy;

import com.mindzone.model.AbstractModel;
import com.mindzone.model.Homework;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Document(collection = "session")
public class Session extends AbstractModel implements Serializable {

    private String therapyId;
    private Date date;

    private String professionalObservations;
    private Homework currentHomeworkState;
    private Double patientMood;

    private String PatientObservations;
}
