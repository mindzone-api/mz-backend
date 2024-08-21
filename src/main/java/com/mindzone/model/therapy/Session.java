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
public class Session extends AbstractModel implements Serializable {

    private String ProfessionalObservations;
    private String PatientObservations;
    private Date date;
    private Double patientMood;
    private Homework currentHomeworkState;
    private List<File> attatchments;

    public Session(Date date) {
        this.date = date;
    }
}
