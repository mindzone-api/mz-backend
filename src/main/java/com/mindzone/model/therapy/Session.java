package com.mindzone.model.therapy;

import com.mindzone.enums.Role;
import com.mindzone.model.AbstractModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
