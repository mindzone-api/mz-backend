package com.mindzone.model.therapy;

import com.mindzone.enums.Role;
import com.mindzone.model.AbstractModel;
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
public class Session extends AbstractModel implements Serializable {

    private String observations;
    private Role createdBy;
    private Date endedAt;
    private Double patientMood;
    private Homework currentHomeworkState;
    private List<File> attatchments;

}
