package com.mindzone.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "questionnaire")
public class Questionnaire extends AbstractModel implements Serializable {

    private String patientId;
    private Double mood;
    private String observations;
}
