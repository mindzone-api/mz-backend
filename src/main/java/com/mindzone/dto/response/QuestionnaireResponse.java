package com.mindzone.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuestionnaireResponse implements Serializable {

    private String patientId;
    private Double mood;
    private String observations;
    private Date createdAt;
}
